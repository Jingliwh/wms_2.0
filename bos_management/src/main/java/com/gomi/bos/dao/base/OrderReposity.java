package com.gomi.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gomi.bos.domain.base.Order;

public interface OrderReposity  extends  JpaRepository<Order, Integer> {

}
