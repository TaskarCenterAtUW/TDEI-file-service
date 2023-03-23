package com.tdei.filesvc.gtfsflex.model;

import com.tdei.filesvc.common.model.MetaValidationError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
 class GtfsFlexUploadTests {

    @Test
    void testCollectedBy(){
        GtfsFlexUpload upload = new GtfsFlexUpload();
        upload.setCollectedBy("morethan50charsmorethan50charsmorethan50charsmorethan50charsmorethan50charsmorethan50chars");
        List<MetaValidationError> errors = upload.isMetadataValidated();
        assertThat(errors.size()).isNotEqualTo(0);
    }

    @Test
    void testCollectionDate(){
        GtfsFlexUpload upload = new GtfsFlexUpload();
        upload.setCollectedBy("collectedBy");
        List<MetaValidationError> errors = upload.isMetadataValidated();
        assertThat(errors.size()).isNotEqualTo(0);
        // Get the first error
        MetaValidationError invalidDateError = errors.get(0);
        assertThat(invalidDateError.getErrorName()).isEqualTo("Invalid format");

        // Assert for future dates in collection date
        upload.setCollectionDate(LocalDateTime.now().plusHours(2));
        List<MetaValidationError> metaErrors = upload.isMetadataValidated();
        assertThat(metaErrors.size()).isNotEqualTo(0);
        MetaValidationError futureError = metaErrors.get(0);
        assertThat(futureError.getErrorName()).isEqualTo("Invalid collection date");
    }

    @Test
    void testCollectionMethod() {
        GtfsFlexUpload upload = new GtfsFlexUpload();
        upload.setCollectedBy("collectedBy");
        upload.setCollectionDate(LocalDateTime.now());
        // No collection_method
        List<MetaValidationError> errors = upload.isMetadataValidated();
        assertThat(errors.size()).isEqualTo(1);

        // invalid collection method
        upload.setCollectionMethod("junkmethod");
        List<MetaValidationError> invalidCollectionMethod = upload.isMetadataValidated();
        assertThat(invalidCollectionMethod.size()).isNotEqualTo(0);

        // valid one
        upload.setCollectionMethod("other"); // Can be replaced with `manual`,`transform`,`generated`
        List<MetaValidationError> validCollectionMethod = upload.isMetadataValidated();
        assertThat(validCollectionMethod.size()).isEqualTo(0);
    }

    @Test
    void testDataSource(){
        GtfsFlexUpload upload = new GtfsFlexUpload();
        upload.setCollectedBy("collectedBy");
        upload.setCollectionDate(LocalDateTime.now());
        upload.setCollectionMethod("other");


    }



}
