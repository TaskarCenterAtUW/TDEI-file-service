package com.tdei.filesvc.gtfsflex.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GtfsFlexUploadMessage extends GtfsFlexUpload {
    @JsonProperty("file_upload_path")
    private String fileUploadPath;
    @JsonProperty("tdei_record_id")
    private String tdeiRecordId;
    @JsonProperty("user_id")
    private String userId;
}
