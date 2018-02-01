package com.gomi.bos.service.base.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gomi.bos.dao.base.StandardRepository;
import com.gomi.bos.domain.base.Standard;
import com.gomi.bos.service.base.StandardService;
@Service
@Transactional
public class StandardServiceImp implements StandardService {
	
	@Autowired
	private StandardRepository  standardDaoImp;
	
	
	@Override
	public void save(Standard standard) {
		standardDaoImp.save(standard);
	}


	@Override
	public Page<Standard> findAll(Pageable pageable) {
		return standardDaoImp.findAll(pageable);
	}


	@Override
	public List<Standard> findAll() {
	
		return standardDaoImp.findAll();
	}

}
