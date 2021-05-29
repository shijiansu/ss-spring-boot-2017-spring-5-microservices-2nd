package com.brownfield.pss.search.controller;

import lombok.Data;

@Data
public class SearchQuery {
  String origin;
  String destination;
  String flightDate;

  public SearchQuery() {}

  public SearchQuery(String origin, String destination, String flightDate) {
    this.origin = origin;
    this.destination = destination;
    this.flightDate = flightDate;
  }
}
