package com.gomi.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gomi.bos.domain.base.TakeTime;

public interface TakeTimeReposity extends JpaRepository<TakeTime, Integer> {

}
