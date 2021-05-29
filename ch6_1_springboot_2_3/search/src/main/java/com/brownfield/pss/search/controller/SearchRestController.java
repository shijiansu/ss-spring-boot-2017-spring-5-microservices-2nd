package com.brownfield.pss.search.controller;

import com.brownfield.pss.search.component.SearchComponent;
import com.brownfield.pss.search.entity.Flight;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@Slf4j
@RequestMapping("/search")
public class SearchRestController {
  private SearchComponent searchComponent;

  @Autowired
  public SearchRestController(SearchComponent searchComponent) {
    this.searchComponent = searchComponent;
  }

  @RequestMapping(value = "/get", method = RequestMethod.POST)
  List<Flight> search(@RequestBody SearchQuery query) {
    log.info("Input : " + query);
    return searchComponent.search(query);
  }
}
