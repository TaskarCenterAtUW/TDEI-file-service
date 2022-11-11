package com.tdei.filesvc.gtfspathways.mapper;

import com.tdei.filesvc.gtfspathways.model.GtfsPathwaysUpload;
import com.tdei.filesvc.gtfspathways.model.GtfsPathwaysUploadMessage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GtfsPathwaysUploadMapper {

    GtfsPathwaysUploadMapper INSTANCE = Mappers.getMapper(GtfsPathwaysUploadMapper.class);

    GtfsPathwaysUploadMessage fromGtfsPathwaysUpload(GtfsPathwaysUpload gtfsPathwaysUpload);
}
