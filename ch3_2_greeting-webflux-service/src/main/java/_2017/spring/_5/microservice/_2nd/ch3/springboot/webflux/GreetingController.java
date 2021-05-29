package _2017.spring._5.microservice._2nd.ch3.springboot.webflux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class GreetingController {
  //  @GetMapping("/hello")
  //  public Greet hello() {
  //    return new Greet("Hello World!");
  //  }

  // @RequestMapping("/")
  @GetMapping("/hello")
  Mono<Greet> hello() {
    return Mono.just(new Greet("Hello World!"));
  }
}
