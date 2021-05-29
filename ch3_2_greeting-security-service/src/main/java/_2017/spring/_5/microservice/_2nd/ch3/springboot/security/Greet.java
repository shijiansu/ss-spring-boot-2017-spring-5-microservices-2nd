package _2017.spring._5.microservice._2nd.ch3.springboot.security;

public class Greet {
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
