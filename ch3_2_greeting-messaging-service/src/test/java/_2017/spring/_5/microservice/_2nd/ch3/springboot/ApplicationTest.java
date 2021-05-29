package _2017.spring._5.microservice._2nd.ch3.springboot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import({QpidProperties.class, EmbeddedAmqpBroker.class})
public class ApplicationTest {

  @Test
  public void contextLoads() {}
}
