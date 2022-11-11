package com.tdei.filesvc.gtfsflex.model;

import lombok.Data;

@Data
public class GtfsFlexUploadMessage extends GtfsFlexUpload {
    private String fileUploadPath;
}
