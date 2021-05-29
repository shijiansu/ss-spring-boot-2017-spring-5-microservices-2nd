package com.brownfield.pss.search.component;

import com.brownfield.pss.search.controller.SearchQuery;
import com.brownfield.pss.search.entity.Flight;
import com.brownfield.pss.search.entity.Inventory;
import com.brownfield.pss.search.repository.FlightRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SearchComponent {
  private final FlightRepository flightRepository;

  @Autowired
  public SearchComponent(FlightRepository flightRepository) {
    this.flightRepository = flightRepository;
  }

  public List<Flight> search(SearchQuery query) {
    List<Flight> flights =
        flightRepository.findByOriginAndDestinationAndFlightDate(
            query.getOrigin(), query.getDestination(), query.getFlightDate());
    List<Flight> searchResult = new ArrayList<>(flights);
    flights.forEach(
        flight -> {
          flight.getFares();
          int inv = flight.getInventory().getCount();
          if (inv < 0) {
            searchResult.remove(flight);
          }
        });
    return searchResult;
  }

  public void updateInventory(String flightNumber, String flightDate, int inventory) {
    log.info("Updating inventory for flight " + flightNumber + " inventory " + inventory);
    Flight flight = flightRepository.findByFlightNumberAndFlightDate(flightNumber, flightDate);
    Inventory inv = flight.getInventory();
    inv.setCount(inventory);
    flightRepository.save(flight);
  }
}
