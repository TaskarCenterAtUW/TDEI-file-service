package com.tdei.filesvc.core.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties
@Component
@Data
public class ApplicationProperties {
    private SwaggerProperties swagger;
    private CloudProperties cloud;
    private AppProperties application;
    private GtfsFlexProperties gtfsFlex;
    private GtfsPathwaysProperties gtfsPathways;
    private OswProperties osw;

    @Data
    @NoArgsConstructor
    public static class GtfsFlexProperties {
        private String uploadAllowedExtensions;
        private String gtfsFlexContainerName;
    }

    @Data
    @NoArgsConstructor
    public static class GtfsPathwaysProperties {
        private String uploadAllowedExtensions;
        private String gtfsPathwaysContainerName;
    }

    @Data
    @NoArgsConstructor
    public static class OswProperties {
        private String uploadAllowedExtensions;
        private String oswContainerName;
    }

    @Data
    @NoArgsConstructor
    public static class AppProperties {
        private String appName;
    }

    @Data
    @NoArgsConstructor
    public static class SwaggerProperties {
        private SwaggerContact contact;
        private String title;
        private String description;
        private String version;

        @Data
        @NoArgsConstructor
        public static class SwaggerContact {
            private String name = "";
            private String email = "";
            private String url = "";
        }
    }


    @Data
    @NoArgsConstructor
    public static class CloudProperties {
        private Azure azure;

        @Data
        @NoArgsConstructor
        public static class Azure {
            private Storage storage;

            @Data
            @NoArgsConstructor
            public static class Storage {
                private BlobProperty blob;

                @Data
                @NoArgsConstructor
                public static class BlobProperty {
                    private String accountName;
                    private String accountKey;
                    private String endpoint;
                    private String connectionString;
                }
            }
        }
    }
}

