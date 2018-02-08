package com.gomi.bos.service.base.imp;

import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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

	@Override
	public List<Courier> findNoAssociation() {
		//查找未关联定区的快递员，条件 
		Specification<Courier> speciation=new Specification<Courier>() {

			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate=cb.isEmpty(root.get("fixedAreas").as(Set.class));
				return predicate;
			}
		};
		return courierReposity.findAll(speciation);
	}
	
	

}
