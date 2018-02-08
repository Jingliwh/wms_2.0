package com.crm.domain.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.crm.dao.base.CustomerReposity;
import com.crm.service.base.CustomerService;

import cn.itcast.crm.domain.Customer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class CustomerTest {
	@Autowired
	private CustomerService customerService;
	@Autowired
	private CustomerReposity customerReposity;
	@Test
	public void testfindNoAssociationCustomers(){
		List<Customer> list = customerService.findNoAssociationCustomers();
		for (Customer customer : list) {
			System.out.println(customer);
		}
	}
	
	@Test
	public void testfindAssociationCustomers(){
		List<Customer> list = customerService.findHadAssociationFixedAreaCustomers("dp002");
		for (Customer customer : list) {
			System.out.println(customer);
		}
	}
	
	@Test
	public void testupdateFixedAreaId(){
		customerService.asssociateCustomersToFixedArea("101,102", "dp00001");
	
		
	}
	
	@Test
	public void testupdateSave(){
		Customer customer=new Customer();
		//customer.setId(111);
		customer.setTelephone("11111");
		customerService.register(customer);
		//customerReposity.save(customer);
		
	}
	
	@Test
	public void testupdateType(){
		String telephone="11111";
		customerService.updateType(telephone);
		
	}
	
}
