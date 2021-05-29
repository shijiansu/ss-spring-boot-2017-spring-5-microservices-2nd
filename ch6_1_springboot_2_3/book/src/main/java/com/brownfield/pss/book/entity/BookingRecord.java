package com.brownfield.pss.book.entity;

import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class BookingRecord {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  long id;

  private String flightNumber;
  private String origin;
  private String destination;
  private String flightDate;
  private Date bookingDate;
  private String fare;
  private String status;

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "bookingRecord")
  Set<Passenger> passengers;

  public BookingRecord() {}

  public BookingRecord(
      String flightNumber,
      String from,
      String to,
      String flightDate,
      Date bookingDate,
      String fare) {
    this.flightNumber = flightNumber;
    this.origin = from;
    this.destination = to;
    this.flightDate = flightDate;
    this.bookingDate = bookingDate;
    this.fare = fare;
  }
}
