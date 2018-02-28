package com.gomi.bos.web.action.base;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
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
import org.springframework.stereotype.Controller;

import com.gomi.bos.domain.base.Promotion;
import com.gomi.bos.service.base.PromotionService;
@Namespace("/")
@ParentPackage("json-default")
@Scope("prototype")
@Controller
public class PromotionAction extends BaseAction<Promotion> {
	
	@Autowired
	private PromotionService promotionService;
	private  File titleImgSrc;
	private  String titleImgSrcFileName;
	
	public void setTitleImgSrc(File titleImgSrc) {
		this.titleImgSrc = titleImgSrc;
	}
	public void setTitleImgSrcFileName(String titleImgSrcFileName) {
		this.titleImgSrcFileName = titleImgSrcFileName;
	}
	
	@Action(value="promotion_save",results={@Result(name="success",type="redirect", location = "./pages/take_delivery/promotion.html")})
	public String save(){
				//配置文件保存绝对路径
				String savePath=ServletActionContext.getServletContext().getRealPath("/upload/");
				System.out.println("savePath="+savePath);
				//获取工程保存的相对路径
				String  saveUrl=ServletActionContext.getRequest().getContextPath()+"/upload/";
				//生成保存文件名
				 UUID uuid = UUID.randomUUID();
				 System.out.println("文件上传--："+titleImgSrcFileName);
				 String  savefile=uuid+titleImgSrcFileName.substring(titleImgSrcFileName.lastIndexOf("."));
				 // 保存图片
				 File destFile = new File(savePath + "/" + savefile);
				 System.out.println(destFile.getAbsolutePath());
					 try {
						FileUtils.copyFile(titleImgSrc, destFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
					 model.setTitleImg(saveUrl+savefile);
		
					 promotionService.save(model);
		return SUCCESS;
	}
	@Action(value="promotion_pageQuery",results={@Result(name="success",type="json")})
	public  String pageQuery(){
		Page<Promotion> pageData=promotionService.findPageData(pageable);
		System.out.println("rows"+rows);
		this.pushPageDataIntoStack(pageData);
		return SUCCESS;
	}
}
