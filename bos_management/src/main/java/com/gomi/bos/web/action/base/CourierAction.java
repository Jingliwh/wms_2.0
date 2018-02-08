package com.gomi.bos.web.action.base;

import java.util.ArrayList;
import java.util.List;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import com.gomi.bos.domain.base.Courier;
import com.gomi.bos.domain.base.Standard;
import com.gomi.bos.service.base.CourierService;
import com.opensymphony.xwork2.ActionContext;


@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class CourierAction extends BaseAction<Courier> {
	private static final long serialVersionUID = 1L;

	@Autowired
	private CourierService courierServiceimp;



	@Action(value = "courier_save", results = {
			@Result(name = "success", type = "redirect", location = "./pages/base/courier.html") })
	public String save() {
		courierServiceimp.save(model);
		return SUCCESS;
	}

	@Action(value = "courier_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
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
				if (StringUtils.isNotBlank(model.getCourierNum())) {
					Predicate p1 = cb.equal(root.get("courierNum").as(String.class), model.getCourierNum());
					plist.add(p1);
				}
				if (StringUtils.isNotBlank(model.getType())) {
					Predicate p2 = cb.equal(root.get("type").as(String.class), model.getType());
					plist.add(p2);
				}
				if (StringUtils.isNotBlank(model.getCompany())) {
					Predicate p3 = cb.equal(root.get("company").as(String.class), model.getCompany());
					plist.add(p3);
				}
				// 参照其他表--多表查询
				Join<Courier, Standard> standardJoin = root.join("standard", JoinType.INNER);
				if (model.getStandard() != null && StringUtils.isNotBlank(model.getStandard().getName())) {
					Predicate p4 = cb.like(standardJoin.get("name").as(String.class),
							"%" + model.getStandard().getName() + "%");
					plist.add(p4);
				}
				return cb.and(plist.toArray(new Predicate[0]));
			}
		};
		Page<Courier> pages = courierServiceimp.findAll(specification, pageable);
		//将结果存入值栈
		pushPageDataIntoStack(pages);
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
	
	@Action(value = "find_NoAssociationCourier",  results = { @Result(name = "success", type = "json") })
	public String findNoAssociation() {
		List<Courier> list=courierServiceimp.findNoAssociation();
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;

	}


}
