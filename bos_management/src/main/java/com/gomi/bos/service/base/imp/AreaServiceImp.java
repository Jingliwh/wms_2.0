package com.gomi.bos.service.base.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gomi.bos.dao.base.AreaReposity;
import com.gomi.bos.domain.base.Area;
import com.gomi.bos.domain.base.Courier;
import com.gomi.bos.service.base.AreaService;
@Service
@Transactional
public class AreaServiceImp implements AreaService {
	@Autowired
	private AreaReposity areaReposity;

	@Override
	public void saveBatch(List<Area> arealist) {
		areaReposity.save(arealist);
	}

	@Override
	public List<Area> findAll() {
	
		return areaReposity.findAll();
	}

	@Override
	public Page<Area> findAll(Specification<Area> specification, Pageable pageable) {
		// TODO Auto-generated method stub
		return areaReposity.findAll(specification, pageable);
	}

}
