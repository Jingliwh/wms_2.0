package com.gomi.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gomi.bos.domain.base.WorkBill;

public interface WorkBillReposity extends JpaRepository<WorkBill, Integer> {

}
