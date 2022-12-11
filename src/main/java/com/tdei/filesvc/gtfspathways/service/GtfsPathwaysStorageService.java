package com.tdei.filesvc.gtfspathways.service;

import com.tdei.filesvc.common.model.QueueMessage;
import com.tdei.filesvc.common.service.EventBusService;
import com.tdei.filesvc.common.service.StorageService;
import com.tdei.filesvc.core.config.ApplicationProperties;
import com.tdei.filesvc.core.config.exception.handler.exceptions.FileExtensionNotAllowedException;
import com.tdei.filesvc.gtfspathways.mapper.GtfsPathwaysUploadMapper;
import com.tdei.filesvc.gtfspathways.model.GtfsPathwaysUpload;
import com.tdei.filesvc.gtfspathways.model.GtfsPathwaysUploadMessage;
import com.tdei.filesvc.gtfspathways.service.contract.IGtfsPathwaysStorageService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.tdei.filesvc.common.utils.Utility.getExtensionByStringHandling;

@Service
@RequiredArgsConstructor
public class GtfsPathwaysStorageService implements IGtfsPathwaysStorageService {
    private final StorageService storageService;
    private final EventBusService eventBusService;
    private final ApplicationProperties applicationProperties;

    @Override
    public String uploadBlob(GtfsPathwaysUpload meta, String tdeiOrgId, String userId, MultipartFile file) throws FileUploadException {
        String tdeiUniqueRecordId = UUID.randomUUID().toString().replace("-", "");
        String fileExtension = getExtensionByStringHandling(file.getOriginalFilename()).get();
        List<String> allowedExtensions = Arrays.stream(applicationProperties.getGtfsPathways().getUploadAllowedExtensions().split(",")).toList();

        if (!allowedExtensions.contains(fileExtension)) {
            throw new FileExtensionNotAllowedException("Uploaded file extension not supported. Allowed extensions are " + applicationProperties.getGtfsPathways().getUploadAllowedExtensions());
        }
        String OriginalFileName = file.getName();
        if (file.getOriginalFilename().lastIndexOf(".") != -1) {
            OriginalFileName = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));
        }
        String fileName = String.join(".", OriginalFileName + "_" + tdeiUniqueRecordId, fileExtension);
        String year = String.valueOf(LocalDateTime.now().getYear());
        String month = String.valueOf(LocalDateTime.now().getMonth());

        //Pattern: Year/Month/orgId/filename_uuid.extension
        //ex. 2022/11/101/testfile_295d783c624c4f86a7f09b116d55dfd0.zip
        String uploadPath = year + "/" + month + "/" + tdeiOrgId + "/" + fileName;

        String fileUploadedPath = storageService.uploadBlob(file, uploadPath, applicationProperties.getGtfsPathways().getContainerName());
        //Send message to the Queue
        GtfsPathwaysUploadMessage gtfsPathwaysMessge = GtfsPathwaysUploadMapper.INSTANCE.fromGtfsPathwaysUpload(meta);
        gtfsPathwaysMessge.setFileUploadPath(fileUploadedPath);
        gtfsPathwaysMessge.setUserId(userId);
        gtfsPathwaysMessge.setTdeiRecordId(tdeiUniqueRecordId);

        QueueMessage message = new QueueMessage();
        message.setMessageType("gtfspathways");
        message.setMessage("New Data published for the Organization:" + tdeiOrgId);
        message.setData(gtfsPathwaysMessge);
        eventBusService.sendMessage(message, applicationProperties.getGtfsPathways().getUploadTopicName());
        return tdeiUniqueRecordId;

    }
}
