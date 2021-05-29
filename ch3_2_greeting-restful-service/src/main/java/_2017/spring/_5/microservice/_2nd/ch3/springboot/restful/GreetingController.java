package _2017.spring._5.microservice._2nd.ch3.springboot.restful;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

  // @CrossOrigin("http://mytrustedorigin.com")
  @CrossOrigin // accept all
  @GetMapping("/hello")
  public Greet hello() {
    return new Greet("Hello World!");
  }

  @RequestMapping("/greeting")
  @ResponseBody
  public HttpEntity<Greet> greetingWithHateoas(
      @RequestParam(value = "name", required = false, defaultValue = "HATEOAS") String name) {
    Greet greet = new Greet("Hello " + name); // extends RepresentationModel
    greet.add(linkTo(methodOn(GreetingController.class).greetingWithHateoas(name)).withSelfRel());
    return new ResponseEntity<>(greet, HttpStatus.OK);
  }
}
