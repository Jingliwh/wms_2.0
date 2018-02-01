package com.gomi.bos.web.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gomi.bos.domain.base.Area;
import com.gomi.bos.service.base.AreaService;
import com.opensymphony.xwork2.ActionSupport;
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class AreaAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private File files;
	private String filesContentType;
	private String filesFileName;
	
	@Autowired
	private AreaService areaService;
	
	
	public void setFiles(File files) {
		this.files = files;
	}
	
	public void setFilesContentType(String filesContentType) {
		this.filesContentType = filesContentType;
	}
	
	public void setFilesFileName(String filesFileName) {
		this.filesFileName = filesFileName;
	}
	
	@Action(value="area_upload")
	public String ExcelUpload() throws FileNotFoundException, IOException{
		//最终获取的数据存入List集合
		List<Area>arealist=new ArrayList<>();
		/**
		 * HSSF，XSSF
		 * 解析步骤
		 * 1.获取上传文件
		 * 2.提取每个sheet
		 * 3.获取每个sheet中的行数据
		 * 4.将行数据存入每个对象
		 * 5.将每个对象存入List集合
		 * */
		HSSFWorkbook hssfWorkbook=new HSSFWorkbook(new FileInputStream(files));
		//遍历所有的sheet
		for(int i=0;i<hssfWorkbook.getNumberOfSheets();i++){
				//得到sheet
				HSSFSheet sheet= hssfWorkbook.getSheetAt(i);
				//遍历行数据
				for (Row row : sheet) {
					//判断如果是表头--去掉
					if(row.getRowNum()==0){
						continue;
					}
					//读取到空行的话
					if(row.getCell(0)==null||StringUtils.isBlank(row.getCell(0).getStringCellValue())){
						continue;
					}
					//获取每列属性，设置到对象中
					Area area=new Area();
					area.setId(row.getCell(0).getStringCellValue());
					area.setProvince(row.getCell(1).getStringCellValue());
					area.setCity(row.getCell(2).getStringCellValue());
					area.setDistrict(row.getCell(3).getStringCellValue());
					area.setPostcode(row.getCell(4).getStringCellValue());
					arealist.add(area);
				}
				areaService.saveBatch(arealist);
		}
		
		return NONE;
	}
	

}
