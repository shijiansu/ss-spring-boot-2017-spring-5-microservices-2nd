package com.brownfield.pss.book;

import com.brownfield.pss.book.component.BookingComponent;
import com.brownfield.pss.book.entity.BookingRecord;
import com.brownfield.pss.book.entity.Inventory;
import com.brownfield.pss.book.entity.Passenger;
import com.brownfield.pss.book.repository.InventoryRepository;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
// Abstract enabling to all kinds of client, including Eureka
@EnableDiscoveryClient
@EnableFeignClients
@EnableSwagger2
@Slf4j
public class Application implements CommandLineRunner {
  @Autowired BookingComponent bookingComponent;

  @Autowired InventoryRepository inventoryRepository;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Override
  public void run(String... strings) {
    Inventory[] invs = {
      new Inventory("BF100", "22-JAN-18", 100),
      new Inventory("BF101", "22-JAN-18", 100),
      new Inventory("BF102", "22-JAN-18", 100),
      new Inventory("BF103", "22-JAN-18", 100),
      new Inventory("BF104", "22-JAN-18", 100),
      new Inventory("BF105", "22-JAN-18", 100),
      new Inventory("BF106", "22-JAN-18", 100)
    };
    Arrays.asList(invs).forEach(inventory -> inventoryRepository.save(inventory));

    BookingRecord booking =
        new BookingRecord("BF101", "NYC", "SFO", "22-JAN-18", new Date(), "101");
    Set<Passenger> passengers = new HashSet<>();
    passengers.add(new Passenger("Gean", "Franc", "Male", booking));

    booking.setPassengers(passengers);
    long record = bookingComponent.book(booking);
    log.info("Booking successfully saved... {}", record);

    log.info("Looking to load booking record...");
    log.info("Result: {}", bookingComponent.getBooking(record));
  }
}
