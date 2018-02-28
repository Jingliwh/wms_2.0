package com.gomi.bos.service.base;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gomi.bos.domain.base.PageBean;
import com.gomi.bos.domain.base.Promotion;

public interface PromotionService {

	void save(Promotion model);

	Page<Promotion> findPageData(Pageable pageable);
	
	@Path("pageQuery")
	@GET
	@Produces({"application/xml","application/json"})
	PageBean<Promotion> findPageData(@QueryParam("page")int page,@QueryParam("rows")int rows);

	
	@Path("findById/{id}")
	@GET
	@Produces({"application/xml","application/json"})
	 Promotion findById(@PathParam("id")Integer id);

	void updateDate(Date date);
	
}
