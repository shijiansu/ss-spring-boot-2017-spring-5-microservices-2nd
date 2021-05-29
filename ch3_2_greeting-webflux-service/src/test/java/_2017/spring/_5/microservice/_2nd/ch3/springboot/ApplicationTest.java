package _2017.spring._5.microservice._2nd.ch3.springboot;

import _2017.spring._5.microservice._2nd.ch3.springboot.webflux.Greet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class ApplicationTest {
  WebTestClient webClient;

  @BeforeEach
  public void setup() {
    webClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
  }

  @Test
  public void testWebFluxEndpoint() throws Exception {
    webClient
        .get()
        .uri("/hello")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(Greet.class)
        .returnResult()
        .getResponseBody()
        .getMessage()
        .equals("Hello World!");
  }
}
