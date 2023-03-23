package com.tdei.filesvc;

import com.tdei.filesvc.common.model.QueueMessage;
import com.tdei.filesvc.common.service.EventBusService;
import com.tdei.filesvc.common.service.StorageService;
import com.tdei.filesvc.core.config.ApplicationProperties;
import com.tdei.filesvc.gtfsflex.controller.GtfsFlexFileController;
import com.tdei.filesvc.gtfsflex.model.GtfsFlexUpload;
import com.tdei.filesvc.gtfsflex.service.GtfsFlexStorageService;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;

import java.security.Principal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GtfsFlexTests {

    @Mock
    private GtfsFlexStorageService gtfsFlexStorageService;
    @Mock
    private EventBusService eventBusService;

    @Mock
    private ApplicationProperties applicationProperties;

    @InjectMocks
    private GtfsFlexStorageService gtfsFlexStorageServiceInjectMock;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private GtfsFlexFileController gtfsFlexFileController;

    @Test
    void uploadFlexFileController() throws FileUploadException {

        Principal mockPrincipal = mock(Principal.class);
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        MockHttpServletRequest request = new MockHttpServletRequest();

        String orgId = "101";
        when(gtfsFlexStorageService.uploadBlob(any(GtfsFlexUpload.class), anyString(), anyString(), any(MockMultipartFile.class))).thenReturn("success");
        var result = gtfsFlexFileController.uploadGtfsFlexFile(new GtfsFlexUpload(), orgId, "2039-2829", file, request);

        assertThat(result.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getBody()).isEqualTo("success");
    }

    @Test
    void uploadFlexServiceTest() throws FileUploadException {

        Principal mockPrincipal = mock(Principal.class);
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.zip",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "Hello, World!".getBytes()
        );
        MockHttpServletRequest request = new MockHttpServletRequest();
        ApplicationProperties props = new ApplicationProperties();
        var gtfsFlexProperties = new ApplicationProperties.GtfsFlexProperties();
        gtfsFlexProperties.setUploadAllowedExtensions("zip");
        gtfsFlexProperties.setContainerName("gtfs-Flex");
        gtfsFlexProperties.setUploadTopicName("uploaded");
        props.setGtfsFlex(gtfsFlexProperties);

        String orgId = "101";
        GtfsFlexUpload upload = new GtfsFlexUpload();
        upload.setCollectedBy("morethan50chars");
        boolean isMetaValid = upload.isMetadataValidated();
        assertThat(isMetaValid).isTrue();
        when(storageService.uploadBlob(any(MockMultipartFile.class), anyString(), anyString())).thenReturn("success");
        when(applicationProperties.getGtfsFlex()).thenReturn(props.getGtfsFlex());
        doNothing().when(eventBusService).sendMessage(any(QueueMessage.class), anyString());
        var result = gtfsFlexStorageServiceInjectMock.uploadBlob(upload, orgId, "2039-2829", file);

        assertThat(result).isNotBlank();
    }
}
