package com.gomi.bos.web.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
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
import com.gomi.bos.domain.base.Area;
import com.gomi.bos.service.base.AreaService;
import com.gomi.bos.util.base.PinYin4jUtils;
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class AreaAction extends BaseAction<Area> {
	private static final long serialVersionUID = 1L;
	private File files;
	@SuppressWarnings("unused")
	private String filesContentType;
	@SuppressWarnings("unused")
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
		@SuppressWarnings("resource")
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
					//河北
					String province=area.getProvince().substring(0, area.getProvince().length()-1);
					//保定
					String  city=area.getCity().substring(0, area.getCity().length()-1);
					//易县
					String district=area.getDistrict().substring(0, area.getDistrict().length()-1);
					//PinYin4jUtils.hanziToPinyin(sunion);
					String[] headByString = PinYin4jUtils.getHeadByString(province+city+district);
					StringBuffer sunion=new StringBuffer();
					for (String string : headByString) {
						sunion.append(string);
					}
					area.setShortcode(sunion.toString());
					//拿到城市代码
					String stry = Arrays.toString(PinYin4jUtils.stringToPinyin(city,false,""));
					String citycode = stry.substring(1, stry.length()-1).replace(", ", "");
				area.setCitycode(citycode);
			}
				areaService.saveBatch(arealist);
		}
		
		return NONE;
	}
	
	@Action(value = "area_findAll", results = { @Result(name = "success", type = "json") })
	public String findAll() {
		List<Area> list = areaService.findAll();
		ServletActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}
	
	@Action(value = "area_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		// 创建
		Specification<Area> specification = new Specification<Area>() {
			@Override
			/**
			 * root :获取条件表达式 name=? query:获取条件，where=？ cb:构造条件对象
			 */
			public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// 构造list集合存储查询参数条件
				List<Predicate> plist = new ArrayList<>();
				// 本实体类
				if (StringUtils.isNotBlank(model.getProvince())) {
					Predicate p1 = cb.like(root.get("province").as(String.class), "%"+model.getProvince()+"%");
					plist.add(p1);
				}
				if (StringUtils.isNotBlank(model.getCity())) {
					Predicate p2 = cb.like(root.get("city").as(String.class), "%"+model.getCity()+"%");
					plist.add(p2);
				}
				if (StringUtils.isNotBlank(model.getDistrict())) {
					Predicate p3 = cb.like(root.get("district").as(String.class), "%"+model.getDistrict()+"%");
					plist.add(p3);
				}
				return cb.and(plist.toArray(new Predicate[0]));
			}
		};
		Page<Area> pages = areaService.findAll(specification, pageable);
		//将结果存入值栈
		pushPageDataIntoStack(pages);
		return SUCCESS;
	}

}
