package com.brownfield.pss.book.controller;

import com.brownfield.pss.book.component.BookingComponent;
import com.brownfield.pss.book.component.BookingStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@EnableBinding(BookingSink.class)
@Component
@Slf4j
public class Receiver {

  BookingComponent bookingComponent;

  @Autowired
  public Receiver(BookingComponent bookingComponent) {
    this.bookingComponent = bookingComponent;
  }

  public Receiver() {}

  @ServiceActivator(inputChannel = BookingSink.CHECKINQ)
  public void accept(long bookingID) {
    bookingComponent.updateStatus(BookingStatus.CHECKED_IN, bookingID);
  }
}

interface BookingSink {
  String CHECKINQ = "checkInQ";

  @Input(CHECKINQ)
  MessageChannel checkInQ();
}
