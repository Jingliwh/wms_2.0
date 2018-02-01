package com.gomi.bos.web.action.base;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;

import com.gomi.bos.domain.base.Standard;
import com.gomi.bos.service.base.StandardService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class StandardAction extends ActionSupport  implements ModelDriven<Standard>{
	private static final long serialVersionUID = 1L;
	private Standard standard=new Standard();
	
	@Autowired
	private StandardService standardServiceimp;

	@Override
	public Standard getModel() {
		return standard;
	}
	@Action(value="standard_save",results={@Result(name="success",type="redirect", location = "./pages/base/standard.html")})
	public String save(){
			System.out.println("保存标准");
			standardServiceimp.save(standard);
		return SUCCESS;
	}
	
	private  int page;
	private  int rows;
	private String sort;
	private String order;
	
	public void setPage(int page) {
		this.page = page;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	@Action(value="standard_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery(){
		Pageable pageable=null;
		if(sort!=null){
			Direction direction=null;
			if(order.equals("desc")){
				direction=Direction.DESC;
			}else{
				direction=Direction.ASC;
			}
			Order order=new Sort.Order(direction, sort);
			Sort  sort=new Sort(order);
			pageable=new  PageRequest(page-1, rows,sort);
		}else{
			pageable=new  PageRequest(page-1, rows);
		}
		Page<Standard> pages=standardServiceimp.findAll(pageable);

		//拿到数据以后进行封装---struts-json-plugs
		//利用map存储数据
		Map<String, Object> jsons=new HashMap<>();
		jsons.put("total", pages.getTotalElements());
		System.out.println("总条数"+pages.getTotalElements());
		jsons.put("rows", pages.getContent());
		//压入值栈
		ServletActionContext.getContext().getValueStack().push(jsons);
		return SUCCESS;
	}
	@Action(value="standard_findAll" ,results={@Result(name="success",type="json")})
	public String findAll(){
		List<Standard> list=standardServiceimp.findAll();
		ServletActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
		
	}
	
}
