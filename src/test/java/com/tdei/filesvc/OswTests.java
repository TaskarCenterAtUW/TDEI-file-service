package com.tdei.filesvc;

import com.tdei.filesvc.common.model.QueueMessage;
import com.tdei.filesvc.common.service.EventBusService;
import com.tdei.filesvc.common.service.StorageService;
import com.tdei.filesvc.core.config.ApplicationProperties;
import com.tdei.filesvc.osw.controller.OswFileController;
import com.tdei.filesvc.osw.model.OswUpload;
import com.tdei.filesvc.osw.service.OswStorageService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OswTests {

    @Mock
    private OswStorageService oswStorageServiceMock;
    @Mock
    private EventBusService eventBusService;

    @Mock
    private ApplicationProperties applicationProperties;

    @InjectMocks
    private OswStorageService oswStorageServiceInjectMock;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private OswFileController oswFileController;

    @Test
    void uploadPathwaysFileController() throws FileUploadException {

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
        when(oswStorageServiceMock.uploadBlob(any(OswUpload.class), anyString(), anyString(), any(MockMultipartFile.class))).thenReturn("success");
        var result = oswFileController.uploadOswFile(new OswUpload(), orgId, "2039-2829", file, request);

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
        var oswProperties = new ApplicationProperties.OswProperties();
        oswProperties.setUploadAllowedExtensions("zip");
        oswProperties.setContainerName("osw");
        oswProperties.setUploadTopicName("uploaded");
        props.setOsw(oswProperties);

        String orgId = "101";
        when(storageService.uploadBlob(any(MockMultipartFile.class), anyString(), anyString())).thenReturn("success");
        when(applicationProperties.getOsw()).thenReturn(props.getOsw());
        doNothing().when(eventBusService).sendMessage(any(QueueMessage.class), anyString());
        var result = oswStorageServiceInjectMock.uploadBlob(new OswUpload(), orgId, "2039-2829", file);

        assertThat(result).isNotBlank();
    }
}
