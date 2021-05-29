package com.brownfield.pss.checkin.component;

import com.brownfield.pss.checkin.entity.CheckInRecord;
import com.brownfield.pss.checkin.repository.CheckinRepository;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CheckinComponent {

  CheckinRepository checkinRepository;
  Sender sender;

  @Autowired
  CheckinComponent(CheckinRepository checkinRepository, Sender sender) {
    this.checkinRepository = checkinRepository;
    this.sender = sender;
  }

  public long checkIn(CheckInRecord checkIn) {
    checkIn.setCheckInTime(new Date());
    log.info("Saving checkin ");

    // save
    long id = checkinRepository.save(checkIn).getId();
    log.info("successfully saved checkin ");

    // send a message back to booking to update status
    log.info("sending booking id " + id);
    sender.send(id);
    return id;
  }

  public CheckInRecord getCheckInRecord(long id) {
    return checkinRepository.findById(id).get();
  }
}
