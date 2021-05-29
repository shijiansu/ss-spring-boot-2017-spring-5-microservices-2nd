package com.brownfield.pss.search.controller;

import com.brownfield.pss.search.component.SearchComponent;
import com.brownfield.pss.search.entity.Flight;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@CrossOrigin
@RestController
@Slf4j
@RequestMapping("/search")
public class SearchRestController {
  private final SearchComponent searchComponent;

  @Value("${orginairports.shutdown}")
  private String originAirportShutdownList;

  @Autowired
  public SearchRestController(SearchComponent searchComponent) {
    this.searchComponent = searchComponent;
  }

  @RequestMapping(value = "/get", method = RequestMethod.POST)
  List<Flight> search(@RequestBody SearchQuery query) {
    log.info("Input : " + query);
    if (Arrays.asList(originAirportShutdownList.split(",")).contains(query.getOrigin())) {
      log.info("The origin airport is in shutdown state");
      return new ArrayList<>();
    }
    return searchComponent.search(query);
  }

  @Value("${demo.refreshscope.hello}")
  private String hello;

  @RequestMapping(value = "/hello", method = RequestMethod.GET)
  String hello() {
    return hello;
  }
}
