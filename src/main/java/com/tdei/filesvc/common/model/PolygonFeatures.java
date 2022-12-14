package com.tdei.filesvc.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PolygonFeatures {
    @JsonProperty("type")
    private String type = null;

    @JsonProperty("properties")
    private Object properties = null;

    @JsonProperty("geometry")
    private PolygonGeometry geometry = null;
}
