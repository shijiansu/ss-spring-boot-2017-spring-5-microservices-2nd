package com.brownfield.pss.client.controller;

import com.brownfield.pss.client.component.BookingRecord;
import com.brownfield.pss.client.component.CheckInRecord;
import com.brownfield.pss.client.component.Fares;
import com.brownfield.pss.client.component.Flight;
import com.brownfield.pss.client.component.Passenger;
import com.brownfield.pss.client.component.SearchQuery;
import com.brownfield.pss.client.component.UIData;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

@Controller
@Slf4j
public class BrownFieldSiteController {
  @Value("${microservice.book.hostname:localhost}")
  String bookHostname;

  @Value("${microservice.search.hostname:localhost}")
  String searchHostname;

  @Value("${microservice.checkin.hostname:localhost}")
  String checkinHostname;

  RestTemplate searchClient = new RestTemplate();

  RestTemplate bookingClient = new RestTemplate();

  RestTemplate checkInClient = new RestTemplate();

  @RequestMapping(
      value = {"/", "/search"},
      method = RequestMethod.GET)
  public String greetingForm(Model model) {
    SearchQuery query = new SearchQuery("NYC", "SFO", "22-JAN-18");
    UIData uiData = new UIData();
    uiData.setSearchQuery(query);
    model.addAttribute("uidata", uiData);
    return "search";
  }

  @RequestMapping(value = "/search", method = RequestMethod.POST)
  public String greetingSubmit(@ModelAttribute UIData uiData, Model model) {
    Flight[] flights =
        searchClient.postForObject(
            "http://" + searchHostname + ":8083/search/get",
            uiData.getSearchQuery(),
            Flight[].class);
    uiData.setFlights(Arrays.asList(flights));
    model.addAttribute("uidata", uiData);
    return "result";
  }

  @RequestMapping(
      value = "/book/{flightNumber}/{origin}/{destination}/{flightDate}/{fare}",
      method = RequestMethod.GET)
  public String bookQuery(
      @PathVariable String flightNumber,
      @PathVariable String origin,
      @PathVariable String destination,
      @PathVariable String flightDate,
      @PathVariable String fare,
      Model model) {
    UIData uiData = new UIData();
    Flight flight =
        new Flight(flightNumber, origin, destination, flightDate, new Fares(fare, "AED"));
    uiData.setSelectedFlight(flight);
    uiData.setPassenger(new Passenger());
    model.addAttribute("uidata", uiData);
    return "book";
  }

  @RequestMapping(value = "/confirm", method = RequestMethod.POST)
  public String ConfirmBooking(@ModelAttribute UIData uiData, Model model) {
    Flight flight = uiData.getSelectedFlight();
    BookingRecord booking =
        new BookingRecord(
            flight.getFlightNumber(),
            flight.getOrigin(),
            flight.getDestination(),
            flight.getFlightDate(),
            null,
            flight.getFares().getFare());
    Set<Passenger> passengers = new HashSet<>();
    Passenger pax = uiData.getPassenger();
    pax.setBookingRecord(booking);
    passengers.add(uiData.getPassenger());
    booking.setPassengers(passengers);
    long bookingId = 0;
    try {
      bookingId =
          bookingClient.postForObject(
              "http://" + bookHostname + ":8080/booking/create", booking, long.class);
      log.info("Booking created {}", bookingId);
    } catch (Exception e) {
      log.error("BOOKING SERVICE NOT AVAILABLE...!!!");
    }
    model.addAttribute("message", "Your Booking is confirmed. Reference Number is " + bookingId);
    return "confirm";
  }

  @RequestMapping(value = "/search-booking", method = RequestMethod.GET)
  public String searchBookingForm(Model model) {
    UIData uiData = new UIData();
    uiData.setBookingid("10");
    model.addAttribute("uidata", uiData);
    return "bookingsearch";
  }

  @RequestMapping(value = "/search-booking-get", method = RequestMethod.POST)
  public String searchBookingSubmit(@ModelAttribute UIData uiData, Model model) {
    long id = Long.parseLong(uiData.getBookingid());
    BookingRecord booking =
        bookingClient.getForObject(
            "http://" + bookHostname + ":8080/booking/get/" + id, BookingRecord.class);

    if (booking != null) {
      Flight flight =
          new Flight(
              booking.getFlightNumber(),
              booking.getOrigin(),
              booking.getDestination(),
              booking.getFlightDate(),
              new Fares(booking.getFare(), "AED"));
      Passenger pax = booking.getPassengers().iterator().next();
      Passenger paxUI = new Passenger(pax.getFirstName(), pax.getLastName(), pax.getGender(), null);
      uiData.setPassenger(paxUI);
      uiData.setSelectedFlight(flight);
      uiData.setBookingid(Long.toString(id));
    }

    model.addAttribute("uidata", uiData);
    return "bookingsearch";
  }

  @RequestMapping(
      value =
          "/checkin/{flightNumber}/{origin}/{destination}/{flightDate}/{fare}/{firstName}/{lastName}/{gender}/{bookingid}",
      method = RequestMethod.GET)
  public String bookQuery(
      @PathVariable String flightNumber,
      @PathVariable String origin,
      @PathVariable String destination,
      @PathVariable String flightDate,
      @PathVariable String fare,
      @PathVariable String firstName,
      @PathVariable String lastName,
      @PathVariable String gender,
      @PathVariable String bookingid,
      Model model) {

    CheckInRecord checkIn =
        new CheckInRecord(
            firstName, lastName, "28C", null, flightDate, flightDate, Long.parseLong(bookingid));

    long checkinId =
        checkInClient.postForObject(
            "http://" + checkinHostname + ":8081/checkin/create", checkIn, long.class);
    model.addAttribute("message", "Checked In, Seat Number is 28c , checkin id is " + checkinId);
    return "checkinconfirm";
  }
}
