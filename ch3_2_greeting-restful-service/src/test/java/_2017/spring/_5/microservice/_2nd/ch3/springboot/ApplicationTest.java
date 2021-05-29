package _2017.spring._5.microservice._2nd.ch3.springboot;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApplicationTest {

  @Autowired private TestRestTemplate restTemplate;

  @Test
  public void testSpringBootApp() throws JsonProcessingException, IOException {
    String body = restTemplate.getForObject("/hello", String.class);
    assertThat(new ObjectMapper().readTree(body).get("message").textValue())
        .isEqualTo("Hello World!");
  }
}
