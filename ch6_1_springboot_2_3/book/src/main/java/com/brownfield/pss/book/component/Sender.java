package com.brownfield.pss.book.component;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Sender {
  RabbitMessagingTemplate template;

  @Autowired
  Sender(RabbitMessagingTemplate template) {
    this.template = template;
  }

  @Bean
  Queue searchQ() {
    return new Queue("SearchQ", false);
  }

  @Bean
  Queue checkInQ() {
    return new Queue("CheckINQ", false);
  }

  public void send(Object message) {
    template.convertAndSend("SearchQ", message);
  }
}
