package com.gomi.bos.service.base.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gomi.bos.dao.base.CourierReposity;
import com.gomi.bos.domain.base.Courier;
import com.gomi.bos.service.base.CourierService;
@Service
@Transactional
public class CourierServiceImp implements CourierService {
	@Autowired
	private CourierReposity courierReposity;

	@Override
	public void save(Courier courier) {
		courierReposity.save(courier);
	}

	@Override
	public Page<Courier> findAll(Pageable pageable) {
	
		return courierReposity.findAll(pageable);
	}

	@Override
	public List<Courier> findAll() {
	
		return courierReposity.findAll();
	}

	@Override
	public Page<Courier> findAll(Specification<Courier> specification, Pageable pageable) {
	
		return courierReposity.findAll(specification, pageable);
	}

	@Override
	public void delBatch(String[] idArray) {
		for (int i = 0; i < idArray.length; i++) {
			courierReposity.delBatch(Integer.parseInt(idArray[i]));
		}
	}
	
	

}
