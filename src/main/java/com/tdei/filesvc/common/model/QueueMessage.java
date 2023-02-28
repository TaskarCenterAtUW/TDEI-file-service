package com.tdei.filesvc.common.model;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class QueueMessage {
    /**
     * Unique message id
     */
    private String messageId = UUID.randomUUID().toString();
    /**
     * Message type for this queue message
     */
    private String messageType;

    /**
     * Published Date for the queue message.
     * Defaults to local time if not specified.
     */
    private String publishedDate = Instant.now().toString();

    /**
     * Optional message string for the message
     */
    private String message = "";

    /**
     * Additional data that is related to the message
     */
    private Object data;
}
