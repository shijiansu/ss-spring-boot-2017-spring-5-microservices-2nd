package _2017.spring._5.microservice._2nd.ch3.springboot.messaging;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Sender {
  @Autowired RabbitMessagingTemplate template;

  @Bean
  Queue queue() {
    return new Queue("TestQ", false);
  }

  public void send(String message) {
    template.convertAndSend("TestQ", message);
  }
}
