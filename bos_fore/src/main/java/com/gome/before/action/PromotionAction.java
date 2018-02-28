package com.gome.before.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gomi.bos.domain.base.PageBean;
import com.gomi.bos.domain.base.Promotion;
import com.opensymphony.xwork2.ActionContext;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
@Controller
@Namespace("/")
@ParentPackage("json-default")
@Scope("prototype")
public class PromotionAction extends BaseAction<Promotion>{
	private static final long serialVersionUID = 1L;
	
	@Action(value="promotion_pageQuery",results={@Result(name="success",type="json")})
	public  String pageQuery(){
		//webService远程调用--bos_management
		String remoteSql="http://localhost:8080/bos_management/services/promotionService/pageQuery?page="+page+"&rows="+rows;
		@SuppressWarnings("unchecked")
		PageBean<Promotion> pageBean=WebClient.create(remoteSql).accept(MediaType.APPLICATION_JSON).get(PageBean.class);
		System.out.println("pageBean="+pageBean);
		System.out.println("pageBeancount="+pageBean.getTotalCount());
		System.out.println("rows="+rows);
		ActionContext.getContext().getValueStack().push(pageBean);
		return SUCCESS;
	}
	
	@Action(value="promotion_ShowDetail")
	public String showDetail() throws IOException, TemplateException{
		//先判断id对应的页面是否生成--拿到目录根路径
		String realHtml=ServletActionContext.getServletContext().getRealPath("/freemarker");
		//拿到以后判断
		File file=new File(realHtml+"/"+model.getId()+".html");
		if(!file.exists()){
			//如果网页未生成---调用模板生成网页
			Configuration configuration=new Configuration(Configuration.VERSION_2_3_22);
			configuration.setDirectoryForTemplateLoading(new File(ServletActionContext.getServletContext().getRealPath("/WEB-INF/template")));
			//设置模板类
			Template template = configuration.getTemplate("promotion_detail.ftl");
			//获取动态数据设置到模板
			String remoteSql="http://localhost:8080/bos_management/services/promotionService/findById/"+model.getId();
			Promotion promotion=WebClient.create(remoteSql).accept(MediaType.APPLICATION_JSON).get(Promotion.class);
			Map<String, Object> promap=new HashMap<>();
			promap.put("promotion", promotion);
			//合并输出文件
			template.process(promap, new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));
		}
		//存在的话直接返回
		ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
		FileUtils.copyFile(file, ServletActionContext.getResponse().getOutputStream());
		return NONE;
	}
}
