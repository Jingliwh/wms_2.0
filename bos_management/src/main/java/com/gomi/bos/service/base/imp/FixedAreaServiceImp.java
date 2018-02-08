package com.gomi.bos.service.base.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gomi.bos.dao.base.CourierReposity;
import com.gomi.bos.dao.base.FixedAreaReposity;
import com.gomi.bos.dao.base.TakeTimeReposity;
import com.gomi.bos.domain.base.Courier;
import com.gomi.bos.domain.base.FixedArea;
import com.gomi.bos.domain.base.TakeTime;
import com.gomi.bos.service.base.FixedAreaService;
@Service
@Transactional
public class FixedAreaServiceImp implements FixedAreaService {
	@Autowired
	private FixedAreaReposity fixedAreaReposityImp;
	@Autowired
	private CourierReposity courierReposity;
	@Autowired
	private TakeTimeReposity takeTimeReposity;

	@Override
	public void save(FixedArea model) {
		fixedAreaReposityImp.save(model);
	}

	@Override
	public Page<FixedArea> findAll(Specification<FixedArea> specification, Pageable pageable) {
		
		return fixedAreaReposityImp.findAll(specification, pageable);
	}

	@Override
	public void associationCourierToFixedArea(FixedArea model, Integer courierId, Integer takeTimeId) {
		//fixedAreaReposityImp.
		//定区关联快递员
		FixedArea fixedArea = fixedAreaReposityImp.findOne(model.getId());
		Courier courier = courierReposity.findOne(courierId);
		fixedArea.getCouriers().add(courier);//外键维护
		TakeTime takeTime = takeTimeReposity.findOne(takeTimeId);
		courier.setTakeTime(takeTime);
		
	}

}
