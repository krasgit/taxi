package com.matin.taxi.db;

import com.matin.taxi.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "order")
public class Order extends BaseEntity{

}
