package com.brownfield.pss.checkin.controller;

import com.brownfield.pss.checkin.component.CheckinComponent;
import com.brownfield.pss.checkin.entity.CheckInRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
@CrossOrigin
@RequestMapping("/checkin")
public class CheckInController {

  CheckinComponent checkInComponent;

  @Autowired
  CheckInController(CheckinComponent checkInComponent) {
    this.checkInComponent = checkInComponent;
  }

  @RequestMapping("/get/{id}")
  CheckInRecord getCheckIn(@PathVariable long id) {
    return checkInComponent.getCheckInRecord(id);
  }

  @RequestMapping(value = "/create", method = RequestMethod.POST)
  long checkIn(@RequestBody CheckInRecord checkIn) {
    return checkInComponent.checkIn(checkIn);
  }

  @Value("${demo.refreshscope.hello}")
  private String hello;

  @RequestMapping(value = "/hello", method = RequestMethod.GET)
  String hello() {
    return hello;
  }
}
