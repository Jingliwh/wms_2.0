package com.gomi.bos.web.action.base;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

@Controller
@Namespace("/")
@ParentPackage("json-default")
@Scope("prototype")
public class ImageAction extends BaseAction<Object> {
	private static final long serialVersionUID = -6265062469052748625L;
	//上传的文件名为imgFile，属性驱动完成
	private	File imgFile;
	private String imgFileFileName;
	private String imgFileContentType;
	
	public void setImgFile(File imgFile) {
		this.imgFile = imgFile;
	}

	public void setImgFileFileName(String imgFileFileName) {
		this.imgFileFileName = imgFileFileName;
	}

	public void setImgFileContentType(String imgFileContentType) {
		this.imgFileContentType = imgFileContentType;
	}


	@Action(value="image_uploadImg" ,results={@Result(name="success",type="json")})
	public String uploadImg(){
		System.out.println("文件："+imgFile);
		System.out.println("文件名："+imgFileFileName);
		System.out.println("文件类型："+imgFileContentType);
		//配置文件保存绝对路径
		String savePath=ServletActionContext.getServletContext().getRealPath("/upload/");
		System.out.println("savePath="+savePath);
		//获取工程保存的相对路径
		String  saveUrl=ServletActionContext.getRequest().getContextPath()+"/upload/";
		//生成保存文件名
		 UUID uuid = UUID.randomUUID();
		 String  savefile=uuid+imgFileFileName.substring(imgFileFileName.lastIndexOf("."));
		 // 保存图片
		 File destFile = new File(savePath + "/" + savefile);
		System.out.println(destFile.getAbsolutePath());
			
		 try {
			 FileUtils.copyFile(imgFile, destFile);
			//map保存上传结果压入值栈
			Map<String,Object> maps=new HashMap<>();
			maps.put("error", 0);
			maps.put("url", saveUrl+savefile);
			//最后返回数据
			ActionContext.getContext().getValueStack().push(maps);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}
	@Action(value="image_manage" ,results={@Result(name="success",type="json")})
	public  String  imageManage(){
		//管理图片
		//根目录路径，可以指定绝对路径，比如 /var/www/attached/
		String rootPath = ServletActionContext.getServletContext().getRealPath("/") + "upload/";
		//根目录URL，可以指定绝对路径，比如 http://www.yoursite.com/attached/
		String rootUrl  =ServletActionContext.getRequest().getContextPath() + "/upload/";
		//图片扩展名
		String[] fileTypes = new String[]{"gif", "jpg", "jpeg", "png", "bmp"};
		//
		File currentPathFile =new File(rootPath);
		List<Map<String,Object>> fileList = new ArrayList<>();
		if(currentPathFile.listFiles() != null) {
			for (File file : currentPathFile.listFiles()) {
				 Map<String, Object> hash = new HashMap<String, Object>();
				String fileName = file.getName();
				if(file.isDirectory()) {
					hash.put("is_dir", true);
					hash.put("has_file", (file.listFiles() != null));
					hash.put("filesize", 0L);
					hash.put("is_photo", false);
					hash.put("filetype", "");
				} else if(file.isFile()){
					String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
					hash.put("is_dir", false);
					hash.put("has_file", false);
					hash.put("filesize", file.length());
					hash.put("is_photo", Arrays.<String>asList(fileTypes).contains(fileExt));
					hash.put("filetype", fileExt);
				}
				hash.put("filename", fileName);
				hash.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
				fileList.add(hash);
			}
			
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("moveup_dir_path", "");
		result.put("current_dir_path", rootPath);
		result.put("current_url", rootUrl);
		result.put("total_count", fileList.size());
		result.put("file_list", fileList);
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}
	
}

