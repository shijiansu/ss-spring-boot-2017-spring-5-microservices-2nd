package com.brownfield.pss.fares.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Fare {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  long id;

  String flightNumber;
  String flightDate;
  String fare;

  public Fare() {}

  public Fare(String flightNumber, String flightDate, String fare) {
    this.flightNumber = flightNumber;
    this.flightDate = flightDate;
    this.fare = fare;
  }
}
