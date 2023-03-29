package com.tdei.filesvc.gtfsflex.model;

import com.tdei.filesvc.common.model.MetaValidationError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.tdei.filesvc.common.model.MetaErrorCodes.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class GtfsFlexUploadTests {

    @Test
    void testCollectedBy() {
        GtfsFlexUpload upload = new GtfsFlexUpload();
        upload.setCollectedBy("morethan50charsmorethan50charsmorethan50charsmorethan50charsmorethan50charsmorethan50chars");
        List<MetaValidationError> errors = upload.isMetadataValidated();
        assertThat(errors.size()).isNotEqualTo(0);
        MetaValidationError firstError = errors.get(0);
        assertThat(firstError.getCode()).isEqualTo(INVALID_COLLECTEDBY_LENGTH);
    }

    @Test
    void testCollectionDate() {
        GtfsFlexUpload upload = new GtfsFlexUpload();
        upload.setCollectedBy("collectedBy");
        upload.setDataSource("3rdParty");
        upload.setFlexSchemaVersion("v2.0");
        upload.setCollectionMethod("other");

        List<MetaValidationError> errors = upload.isMetadataValidated();
        assertThat(errors.size()).isNotEqualTo(0);
        // Get the first error
        MetaValidationError invalidDateError = errors.get(0);
        assertThat(invalidDateError.getCode()).isEqualTo(NO_COLLECTION_DATE);

        // Invalid/malformed date
        upload.setCollectionDate("2023-03-02T04:22");
        //2023-03-02T04:22:42.493Z // Correct time zone
        List<MetaValidationError> malformedErrors = upload.isMetadataValidated();
        assertThat(malformedErrors.size()).isNotEqualTo(0);
        MetaValidationError malformedError = malformedErrors.get(0);
        assertThat(malformedError.getCode()).isEqualTo(MALFORMED_COLLECTION_DATE);

        // Future date with offset (timezone)
        upload.setCollectionDate("2024-04-29T10:30:00+05:30");
        List<MetaValidationError> metaErrors = upload.isMetadataValidated();
        assertThat(metaErrors.size()).isNotEqualTo(0);
        MetaValidationError futureError = metaErrors.get(0);
        assertThat(futureError.getCode()).isEqualTo(COLLECTION_DATE_FUTURE);

        //Future date with UTC timezone
        upload.setCollectionDate("2024-04-29T10:30:00+05:30");
        List<MetaValidationError> utcFutureErrors = upload.isMetadataValidated();
        assertThat(utcFutureErrors.size()).isNotEqualTo(0);
        MetaValidationError utcFutureError = utcFutureErrors.get(0);
        assertThat(utcFutureError.getCode()).isEqualTo(COLLECTION_DATE_FUTURE);

        // Valid ones
        // Past date with timezone
        upload.setCollectionDate("2023-03-29T10:30:00+05:30");
        List<MetaValidationError> noErrors = upload.isMetadataValidated();
        assertThat(noErrors.size()).isEqualTo(0);

        // Past date with UTC
        upload.setCollectionDate("2023-03-02T04:22:42.493Z");
        List<MetaValidationError> utcNoErrors = upload.isMetadataValidated();
        assertThat(utcNoErrors.size()).isEqualTo(0);

    }

    @Test
    void testCollectionMethod() {
        GtfsFlexUpload upload = new GtfsFlexUpload();
        upload.setCollectedBy("collectedBy");
        upload.setCollectionDate("2023-03-02T04:22:42.493Z");
        upload.setDataSource("3rdParty");
        upload.setFlexSchemaVersion("v2.0");
        // No collection_method
        List<MetaValidationError> errors = upload.isMetadataValidated();
        assertThat(errors.size()).isNotEqualTo(0);


        // invalid collection method
        upload.setCollectionMethod("junkmethod");
        List<MetaValidationError> invalidCollectionMethod = upload.isMetadataValidated();
        assertThat(invalidCollectionMethod.size()).isNotEqualTo(0);
        // method to be invalid collection method
        MetaValidationError metaValidationError = invalidCollectionMethod.get(0);
        assertThat(metaValidationError.getCode()).isEqualTo(INVALID_COLLECTION_METHOD);

        // valid one
        upload.setCollectionMethod("other"); // Can be replaced with `manual`,`transform`,`generated`
        List<MetaValidationError> validCollectionMethod = upload.isMetadataValidated();
        assertThat(validCollectionMethod.size()).isEqualTo(0);
    }

    @Test
    void testDataSource() {
        GtfsFlexUpload upload = new GtfsFlexUpload();
        upload.setCollectedBy("collectedBy");
        upload.setCollectionDate("2023-03-02T04:22:42.493Z");
        upload.setCollectionMethod("other");
        upload.setFlexSchemaVersion("v2.0");
        List<MetaValidationError> errors = upload.isMetadataValidated();
        assertThat(errors.size()).isNotEqualTo(0);
        MetaValidationError noDataSourceError = errors.get(0);
        assertThat(noDataSourceError.getCode()).isEqualTo(NO_DATA_SOURCE);
        // Invalid datasource
        upload.setDataSource("testdatasource");
        List<MetaValidationError> dsErrors = upload.isMetadataValidated();
        assertThat(dsErrors.size()).isNotEqualTo(0);
        MetaValidationError invalidDatasourceError = dsErrors.get(0);
        assertThat(invalidDatasourceError.getCode()).isEqualTo(INVALID_DATA_SOURCE);

        // valid datasource
        upload.setDataSource("3rdParty");
        List<MetaValidationError> noErrors = upload.isMetadataValidated();
        assertThat(noErrors.size()).isEqualTo(0);
    }


    @Test
    void testVersionSchema() {
        GtfsFlexUpload upload = new GtfsFlexUpload();
        upload.setCollectedBy("collectedBy");
        upload.setCollectionDate("2023-03-02T04:22:42.493Z");
        upload.setCollectionMethod("other");
        upload.setDataSource("3rdParty");
        List<MetaValidationError> errors = upload.isMetadataValidated();

        assertThat(errors.size()).isNotEqualTo(0);
        MetaValidationError noSchemaError = errors.get(0);
        assertThat(noSchemaError.getCode()).isEqualTo(NO_FLEX_SCHEMA);
        // invalid schema version
        upload.setFlexSchemaVersion("v1.0");
        List<MetaValidationError> invalidSchemaErrors = upload.isMetadataValidated();
        assertThat(invalidSchemaErrors.size()).isNotEqualTo(0);
        MetaValidationError invalidSchemaError = invalidSchemaErrors.get(0);
        assertThat(invalidSchemaError.getCode()).isEqualTo(INVALID_FLEX_SCHEMA);
        upload.setFlexSchemaVersion("v2.0");
        List<MetaValidationError> noErrors = upload.isMetadataValidated();
        assertThat(noErrors.size()).isEqualTo(0);


    }


}
