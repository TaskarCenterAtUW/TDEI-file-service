package com.tdei.filesvc.gtfsflex.service;

import com.tdei.filesvc.common.model.QueueMessage;
import com.tdei.filesvc.common.service.EventBusService;
import com.tdei.filesvc.common.service.StorageService;
import com.tdei.filesvc.core.config.ApplicationProperties;
import com.tdei.filesvc.core.config.exception.handler.exceptions.FileExtensionNotAllowedException;
import com.tdei.filesvc.gtfsflex.mapper.GtfsFlexUploadMapper;
import com.tdei.filesvc.gtfsflex.model.GtfsFlexUpload;
import com.tdei.filesvc.gtfsflex.model.GtfsFlexUploadMessage;
import com.tdei.filesvc.gtfsflex.service.contract.IGtfsFlexStorageService;
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
public class GtfsFlexStorageService implements IGtfsFlexStorageService {
    private final StorageService storageService;
    private final EventBusService eventBusService;
    private final ApplicationProperties applicationProperties;

    @Override
    public String uploadBlob(GtfsFlexUpload meta, String tdeiOrgId, String userId, MultipartFile file) throws FileUploadException {
        String fileExtension = getExtensionByStringHandling(file.getOriginalFilename()).get();
        List<String> allowedExtensions = Arrays.stream(applicationProperties.getGtfsFlex().getUploadAllowedExtensions().split(",")).toList();

        if (!allowedExtensions.contains(fileExtension)) {
            throw new FileExtensionNotAllowedException("Uploaded file extension not supported. Allowed extensions are " + applicationProperties.getGtfsFlex().getUploadAllowedExtensions());
        }

        String fileName = String.join(".", file.getName() + "_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().replace("-", ""), fileExtension);
        String year = String.valueOf(LocalDateTime.now().getYear());
        String month = String.valueOf(LocalDateTime.now().getMonth());

        //Pattern: Year/Month/AgencyId/filename.extension
        //ex. 2022/11/101/testfile_1668063783868_295d783c624c4f86a7f09b116d55dfd0.zip
        String uploadPath = year + "/" + month + "/" + tdeiOrgId + "/" + fileName;
        String fileUploadedPath = storageService.uploadBlob(file, uploadPath, applicationProperties.getGtfsFlex().getGtfsFlexContainerName());

        //Send message to the Queue
        GtfsFlexUploadMessage gtfsFlexMessge = GtfsFlexUploadMapper.INSTANCE.fromGtfsFlexUpload(meta);
        gtfsFlexMessge.setFileUploadPath(fileUploadedPath);
        gtfsFlexMessge.setUserId(userId);

        QueueMessage message = new QueueMessage();
        message.setMessageType("gtfsflex");
        message.setMessage("New Data published for the Organization:" + tdeiOrgId);
        message.setData(gtfsFlexMessge);
        eventBusService.sendMessage(message, applicationProperties.getGtfsFlex().getUploadTopicName());
        return "Received the file, request is under process.";

    }
}
