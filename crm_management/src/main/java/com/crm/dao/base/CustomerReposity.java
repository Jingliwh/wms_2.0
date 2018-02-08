package com.crm.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.crm.domain.Customer;

public interface CustomerReposity extends JpaRepository<Customer, Integer> {
	
	public List<Customer> findByFixedAreaIdIsNull();//未关联定区
	public List<Customer> findByFixedAreaId(String fixedAreaId);
	
	@Query("update Customer set fixedAreaId=? where id=?")
	@Modifying
	public  void updateFixedAreaId(String fixedAreaId,Integer id);
	
	@Query("update Customer set fixedAreaId=null where fixedAreaId=?")
	@Modifying
	public void clearFixedAreaId(String fixedAreaId);
	
	public Customer findByTelephone(String telephone);
	
	@Query("update Customer set type=1 where telephone=?")
	@Modifying
	public void updateType(String telephone);

}