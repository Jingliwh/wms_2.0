package com.gomi.test.redis;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


public class FreeMarkerTest {
	
	@Test
	public  void testFm(){
		//1.创建Configuration，生成模板实例
		Configuration  conf=new Configuration(Configuration.VERSION_2_3_22);
		try {
			conf.setDirectoryForTemplateLoading(new File("src/main/webapp/WEB-INF/template"));
			//2.指定使用模板文件，生成Template实例
			Template template=conf.getTemplate("hello.ftl");
			Map<String, String> map = new HashMap<String, String>();
			//3.填充数据模型，数据模型就是一个Map
			map.put("title", "标题");
			map.put("message", "信息");
			//	4.调用Template实例process完成数据合并
			 template.process(map, new PrintWriter(System.out));
		}catch (ParseException e) {e.printStackTrace();
		} catch (IOException e) {e.printStackTrace();
		} catch (TemplateException e) {e.printStackTrace();}
	}

}
