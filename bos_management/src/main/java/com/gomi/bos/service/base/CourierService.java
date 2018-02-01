package com.gomi.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.gomi.bos.domain.base.Courier;

public interface CourierService {

	void save(Courier courier);

	Page<Courier> findAll(Pageable pageable);

	List<Courier> findAll();

	Page<Courier> findAll(Specification<Courier> specification, Pageable pageable);

	void delBatch(String[] idArray);

}
