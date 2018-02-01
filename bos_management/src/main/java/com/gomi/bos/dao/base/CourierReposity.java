package com.gomi.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.gomi.bos.domain.base.Courier;

public interface CourierReposity  extends JpaRepository<Courier, Integer>,JpaSpecificationExecutor<Courier>{

	@Query(value="update t_courier set c_deltag=1 where c_id=?",nativeQuery=true )
	@Modifying
	public void delBatch(int parseInt);

	

}
