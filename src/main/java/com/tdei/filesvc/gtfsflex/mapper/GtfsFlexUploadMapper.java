package com.tdei.filesvc.gtfsflex.mapper;

import com.tdei.filesvc.gtfsflex.model.GtfsFlexUpload;
import com.tdei.filesvc.gtfsflex.model.GtfsFlexUploadMessage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GtfsFlexUploadMapper {

    GtfsFlexUploadMapper INSTANCE = Mappers.getMapper(GtfsFlexUploadMapper.class);

    GtfsFlexUploadMessage fromGtfsFlexUpload(GtfsFlexUpload gtfsFlexUpload);
}
