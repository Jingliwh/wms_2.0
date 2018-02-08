package com.gomi.bos.dao.standard.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gomi.bos.dao.base.StandardRepository;
import com.gomi.bos.domain.base.Standard;
import com.gomi.bos.util.base.PinYin4jUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class StandardTest {
	@Autowired
	private StandardRepository standardRepositoryImp;
	
	@Test
	public void findbyName(){
		List<Standard> list = standardRepositoryImp.findByName("0-10公斤");
		for (Standard standard : list) {
			System.out.println(standard);
		}
	}
	
	@Test
	public void findbyNameLike(){
		List<Standard> list = standardRepositoryImp.findByNameLike("%公斤%");
		for (Standard standard : list) {
			System.out.println(standard);
		}
	}
	
		@Test
		public void findByNameAndMinLength(){
			 Standard standard = standardRepositoryImp.findByNameAndMinLength("20-40公斤", 1);
			 System.out.println(standard);
		}

		@Test
		public void queryName(){
			Standard name = standardRepositoryImp.queryName("10-20公斤");
			System.out.println(name);
			
		}
		
		@Test
		public void nativeQueryName(){
			Standard name = standardRepositoryImp.nativeQueryName("20-40公斤");
			System.out.println(name);
			
		}
		
		@Test
		public void updateMinLengthByName(){
			standardRepositoryImp.updateMinLengthByName("20-40公斤",11);
		}
		
		@Test
		public void testHello(){
			//河北
			String province="北京市";
			//保定
			String  city="北京市";
			//易县
			String district="房山区";
			
			StringBuffer sunion=new StringBuffer();
			sunion=sunion.append(province).append(city).append(district);
			String[] headByString = PinYin4jUtils.getHeadByString(sunion.toString());
			for (int i = 0; i < headByString.length; i++) {
				System.out.print(headByString[i]);//BJSBJSFSQ
			}
			
		}
		
}
