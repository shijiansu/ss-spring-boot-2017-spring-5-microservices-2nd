package com.brownfield.pss.book.controller;

import com.brownfield.pss.book.component.BookingComponent;
import com.brownfield.pss.book.component.BookingStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Receiver {
  BookingComponent bookingComponent;

  @Autowired
  public Receiver(BookingComponent bookingComponent) {
    this.bookingComponent = bookingComponent;
  }

  @RabbitListener(queues = "CheckINQ")
  public void processMessage(long bookingID) {
    log.info("Booking ID {}", bookingID);
    bookingComponent.updateStatus(BookingStatus.CHECKED_IN, bookingID);
  }
}
