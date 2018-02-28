package com.gome.before.action;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gomi.bos.domain.base.Area;
import com.gomi.bos.domain.base.Order;

import cn.itcast.crm.domain.Customer;
@Namespace("/")
@ParentPackage("json-default")
@Controller
@Scope("prototype")
public class OrderAction extends  BaseAction<Order>{
	private static final long serialVersionUID = 7916491046408282667L;
	
	private String sendAreaInfo;
	private String recAreaInfo;
	public void setSendAreaInfo(String sendAreaInfo) {
		this.sendAreaInfo = sendAreaInfo;
	}
	public void setRecAreaInfo(String recAreaInfo) {
		this.recAreaInfo = recAreaInfo;
	}
	@Action(value="order_save",results={
			@Result(name="success",type="redirect",location="index.html")
	})
	public String save(){
			//拿到寄件人省市区信息
			String[] sendinfo = sendAreaInfo.split("/");
			Area  sendArea=new Area();
			sendArea.setProvince(sendinfo[0]);
			sendArea.setCity(sendinfo[1]);
			sendArea.setDistrict(sendinfo[2]);
			//拿到收件人省市区信息
			String[] recinfo = recAreaInfo.split("/");
			Area  recArea=new Area();
			recArea.setProvince(recinfo[0]);
			recArea.setCity(recinfo[1]);
			recArea.setDistrict(recinfo[2]);
			//添加到order，关联 
			model.setSendArea(sendArea);
			model.setRecArea(recArea);
			//关联客户
			Customer  customer=(Customer) ServletActionContext.getRequest().getSession().getAttribute("customer");
			model.setCustomer_id(customer.getId());
			//利用webservice 调用bos_manager,对象传递需要为domain 实体类设置rootXmlElement注解
			String url="http://localhost:8080/bos_management/services/orderService/saveOrder";
			WebClient.create(url).type(MediaType.APPLICATION_JSON).post(model);
		return SUCCESS;
	}
	
	
	

}
