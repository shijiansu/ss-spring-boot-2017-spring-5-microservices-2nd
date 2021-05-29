package _2017.spring._5.microservice._2nd.ch3.springboot.restful;

import org.springframework.hateoas.RepresentationModel;

// RepresentationModel uses for HATOAS
public class Greet extends RepresentationModel {
  private String message;

  public Greet() {}

  public Greet(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
