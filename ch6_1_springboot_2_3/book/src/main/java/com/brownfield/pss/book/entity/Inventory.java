package com.brownfield.pss.book.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Inventory {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  long id;

  String flightNumber;
  String flightDate;
  int available;

  public Inventory(String flightNumber, String flightDate, int available) {
    this.flightNumber = flightNumber;
    this.flightDate = flightDate;
    this.available = available;
  }

  public Inventory() {}

  public boolean isAvailable(int count) {
    return ((available - count) > 5);
  }

  public int getBookableInventory() {
    return available - 5;
  }
}
