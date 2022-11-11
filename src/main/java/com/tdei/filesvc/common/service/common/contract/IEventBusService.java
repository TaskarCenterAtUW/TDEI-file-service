package com.tdei.filesvc.common.service.common.contract;

import com.tdei.filesvc.common.model.QueueMessage;

public interface IEventBusService {

    void sendMessage(QueueMessage message, String topic);

}
