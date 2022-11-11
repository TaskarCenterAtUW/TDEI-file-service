package com.tdei.filesvc.osw.mapper;

import com.tdei.filesvc.osw.model.OswUpload;
import com.tdei.filesvc.osw.model.OswUploadMessage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OswUploadMapper {

    OswUploadMapper INSTANCE = Mappers.getMapper(OswUploadMapper.class);

    OswUploadMessage fromOswUpload(OswUpload oswUpload);
}
