package com.gomi.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gomi.bos.domain.base.FixedArea;

public interface FixedAreaReposity extends JpaRepository<FixedArea, String>,JpaSpecificationExecutor<FixedArea> {

}
