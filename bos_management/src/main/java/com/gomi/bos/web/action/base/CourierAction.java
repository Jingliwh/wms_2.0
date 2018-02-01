package com.gomi.bos.web.action.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.gomi.bos.domain.base.Courier;
import com.gomi.bos.domain.base.Standard;
import com.gomi.bos.service.base.CourierService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class CourierAction extends ActionSupport implements ModelDriven<Courier> {
	private static final long serialVersionUID = 1L;
	private Courier courier = new Courier();

	@Autowired
	private CourierService courierServiceimp;

	@Override
	public Courier getModel() {
		return courier;
	}

	@Action(value = "courier_save", results = {
			@Result(name = "success", type = "redirect", location = "./pages/base/courier.html") })
	public String save() {
		System.out.println("保存标准");
		courierServiceimp.save(courier);
		return SUCCESS;
	}

	private int page;
	private int rows;

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	@Action(value = "courier_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		Pageable pageable = new PageRequest(page - 1, rows);
		// 创建
		Specification<Courier> specification = new Specification<Courier>() {
			@Override
			/**
			 * root :获取条件表达式 name=? query:获取条件，where=？ cb:构造条件对象
			 */
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// 构造list集合存储查询参数条件
				List<Predicate> plist = new ArrayList<>();
				// 本实体类
				if (StringUtils.isNotBlank(courier.getCourierNum())) {
					Predicate p1 = cb.equal(root.get("courierNum").as(String.class), courier.getCourierNum());
					plist.add(p1);
				}
				if (StringUtils.isNotBlank(courier.getType())) {
					Predicate p2 = cb.equal(root.get("type").as(String.class), courier.getType());
					plist.add(p2);
				}
				if (StringUtils.isNotBlank(courier.getCompany())) {
					Predicate p3 = cb.equal(root.get("company").as(String.class), courier.getCompany());
					plist.add(p3);
				}
				// 参照其他表--多表查询
				Join<Courier, Standard> standardJoin = root.join("standard", JoinType.INNER);
				if (courier.getStandard() != null && StringUtils.isNotBlank(courier.getStandard().getName())) {
					Predicate p4 = cb.like(standardJoin.get("name").as(String.class),
							"%" + courier.getStandard().getName() + "%");
					plist.add(p4);
				}
				return cb.and(plist.toArray(new Predicate[0]));
			}
		};

		Page<Courier> pages = courierServiceimp.findAll(specification, pageable);

		// 利用map存储数据
		Map<String, Object> jsons = new HashMap<>();
		jsons.put("total", pages.getTotalElements());
		System.out.println("总条数" + pages.getTotalElements());
		jsons.put("rows", pages.getContent());
		// 压入值栈
		ServletActionContext.getContext().getValueStack().push(jsons);
		return SUCCESS;
	}

	@Action(value = "courier_findAll", results = { @Result(name = "success", type = "json") })
	public String findAll() {
		List<Courier> list = courierServiceimp.findAll();
		ServletActionContext.getContext().getValueStack().push(list);
		return SUCCESS;

	}
	private String ids;
	

	public void setIds(String ids) {
		this.ids = ids;
	}

	@Action(value = "courier_delbatch", results = { @Result(name = "success", type = "redirect",location="./pages/base/courier.html") })
	public String delBatch() {
		String[] idArray=ids.split(",");
		 courierServiceimp.delBatch(idArray);
		return SUCCESS;

	}

}
