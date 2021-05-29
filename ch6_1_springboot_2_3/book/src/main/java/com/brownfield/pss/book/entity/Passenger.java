package com.brownfield.pss.book.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString.Exclude;

@Entity
@Data
public class Passenger {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  long id;

  String firstName;
  String lastName;
  String gender;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "BOOKING_ID")
  @JsonIgnore
  @Exclude // to avoid infinity looping
  @EqualsAndHashCode.Exclude // to avoid infinity looping
  private BookingRecord bookingRecord;

  public Passenger() {}

  public Passenger(String firstName, String lastName, String gender, BookingRecord bookingRecord) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.gender = gender;
    this.bookingRecord = bookingRecord;
  }
}
