package _2017.spring._5.microservice._2nd.ch3.springboot;

import _2017.spring._5.microservice._2nd.ch3.springboot.messaging.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
public class Application implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(Application.class);
    app.addListeners(new ApplicationPidFileWriter()); // pid file
    app.run(args);
  }

  @Autowired Sender sender;

  @Override
  public void run(String... args) throws Exception {
    sender.send("Hello Messaging..!!!");
  }
}
