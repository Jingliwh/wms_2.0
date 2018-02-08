package com.gomi.bos.service.base.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gomi.bos.dao.base.TakeTimeReposity;
import com.gomi.bos.domain.base.TakeTime;
import com.gomi.bos.service.base.TakeTimeService;
@Service
@Transactional
public class TakeTimeServiceImp implements TakeTimeService {
	@Autowired
	private TakeTimeReposity takeTimeReposity;

	@Override
	public List<TakeTime> findAll() {
		
		return takeTimeReposity.findAll();
	}

}
