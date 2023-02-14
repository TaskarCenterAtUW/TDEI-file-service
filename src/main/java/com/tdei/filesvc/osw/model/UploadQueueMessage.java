package com.tdei.filesvc.osw.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class UploadQueueMessage {

    @JsonProperty("tdei_record_id")
    private String tdeiRecordId;
    @JsonProperty("tdei_org_id")
    private String orgId;
    @JsonProperty("user_id")
    private String userId;
    private String stage = "Flex-Upload";
    private Object request;
    private Map<String, String> meta;
    private ResponseInfo response;
}

