package com.gomi.bos.web.action.base;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gomi.bos.domain.base.TakeTime;
import com.gomi.bos.service.base.TakeTimeService;
import com.opensymphony.xwork2.ActionContext;
@Controller
@Namespace("/")
@ParentPackage("json-default")
@Scope("prototype")
public class TakeTimeAction extends BaseAction<TakeTime> {
	@Autowired
	private TakeTimeService takeTimeServiceImp;

	private static final long serialVersionUID = -7870154116763078611L;

	@Action(value="takeTime_findAll", results={ @Result(name = "success", type = "json") })
	public String findAll(){
		System.out.println("hello");
		List<TakeTime>  takeTimes =takeTimeServiceImp.findAll();
		ActionContext.getContext().getValueStack().push(takeTimes);
		
		return SUCCESS;
	}
}
