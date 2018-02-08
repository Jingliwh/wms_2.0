package com.gomi.bos.web.action.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.gomi.bos.domain.base.FixedArea;
import com.gomi.bos.service.base.FixedAreaService;
import com.opensymphony.xwork2.ActionContext;

import cn.itcast.crm.domain.Customer;
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class FixedAreaAction extends BaseAction<FixedArea> {
	private static final long serialVersionUID = -5407087930681484742L;
	
	@Autowired
	private  FixedAreaService  fixedAreaServiceImp;

	@Action(value="fixedarea_save",results={@Result(name="success",type="redirect", location = "./pages/base/fixed_area.html")})
	public String save(){
		fixedAreaServiceImp.save(model);
		return SUCCESS;
	}
	
	@Action(value = "fixedArea_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		// 创建
		Specification<FixedArea> specification = new Specification<FixedArea>() {
			@Override
			/**
			 * root :获取条件表达式 name=? query:获取条件，where=？ cb:构造条件对象
			 */
			public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// 构造list集合存储查询参数条件
				List<Predicate> plist = new ArrayList<>();
				// 本实体类
				if(StringUtils.isNotBlank(model.getId())){
					Predicate p1=cb.equal( root.get("id").as(String.class),model.getId());
					plist.add(p1);
				}
				if(StringUtils.isNotBlank(model.getCompany())){
					Predicate p2=cb.like(root.get("company").as(String.class), "%"+model.getCompany()+"%");
					plist.add(p2);
				}
				return cb.and(plist.toArray(new Predicate[0]));
			}
		};
		Page<FixedArea> pages = fixedAreaServiceImp.findAll(specification, pageable);
		//将结果存入值栈
		pushPageDataIntoStack(pages);
		return SUCCESS;
	}
	@Action(value="fixedArea_findNoAssociationCustomers",results={@Result(name="success",type="json")})
	public String findNoAssociationCustomers(){
		String url="http://localhost:9001/crm_management/services/customerService/noassociationcustomers";
		Collection<? extends Customer> customers = WebClient.create(url).accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
			for (Customer customer : customers) {
				System.out.println(customer);
			}
		ActionContext.getContext().getValueStack().push(customers);
		return SUCCESS;
	}
	
	@Action(value="fixedArea_findHadAssociationCustomers",results={@Result(name="success",type="json")})
	public String findHadAssociationCustomers(){
		String url="http://localhost:9001/crm_management/services/customerService/hadassociationfixedareacustomers/"+model.getId();
		Collection<? extends Customer> customers = WebClient.create(url).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).getCollection(Customer.class);
		for (Customer customer : customers) {
			System.out.println(customer);
		}	
		ActionContext.getContext().getValueStack().push(customers);
		return SUCCESS;
	}
	private String[] customerIds;
	
	public void setCustomerIds(String[] customerIds) {
		this.customerIds = customerIds;
	}

	@Action(value="fixedArea_associateCustomersToFixedArea",results={@Result(name="success",type="redirect", location = "./pages/base/fixed_area.html")})
	public String associateCustomersToFixedArea(){
		String idsStr=StringUtils.join(customerIds,",");
		System.out.println("idsStr="+idsStr);
		String url="http://localhost:9001/crm_management/services/customerService/asssociatecustomerstofixedarea?customerIdsStr="+idsStr+"&fixedAreaId="+model.getId();
		System.out.println("fixedAreaId="+model.getId());
		WebClient.create(url).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).put(null);
		
		return SUCCESS;
	}
	
	private Integer courierId;
	private Integer takeTimeId;
	
	
	public void setCourierId(Integer courierId) {
		this.courierId = courierId;
	}

	public void setTakeTimeId(Integer takeTimeId) {
		this.takeTimeId = takeTimeId;
	}

	@Action(value="fixedArea_associationCourierToFixedArea",results={@Result(name="success",type="redirect", location = "./pages/base/fixed_area.html")})
	public String associationCourierToFixedArea(){
		System.out.println("courierId="+courierId);
		System.out.println("takeTimeId="+takeTimeId);
		System.out.println("model="+model.getId());
		fixedAreaServiceImp.associationCourierToFixedArea(model,courierId,takeTimeId);
		return SUCCESS;
	}
	
}
