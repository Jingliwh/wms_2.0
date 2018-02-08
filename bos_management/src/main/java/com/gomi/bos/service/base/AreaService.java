package com.gomi.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.gomi.bos.domain.base.Area;

public interface AreaService {

	void saveBatch(List<Area> arealist);

	List<Area> findAll();

	Page<Area> findAll(Specification<Area> specification, Pageable pageable);

}
