package com.brownfield.pss.book.controller;

import com.brownfield.pss.book.component.BookingComponent;
import com.brownfield.pss.book.entity.BookingRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/booking")
@Slf4j
public class BookingController {
  BookingComponent bookingComponent;

  @Autowired
  BookingController(BookingComponent bookingComponent) {
    this.bookingComponent = bookingComponent;
  }

  @RequestMapping(value = "/create", method = RequestMethod.POST)
  long book(@RequestBody BookingRecord record) {
    log.info("Booking Request {}", record);
    return bookingComponent.book(record);
  }

  @RequestMapping("/get/{id}")
  BookingRecord getBooking(@PathVariable long id) {
    return bookingComponent.getBooking(id);
  }
}
