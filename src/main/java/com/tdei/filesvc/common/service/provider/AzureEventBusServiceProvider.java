package com.tdei.filesvc.common.service.provider;

import com.azure.core.util.BinaryData;
import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.tdei.filesvc.common.model.QueueMessage;
import com.tdei.filesvc.common.service.common.contract.IEventBusService;
import com.tdei.filesvc.core.config.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AzureEventBusServiceProvider implements IEventBusService {

    private final ApplicationProperties applicationProperties;

    private ServiceBusSenderClient getConnection(String topic) {
        return new ServiceBusClientBuilder()
                .connectionString(applicationProperties.getCloud().getAzure().getServiceBus().getConnectionString())
                .sender()
                .topicName(topic)
                .buildClient();
    }

    @Override
    public void sendMessage(QueueMessage message, String topic) {

        ServiceBusSenderClient serviceBusSenderClient = getConnection(topic);
        var serviceMsg = new ServiceBusMessage(BinaryData.fromObject(message));
        serviceMsg.setMessageId(UUID.randomUUID().toString());
        serviceMsg.setSubject(message.getMessageType());
        serviceBusSenderClient.sendMessage(serviceMsg);
        serviceBusSenderClient.close();
    }
}
