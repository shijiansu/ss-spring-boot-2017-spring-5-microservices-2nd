package com.brownfield.pss.checkin.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(CheckInSource.class)
public class Sender {

  public Sender() {}

  @Output(CheckInSource.CHECKINQ)
  @Autowired
  private MessageChannel messageChannel;

  public void send(Object message) {
    messageChannel.send(MessageBuilder.withPayload(message).build());
  }
}

interface CheckInSource {
  String CHECKINQ = "checkInQ";

  @Output("checkInQ")
  MessageChannel checkInQ();
}
