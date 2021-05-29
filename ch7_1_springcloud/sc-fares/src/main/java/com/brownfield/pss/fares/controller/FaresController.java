package com.brownfield.pss.fares.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.brownfield.pss.fares.component.FaresComponent;
import com.brownfield.pss.fares.entity.Fare;

@RefreshScope
@RestController
@CrossOrigin
@RequestMapping("/fares")
public class FaresController {
  FaresComponent faresComponent;

  @Autowired
  FaresController(FaresComponent faresComponent) {
    this.faresComponent = faresComponent;
  }

  @RequestMapping("/get")
  Fare getFare(
      @RequestParam(value = "flightNumber") String flightNumber,
      @RequestParam(value = "flightDate") String flightDate) {
    return faresComponent.getFare(flightNumber, flightDate);
  }

  @Value("${demo.refreshscope.hello}")
  private String hello;

  @RequestMapping(value = "/hello", method = RequestMethod.GET)
  String hello() {
    return hello;
  }
}
