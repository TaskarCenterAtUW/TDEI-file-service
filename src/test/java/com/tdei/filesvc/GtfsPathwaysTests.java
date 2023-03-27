package com.tdei.filesvc;

import com.tdei.filesvc.common.model.QueueMessage;
import com.tdei.filesvc.common.service.EventBusService;
import com.tdei.filesvc.common.service.StorageService;
import com.tdei.filesvc.core.config.ApplicationProperties;
import com.tdei.filesvc.gtfspathways.controller.GtfsPathwaysFileController;
import com.tdei.filesvc.gtfspathways.model.GtfsPathwaysUpload;
import com.tdei.filesvc.gtfspathways.service.GtfsPathwaysStorageService;
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
class GtfsPathwaysTests {

    @Mock
    private GtfsPathwaysStorageService gtfsPathwaysStorageService;
    @Mock
    private EventBusService eventBusService;

    @Mock
    private ApplicationProperties applicationProperties;

    @InjectMocks
    private GtfsPathwaysStorageService gtfsPathwaysStorageServiceInjectMock;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private GtfsPathwaysFileController gtfsPathwaysFileController;

    @Test
    void uploadPathwaysFileController() throws FileUploadException {

        Principal mockPrincipal = mock(Principal.class);
        GtfsPathwaysUpload mockGtfsPathwaysUpload = mock(GtfsPathwaysUpload.class);
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        MockHttpServletRequest request = new MockHttpServletRequest();

        String orgId = "101";
        when(gtfsPathwaysStorageService.uploadBlob(any(GtfsPathwaysUpload.class), anyString(), anyString(), any(MockMultipartFile.class))).thenReturn("success");
        var result = gtfsPathwaysFileController.uploadGtfsPathwaysFile(mockGtfsPathwaysUpload, orgId, "2039-2829", file, request);

        assertThat(result.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getBody()).isEqualTo("success");
    }

    @Test
    void uploadPathwaysServiceTest() throws FileUploadException {

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
        var gtfsPathwaysProperties = new ApplicationProperties.GtfsPathwaysProperties();
        gtfsPathwaysProperties.setUploadAllowedExtensions("zip");
        gtfsPathwaysProperties.setContainerName("gtfs-pathways");
        gtfsPathwaysProperties.setUploadTopicName("uploaded");
        props.setGtfsPathways(gtfsPathwaysProperties);

        String orgId = "101";
        when(storageService.uploadBlob(any(MockMultipartFile.class), anyString(), anyString())).thenReturn("success");
        when(applicationProperties.getGtfsPathways()).thenReturn(props.getGtfsPathways());
        doNothing().when(eventBusService).sendMessage(any(QueueMessage.class), anyString());
        var result = gtfsPathwaysStorageServiceInjectMock.uploadBlob(new GtfsPathwaysUpload(), orgId, "2039-2829", file);

        assertThat(result).isNotBlank();
    }
}
