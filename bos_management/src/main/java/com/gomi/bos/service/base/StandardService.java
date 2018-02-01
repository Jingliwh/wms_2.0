package com.gomi.bos.service.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gomi.bos.domain.base.Standard;

public interface StandardService {

	void save(Standard standard);

	Page<Standard> findAll(Pageable pageable);

	List<Standard> findAll();

}
