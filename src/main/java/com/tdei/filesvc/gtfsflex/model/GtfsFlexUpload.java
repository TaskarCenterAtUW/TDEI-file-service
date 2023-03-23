package com.tdei.filesvc.gtfsflex.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tdei.filesvc.common.model.GeoJsonObject;
import com.tdei.filesvc.common.model.MetaErrorMessages;
import com.tdei.filesvc.common.model.MetaValidationError;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.tdei.filesvc.common.model.MetaErrorCodes.*;

/**
 * Represents meta data needed to upload a gtfs_flex data file
 */
@Schema(description = "Represents meta data needed to upload a gtfs_flex data file")
@Validated
@Data
public class GtfsFlexUpload {
    @Schema(required = true, description = "tdei-assigned organization id. Represented as UUID. Organization ids can be retrieved using the /api/v1.0/organizations path.")
    @NotNull
    @JsonProperty("tdei_org_id")
    private String tdeiOrgId = null;

    @Schema(required = true, description = "TDEI id of a GTFS Flex service")
    @NotNull
    @JsonProperty("tdei_service_id")
    private String tdeiServiceId = null;

    @Schema(required = true,description = "Description of who data was collected by. See Best Practices document for information on how to format this string.")
    @NotNull
    @JsonProperty("collected_by")
    private String collectedBy = null;

    @Schema(required = true, description = "date-time that data was collected")
    @NotNull
    @Valid
    @JsonProperty("collection_date")
    @JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime collectionDate = null;

    @Schema(required = true, description = "Method by which the data was collected. See Best Practices document for information on how to format this string.")
    @NotNull
    @JsonProperty("collection_method")
    private String collectionMethod = null;

    @Schema(required = true, description = "date from which this file is valid")
    @NotNull
    @Valid
    @JsonProperty("valid_from")
    @JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime validFrom = null;

    @Schema(description = "date until which this data is valid")
    @Valid
    @JsonProperty("valid_to")
    @JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime validTo = null;

    @Schema(required = true, description = "Description of data source or sources from which the data was collected. See Best Practices document for information on how to format this string.")
    @NotNull
    @JsonProperty("data_source")
    private String dataSource = null;

    @Schema(required = true, description = "")
    @NotNull
    @JsonProperty("polygon")
    private GeoJsonObject polygon = null;

    @Schema(required = true, description = "version of gtfs flex schema this file conforms to")
    @NotNull
    @JsonProperty("flex_schema_version")
    private String flexSchemaVersion = null;

    public List<MetaValidationError> isMetadataValidated(){
        ArrayList<MetaValidationError> errors = new ArrayList<>();

        // Collected By
        if(this.getCollectedBy().length() > 50){
            MetaValidationError collectedByError = new MetaValidationError(INVALID_COLLECTEDBY_LENGTH, MetaErrorMessages.COLLECTED_BY_LENGTHY);//new MetaValidationError("Invalid length","collected_by should be less than 50 characters");
            errors.add(collectedByError);
        }
        // Collection date
        if(this.getCollectionDate() == null){
            MetaValidationError collectionDateError = new MetaValidationError(NO_COLLECTION_DATE,MetaErrorMessages.NO_COLLECTION_DATE);
            errors.add(collectionDateError);
        }
       else if(this.getCollectionDate().isAfter(LocalDateTime.now())){
            // Cannot be future date
            MetaValidationError collectionDateError = new MetaValidationError(COLLECTION_DATE_FUTURE,MetaErrorMessages.COLLECTION_DATE_FUTURE);
            errors.add(collectionDateError);
        }

       // Collection method
        if(this.getCollectionMethod() == null){
            MetaValidationError collectionMethodError = new MetaValidationError(NO_COLLECTION_METHOD,MetaErrorMessages.INVALID_COLLECTION_METHOD);
            errors.add(collectionMethodError);
        }
        else {
            String collectionMethodLowerCase = this.getCollectionMethod().toLowerCase();
            List<String> validCollectionMethods = new ArrayList<>(
                    List.of("manual",
                            "transform",
                            "generated",
                            "other"));
            if(!validCollectionMethods.contains(collectionMethodLowerCase)){
                MetaValidationError invalidCollectionMethodError = new MetaValidationError(INVALID_COLLECTION_METHOD,MetaErrorMessages.INVALID_COLLECTION_METHOD);
                errors.add(invalidCollectionMethodError);
            }
        }

        // Data source
        if(this.getDataSource() == null){
            MetaValidationError datasourceError = new MetaValidationError(NO_DATA_SOURCE,MetaErrorMessages.INVALID_DATA_SOURCE);
            errors.add(datasourceError);
        }
        else {
            List<String> validDataSource = new ArrayList<>(List.of("3rdParty","TDEITools","InHouse"));
            if(!validDataSource.contains(this.getDataSource())){
                MetaValidationError invalidDatasourceError = new MetaValidationError(INVALID_DATA_SOURCE,MetaErrorMessages.INVALID_DATA_SOURCE);
                errors.add(invalidDatasourceError);
            }
        }

         return errors;
    }
}
