package com.brownfield.pss.search.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Inventory {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "inv_id")
  long id;

  int count;

  public Inventory() {}

  public Inventory(int count) {
    this.count = count;
  }
}
