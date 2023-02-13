package com.tdei.filesvc.gtfspathways.service;

import com.tdei.filesvc.common.model.QueueMessage;
import com.tdei.filesvc.common.service.EventBusService;
import com.tdei.filesvc.common.service.StorageService;
import com.tdei.filesvc.core.config.ApplicationProperties;
import com.tdei.filesvc.core.config.exception.handler.exceptions.FileExtensionNotAllowedException;
import com.tdei.filesvc.gtfspathways.model.GtfsPathwaysUpload;
import com.tdei.filesvc.gtfspathways.model.ResponseInfo;
import com.tdei.filesvc.gtfspathways.model.UploadQueueMessage;
import com.tdei.filesvc.gtfspathways.service.contract.IGtfsPathwaysStorageService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

import static com.tdei.filesvc.common.utils.Utility.getExtensionByStringHandling;

@Service
@RequiredArgsConstructor
public class GtfsPathwaysStorageService implements IGtfsPathwaysStorageService {
    private final StorageService storageService;
    private final EventBusService eventBusService;
    private final ApplicationProperties applicationProperties;

    @Override
    public String uploadBlob(GtfsPathwaysUpload uploadInputInfo, String tdeiOrgId, String userId, MultipartFile file) throws FileUploadException {
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
        UploadQueueMessage messageData = new UploadQueueMessage();
        messageData.setRequest(uploadInputInfo);
        messageData.setUserId(userId);
        messageData.setOrgId(tdeiOrgId);
        messageData.setTdeiRecordId(tdeiUniqueRecordId);
        //Set meta information
        Map<String, String> metaInfo = new HashMap<>();
        metaInfo.put("file_upload_path", fileUploadedPath);
        messageData.setMeta(metaInfo);

        messageData.setResponse(new ResponseInfo(
                true,
                "File uploaded for the Organization : " + tdeiOrgId + " with tdei record id : " + tdeiUniqueRecordId
        ));

        QueueMessage message = new QueueMessage();
        message.setMessageType("gtfs-pathways-upload");
        message.setData(messageData);

        eventBusService.sendMessage(message, applicationProperties.getGtfsPathways().getUploadTopicName());
        return tdeiUniqueRecordId;

    }
}
