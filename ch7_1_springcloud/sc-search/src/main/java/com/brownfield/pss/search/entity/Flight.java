package com.brownfield.pss.search.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Flight {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  long id;

  String flightNumber;
  String origin;
  String destination;
  String flightDate;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "fare_Id")
  Fares fares;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "inv_Id")
  Inventory inventory;

  public Flight() {}

  public Flight(
      String flightNumber,
      String origin,
      String destination,
      String flightDate,
      Fares fares,
      Inventory inventory) {
    this.flightNumber = flightNumber;
    this.origin = origin;
    this.destination = destination;
    this.flightDate = flightDate;
    this.fares = fares;
    this.inventory = inventory;
  }
}
