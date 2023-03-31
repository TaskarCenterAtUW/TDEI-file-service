package com.tdei.filesvc.osw.model;

import com.tdei.filesvc.common.model.MetaValidationError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.tdei.filesvc.common.model.MetaErrorCodes.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Test cases for OswUpload
 */
@ExtendWith(MockitoExtension.class)
public class OswUploadTests {

    static OswUpload getDummyOswUpload() {
        OswUpload upload = new OswUpload();
        upload.setCollectedBy("collectedBy");
        upload.setDataSource("3rdParty");
        upload.setOswSchemaVersion("v0.1");
        upload.setCollectionMethod("other");
        upload.setCollectionDate("2023-03-02T04:22:42.493Z");
        upload.setPublicationDate("2023-03-02T04:22:42.493Z");
        return upload;
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Test
    void testCollectedBy() {
        OswUpload upload = OswUploadTests.getDummyOswUpload() ;
        upload.setCollectedBy("morethan50charsmorethan50charsmorethan50charsmorethan50charsmorethan50charsmorethan50chars");
        List<MetaValidationError> errors = upload.isMetadataValidated();
        assertThat(errors.size()).isNotEqualTo(0);
        MetaValidationError firstError = errors.get(0);
        assertThat(firstError.getCode()).isEqualTo(INVALID_COLLECTEDBY_LENGTH);
    }

    @Test
    void testCollectionDate() {
        OswUpload upload = OswUploadTests.getDummyOswUpload() ;
        upload.setCollectionDate(null); // first set the collection date to null

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
        OswUpload upload = OswUploadTests.getDummyOswUpload() ;
        upload.setCollectionMethod(null);

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
        OswUpload upload = OswUploadTests.getDummyOswUpload() ;
        upload.setDataSource(null);
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
        OswUpload upload = OswUploadTests.getDummyOswUpload() ;
        upload.setOswSchemaVersion(null);

        List<MetaValidationError> errors = upload.isMetadataValidated();

        assertThat(errors.size()).isNotEqualTo(0);
        MetaValidationError noSchemaError = errors.get(0);
        assertThat(noSchemaError.getCode()).isEqualTo(NO_OSW_SCHEMA);
        // invalid schema version
        upload.setOswSchemaVersion("v1.0");
        List<MetaValidationError> invalidSchemaErrors = upload.isMetadataValidated();
        assertThat(invalidSchemaErrors.size()).isNotEqualTo(0);
        MetaValidationError invalidSchemaError = invalidSchemaErrors.get(0);
        assertThat(invalidSchemaError.getCode()).isEqualTo(INVALID_OSW_SCHEMA);
        upload.setOswSchemaVersion("v0.1");
        List<MetaValidationError> noErrors = upload.isMetadataValidated();
        assertThat(noErrors.size()).isEqualTo(0);


    }

    @Test
    void testPublicationDate() {
        OswUpload upload = getDummyOswUpload();
        upload.setPublicationDate(null);
        List<MetaValidationError> errors = upload.isMetadataValidated();
        assertThat(errors.size()).isNotEqualTo(0);
        MetaValidationError noPublicationDate = errors.get(0);
        assertThat(noPublicationDate.getCode()).isEqualTo(NO_PUBLICATION_DATE);
        // future date
        //2023-03-02T04:22:42.493Z
        upload.setPublicationDate("2025-03-02T04:22:42.493Z");
        errors = upload.isMetadataValidated();
        assertThat(errors.size()).isNotEqualTo(0);
        MetaValidationError futurePublicationDate = errors.get(0);
        assertThat(futurePublicationDate.getCode()).isEqualTo(PUBLICATION_DATE_FUTURE);
        // valid publication date
        upload.setPublicationDate("2023-03-02T04:22:42.493Z");
        errors = upload.isMetadataValidated();
        assertThat(errors.size()).isEqualTo(0);


    }

//    @Test
//    void  testValidFrom(){
//
//        OswUpload upload = OswUploadTests.getDummyOswUpload();
//
//        upload.setValidFrom(null);
//
//        List<MetaValidationError> errors = upload.isMetadataValidated();
//        assertThat(errors.size()).isNotEqualTo(0);
//        MetaValidationError noValidFrom = errors.get(0);
//        assertThat(noValidFrom.getCode()).isEqualTo(NO_VALID_FROM);
//
//        // Invalid format for valid from
//        upload.setValidFrom("2023-04-23");
//        errors = upload.isMetadataValidated();
//        assertThat(errors.size()).isNotEqualTo(0);
//        MetaValidationError invalidValidFrom = errors.get(0);
//        assertThat(invalidValidFrom.getCode()).isEqualTo(MALFORMED_VALID_FROM);
//
//        // Should not be more than 366 days from current date
//        LocalDateTime oneYearLater = LocalDateTime.now().plusDays(367);
//        //2023-03-02T04:22:42.493Z
//        String formattedDateTime = oneYearLater.format(formatter);
//        upload.setValidFrom(formattedDateTime);
//        errors = upload.isMetadataValidated();
//        assertThat(errors.size()).isNotEqualTo(0);
//        MetaValidationError oneYearLaterError = errors.get(0);
//        assertThat(oneYearLaterError.getCode()).isEqualTo(VALID_FROM_MORE_THAN_YEAR);
//
//        // Valid date should be accepted
//        LocalDateTime now = LocalDateTime.now();
//        String nowDateString = now.format(formatter);
//        upload.setValidFrom(nowDateString);
//        errors = upload.isMetadataValidated();
//        assertThat(errors.size()).isEqualTo(0);
//    }
//
//    @Test
//    void  testValidTo(){
//        OswUpload upload = OswUploadTests.getDummyOswUpload();
//
//        upload.setValidTo(null);
//
//        List<MetaValidationError> errors = upload.isMetadataValidated();
//        assertThat(errors.size()).isNotEqualTo(0);
//        MetaValidationError noValidFrom = errors.get(0);
//        assertThat(noValidFrom.getCode()).isEqualTo(NO_VALID_TO);
//
//        // Invalid valid_to date
//        upload.setValidTo("2023-02-3");
//        errors = upload.isMetadataValidated();
//        assertThat(errors.size()).isNotEqualTo(0);
//        MetaValidationError invalidValidTo = errors.get(0);
//        assertThat(invalidValidTo.getCode()).isEqualTo(MALFORMED_VALID_TO);
//
//        // should be less than valid_from
//        LocalDateTime validFrom = LocalDateTime.now().plusDays(2);
//        LocalDateTime validTo = LocalDateTime.now();
//        String validFromString = validFrom.format(formatter);
//        String validToString = validTo.format(formatter);
//        upload.setValidFrom(validFromString);
//        upload.setValidTo(validToString);
//        errors = upload.isMetadataValidated();
//        assertThat(errors.size()).isNotEqualTo(0);
//        MetaValidationError invalidToFroError = errors.get(0);
//        assertThat(invalidToFroError.getCode()).isEqualTo(VALID_FROM_AFTER_TO);
//
//        // Valid entity for validation
//        LocalDateTime now = LocalDateTime.now();
//        String nowDateString = now.format(formatter);
//        upload.setValidFrom(nowDateString);
//        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
//        upload.setValidTo(tomorrow.format(formatter));
//        errors = upload.isMetadataValidated();
//        assertThat(errors.size()).isEqualTo(0);
//    }
}
