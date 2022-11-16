package com.tdei.filesvc.osw.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OswUploadMessage extends OswUpload {
    @JsonProperty("file_upload_path")
    private String fileUploadPath;
    @JsonProperty("user_id")
    private String userId;
}
