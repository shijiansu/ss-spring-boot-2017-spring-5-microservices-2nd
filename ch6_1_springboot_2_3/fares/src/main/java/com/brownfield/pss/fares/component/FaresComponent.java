package com.brownfield.pss.fares.component;

import com.brownfield.pss.fares.entity.Fare;
import com.brownfield.pss.fares.repository.FaresRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FaresComponent {
  FaresRepository faresRepository;

  public FaresComponent() {}

  @Autowired
  public FaresComponent(FaresRepository faresRepository) {
    this.faresRepository = faresRepository;
  }

  public Fare getFare(String flightNumber, String flightDate) {
    log.info("Looking for fares flightNumber " + flightNumber + " flightDate " + flightDate);
    return faresRepository.getFareByFlightNumberAndFlightDate(flightNumber, flightDate);
  }
}
