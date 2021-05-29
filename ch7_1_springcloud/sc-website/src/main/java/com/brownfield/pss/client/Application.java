package com.brownfield.pss.client;

import com.brownfield.pss.client.component.BookingRecord;
import com.brownfield.pss.client.component.CheckInRecord;
import com.brownfield.pss.client.component.Flight;
import com.brownfield.pss.client.component.Passenger;
import com.brownfield.pss.client.component.SearchQuery;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient // using Eureka
@Slf4j
public class Application implements CommandLineRunner {
  // using Eureka and Ribbon
  // use restTemplate.setInterceptors to add LoadBalancerInterceptor
  @LoadBalanced
  @Bean
  RestTemplate bookClient() {
    return new RestTemplate();
  }

  @LoadBalanced
  @Bean
  RestTemplate searchClient() {
    return new RestTemplate();
  }

  @LoadBalanced
  @Bean
  RestTemplate checkinClient() {
    return new RestTemplate();
  }

  @Autowired RestTemplate bookClient;
  @Autowired RestTemplate searchClient;
  @Autowired RestTemplate checkinClient;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Override
  public void run(String... strings) {
    // Search for a flight
    SearchQuery searchQuery = new SearchQuery("NYC", "SFO", "22-JAN-18");
    Flight[] flights =
        searchClient.postForObject(
            "http://search-apigateway/api/search/get", searchQuery, Flight[].class);

    Arrays.asList(flights).forEach(flight -> log.info(" flight >" + flight));

    // create a booking only if there are flights.
    if (flights.length == 0) {
      return;
    }
    Flight flight = flights[0];
    BookingRecord booking =
        new BookingRecord(
            flight.getFlightNumber(),
            flight.getOrigin(),
            flight.getDestination(),
            flight.getFlightDate(),
            null,
            flight.getFares().getFare());
    Set<Passenger> passengers = new HashSet<>();
    passengers.add(new Passenger("Gavin", "Franc", "Male", booking));
    booking.setPassengers(passengers);
    long bookingId = 0;
    try {
      bookingId =
          bookClient.postForObject(
              "http://book-apigateway/api/booking/create", booking, long.class);
      log.info("Booking created {}", bookingId);
    } catch (Exception e) {
      log.error("BOOKING SERVICE NOT AVAILABLE...!!!");
    }

    // check in passenger
    if (bookingId == 0) return;
    try {
      CheckInRecord checkIn =
          new CheckInRecord("Franc", "Gavin", "28C", null, "BF101", "22-JAN-18", bookingId);
      long checkinId =
          checkinClient.postForObject(
              "http://checkin-apigateway/api/checkin/create", checkIn, long.class);
      log.info("Checked IN {}", checkinId);
    } catch (Exception e) {
      log.error("CHECK IN SERVICE NOT AVAILABLE...!!!");
    }
  }
}
