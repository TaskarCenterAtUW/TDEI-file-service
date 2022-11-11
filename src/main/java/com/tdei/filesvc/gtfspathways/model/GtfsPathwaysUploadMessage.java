package com.tdei.filesvc.gtfspathways.model;

import lombok.Data;

@Data
public class GtfsPathwaysUploadMessage extends GtfsPathwaysUpload {
    private String fileUploadPath;
}
