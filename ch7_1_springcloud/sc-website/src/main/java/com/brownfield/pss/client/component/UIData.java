package com.brownfield.pss.client.component;

import java.util.List;
import lombok.Data;

@Data
public class UIData {
  SearchQuery searchQuery;
  List<Flight> flights;
  Flight selectedFlight;
  Passenger passenger;

  String bookingid;
}
