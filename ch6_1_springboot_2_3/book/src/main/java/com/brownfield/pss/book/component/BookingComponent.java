package com.brownfield.pss.book.component;

import com.brownfield.pss.book.entity.BookingRecord;
import com.brownfield.pss.book.entity.Inventory;
import com.brownfield.pss.book.entity.Passenger;
import com.brownfield.pss.book.repository.BookingRepository;
import com.brownfield.pss.book.repository.InventoryRepository;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class BookingComponent {
  @Value("${microservice.fare.hostname:localhost}")
  String faresHostname;

  BookingRepository bookingRepository;
  InventoryRepository inventoryRepository;

  // RestTemplate restTemplate;
  WebClient webClient;

  Sender sender;

  @Autowired
  public BookingComponent(
      BookingRepository bookingRepository, Sender sender, InventoryRepository inventoryRepository) {
    this.bookingRepository = bookingRepository;
    // this.restTemplate = new RestTemplate();
    this.sender = sender;
    this.inventoryRepository = inventoryRepository;
  }

  @PostConstruct
  public void init(){
    // faresHostname cannot be loaded before the this class constructor
    this.webClient = WebClient.create("http://" + faresHostname + ":8082");
  }

  public long book(BookingRecord record) {
    log.info("Calling fares to get fare");
    // call fares to get fare
    validateFareReactively(record);

    // check fare
    log.info("Calling inventory to get inventory");

    // check inventory
    Inventory inventory =
        inventoryRepository.findByFlightNumberAndFlightDate(
            record.getFlightNumber(), record.getFlightDate());
    if (!inventory.isAvailable(record.getPassengers().size())) {
      throw new BookingException("No more seats available");
    }
    log.info("Successfully checked inventory {}", inventory);
    log.info("Calling inventory to update inventory");

    // update inventory
    inventory.setAvailable(inventory.getAvailable() - record.getPassengers().size());
    inventoryRepository.saveAndFlush(inventory);
    log.info("Successfully updated inventory");

    // save booking
    record.setStatus(BookingStatus.BOOKING_CONFIRMED);
    Set<Passenger> passengers = record.getPassengers();
    passengers.forEach(passenger -> passenger.setBookingRecord(record));
    record.setBookingDate(new Date());
    long id = bookingRepository.save(record).getId();
    log.info("Successfully saved booking {}", id);

    // send a message to search to update inventory
    log.info("Sending a booking event");
    Map<String, Object> bookingDetails = new HashMap<>();
    bookingDetails.put("FLIGHT_NUMBER", record.getFlightNumber());
    bookingDetails.put("FLIGHT_DATE", record.getFlightDate());
    bookingDetails.put("NEW_INVENTORY", inventory.getBookableInventory());

    sender.send(bookingDetails);

    log.info("Booking event successfully delivered {}", bookingDetails);
    return id;
  }

  public BookingRecord getBooking(long id) {
    return bookingRepository.findById(id).orElse(null);
  }

  public void updateStatus(String status, long bookingId) {
    bookingRepository
        .findById(bookingId)
        .ifPresentOrElse(
            b -> b.setStatus(status), () -> log.info("Booking does not exist {}", bookingId));
  }

  public void validateFareReactively(BookingRecord record) {
    // Fare fare = restTemplate.getForObject(FareURL
    // +"/get?flightNumber="+record.getFlightNumber()+"&flightDate="+record.getFlightDate(),Fare.class);
    Mono<Fare> result =
        webClient
            .get()
            .uri(
                "/fares/get?flightNumber="
                    + record.getFlightNumber()
                    + "&flightDate="
                    + record.getFlightDate())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .flatMap(response -> response.bodyToMono(Fare.class));
    // It is not to give back Mono, so subscribe here for the further process
    result.subscribe(fare -> checkFare(record.getFare(), fare.getFare()));
  }

  private void checkFare(String requestedFare, String actualFare) {
    log.info("Calling fares to get fare (reactively collected) {}", actualFare);
    if (!requestedFare.equals(actualFare)) throw new BookingException("Fare is tampered");
  }
}
