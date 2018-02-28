package com.gomi.bos.service.base.imp;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gomi.bos.dao.base.PromotionReposity;
import com.gomi.bos.domain.base.PageBean;
import com.gomi.bos.domain.base.Promotion;
import com.gomi.bos.service.base.PromotionService;

@Service
@Transactional
public class PromotionServiceImp implements PromotionService {
		@Autowired
		private PromotionReposity  promotionReposity;
	@Override
	public void save(Promotion model) {
		
		promotionReposity.save(model);
	}
	@Override
	public Page<Promotion> findPageData(Pageable pageable) {
		return promotionReposity.findAll(pageable);
	}
	@Override
	public PageBean<Promotion> findPageData(int page, int rows) {
		Pageable  pageable=new PageRequest(page-1, rows);
		//分页查询所有数据
		Page<Promotion> pageData=promotionReposity.findAll(pageable);
		List<Promotion> content = pageData.getContent();
		for (Promotion promotion : content) {
			System.out.println("promotion====service"+promotion.getTitle());
		}
		PageBean<Promotion> pageBean=new PageBean<>();
		pageBean.setTotalCount(pageData.getTotalElements());
		pageBean.setPageData(pageData.getContent());
		return pageBean;
	}
	@Override
	public Promotion findById(Integer id) {
		return promotionReposity.findOne(id);
	}
	@Override
	public void updateDate(Date date) {
		promotionReposity.updateStatus(date);
	}

}
