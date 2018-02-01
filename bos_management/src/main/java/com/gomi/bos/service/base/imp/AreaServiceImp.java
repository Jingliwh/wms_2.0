package com.gomi.bos.service.base.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gomi.bos.dao.base.AreaReposity;
import com.gomi.bos.domain.base.Area;
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

}
