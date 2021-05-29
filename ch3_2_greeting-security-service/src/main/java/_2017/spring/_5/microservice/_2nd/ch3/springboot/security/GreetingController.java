package _2017.spring._5.microservice._2nd.ch3.springboot.security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
  @GetMapping("/hello")
  public Greet hello() {
    return new Greet("Hello World!");
  }
}
