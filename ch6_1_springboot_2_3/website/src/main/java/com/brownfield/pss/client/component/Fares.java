package com.brownfield.pss.client.component;

import lombok.Data;

@Data
public class Fares {
  long id;

  String fare;
  String currency;

  public Fares() {}

  public Fares(String fare, String currency) {
    this.fare = fare;
    this.currency = currency;
  }
}
