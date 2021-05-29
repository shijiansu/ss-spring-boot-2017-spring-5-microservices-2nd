package _2017.spring._5.microservice._2nd.ch3.springboot.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {
  @RabbitListener(queues = "TestQ")
  public void processMessage(String content) {
    System.out.println(content);
  }
}
