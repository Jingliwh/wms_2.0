package com.crm.service.base;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crm.dao.base.CustomerReposity;

import cn.itcast.crm.domain.Customer;
@Service
@Transactional
public class CustomerServiceImp implements CustomerService {
	@Autowired
	private  CustomerReposity customerReposity;
	@Override
	public List<Customer> findNoAssociationCustomers() {
		
		return customerReposity.findByFixedAreaIdIsNull();
	}

	@Override
	public List<Customer> findHadAssociationFixedAreaCustomers(String fixedAreaId) {
		
		return customerReposity.findByFixedAreaId(fixedAreaId);
	}

	@Override
	public void asssociateCustomersToFixedArea(String customerIdsStr, String fixedAreaId) {
		//将已关联变成未关联
		customerReposity.clearFixedAreaId(fixedAreaId);
		//判断customerIdsStr是否为空
		if(customerIdsStr.equals("null")){
			return;
		}
		String[] ids = customerIdsStr.split(",");
		for (String id : ids) {
			if(id!="null"){
				System.out.println(id);
				System.out.println("fixedAreaId"+fixedAreaId);
				customerReposity.updateFixedAreaId(fixedAreaId, Integer.parseInt(id));
			}
			
		}	
	}
	@Override
	public  void register(Customer customer){
		customerReposity.save(customer);
		System.out.println("hehe");
	}

	@Override
	public Customer findByTelephone(String telephone) {
		return customerReposity.findByTelephone(telephone);
	}

	@Override
	public void updateType(String telephone) {
		
		customerReposity.updateType(telephone);
		System.out.println("hehe");
	}

}
