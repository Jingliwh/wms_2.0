package com.gomi.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gomi.bos.domain.base.Area;

public interface AreaReposity extends JpaRepository<Area, String> {

}
