package _2017.spring._5.microservice._2nd.ch3.springboot;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import _2017.spring._5.microservice._2nd.ch3.springboot.security.Greet;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class ApplicationTest {

  @Test
  public void testSpringBootApp() throws JsonProcessingException, IOException {
    RestTemplate restTemplate = new RestTemplate();
    Greet body = restTemplate.getForObject("http://localhost:8080", Greet.class);
    assertThat("Hello World!".equals(body.getMessage()));
  }
}