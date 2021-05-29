package _2017.spring._5.microservice._2nd.ch3.springboot;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Configuration;

@Data
@TestConfiguration
public class QpidProperties {

  @Value("${spring.rabbitmq.port}")
  private String qpidPort;

  @Value("${spring.rabbitmq.username}")
  private String qpidUserName;

  @Value("${spring.rabbitmq.password}")
  private String qpidPassword;

  @Value("${spring.rabbitmq.virtual-host}")
  private String qpidVirtualHost;
}
