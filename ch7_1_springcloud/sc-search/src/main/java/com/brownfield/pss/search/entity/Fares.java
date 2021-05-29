package com.brownfield.pss.search.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Fares {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "fare_id")
  long id;

  String fare;
  String currency;

  public Fares() {}

  public Fares(String fare, String currency) {
    this.fare = fare;
    this.currency = currency;
  }
}
