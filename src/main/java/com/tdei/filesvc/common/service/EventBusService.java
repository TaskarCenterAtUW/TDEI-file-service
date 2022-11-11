package com.tdei.filesvc.common.service;

import com.tdei.filesvc.common.model.QueueMessage;
import com.tdei.filesvc.common.service.common.contract.IEventBusService;
import com.tdei.filesvc.common.service.provider.AzureEventBusServiceProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventBusService implements IEventBusService {
    private final AzureEventBusServiceProvider azureEventBusServiceProvider;

    @Override
    public void sendMessage(QueueMessage message, String topic) {
        azureEventBusServiceProvider.sendMessage(message, topic);
    }
}
