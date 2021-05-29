package com.brownfield.pss.checkin;

import com.brownfield.pss.checkin.entity.CheckInRecord;
import com.brownfield.pss.checkin.repository.CheckinRepository;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableDiscoveryClient
@EnableSwagger2
@Slf4j
public class Application implements CommandLineRunner {
  @Autowired CheckinRepository repository;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Override
  public void run(String... strings) {
    CheckInRecord record =
        new CheckInRecord("Franc", "Gean", "28A", new Date(), "BF101", "22-JAN-18", 1);

    CheckInRecord result = repository.save(record);
    log.info("Checked in successfully ..." + result);

    log.info("Looking to load checkedIn record...");
    log.info("Result: " + repository.findOne(result.getId()));
  }
}
