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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@Slf4j
public class Application implements CommandLineRunner {
  @Value("${microservice.book.hostname:localhost}")
  String bookHostname;

  @Value("${microservice.search.hostname:localhost}")
  String searchHostname;

  @Value("${microservice.checkin.hostname:localhost}")
  String checkinHostname;

  RestTemplate searchClient = new RestTemplate();

  RestTemplate bookingClient = new RestTemplate();

  RestTemplate checkInClient = new RestTemplate();

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Override
  public void run(String... strings) {
    // Search for a flight
    SearchQuery searchQuery = new SearchQuery("NYC", "SFO", "22-JAN-18");
    Flight[] flights =
        searchClient.postForObject(
            "http://" + searchHostname + ":8083/search/get", searchQuery, Flight[].class);

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
          bookingClient.postForObject(
              "http://" + bookHostname + ":8080/booking/create", booking, long.class);
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
          checkInClient.postForObject(
              "http://" + checkinHostname + ":8081/checkin/create", checkIn, long.class);
      log.info("Checked IN {}", checkinId);
    } catch (Exception e) {
      log.error("CHECK IN SERVICE NOT AVAILABLE...!!!");
    }
  }
}
