package com.brownfield.pss.book.component;

import com.brownfield.pss.book.entity.BookingRecord;
import com.brownfield.pss.book.entity.Inventory;
import com.brownfield.pss.book.entity.Passenger;
import com.brownfield.pss.book.repository.BookingRepository;
import com.brownfield.pss.book.repository.InventoryRepository;
import com.brownfield.pss.book.service_client.FareServiceProxy;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BookingComponent {

  BookingRepository bookingRepository;
  InventoryRepository inventoryRepository;
  // feign
  FareServiceProxy fareServiceProxy;
  // RestTemplate restTemplate;
  Sender sender;

  public BookingComponent() {}

  @Autowired
  public BookingComponent(
      BookingRepository bookingRepository,
      Sender sender,
      InventoryRepository inventoryRepository,
      FareServiceProxy fareServiceProxy) {
    this.bookingRepository = bookingRepository;
    // this.restTemplate = new RestTemplate();
    this.sender = sender;
    this.inventoryRepository = inventoryRepository;
    this.fareServiceProxy = fareServiceProxy;
  }

  public long book(BookingRecord record) {
    log.info("Calling fares to get fare");
    // call fares to get fare
    validateFare(record);

    Fare fare = fareServiceProxy.getFare(record.getFlightNumber(), record.getFlightDate());

    log.info("Calling fares to get fare " + fare);
    // check fare
    if (!record.getFare().equals(fare.getFare())) throw new BookingException("fare is tampered");
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
    return bookingRepository.findOne(id);
  }

  public void updateStatus(String status, long bookingId) {
    BookingRecord b = bookingRepository.findOne(bookingId);
    if (b != null) {
      b.setStatus(status);
    } else {
      log.info("Booking does not exist {}", bookingId);
    }
  }

  public void validateFare(BookingRecord record) {
    //	Fare fare = restTemplate.getForObject(fareServiceUrl+FareURL
    // +"/get?flightNumber="+record.getFlightNumber()+"&flightDate="+record.getFlightDate(),Fare.class);
    Fare fare = fareServiceProxy.getFare(record.getFlightNumber(), record.getFlightDate());

    // check fare
    checkFare(record.getFare(), fare.getFare());
  }

  private void checkFare(String requestedFare, String actualFare) {
    log.info("Calling fares to get fare {}", actualFare);
    if (!requestedFare.equals(actualFare)) throw new BookingException("Fare is tampered");
  }
}
