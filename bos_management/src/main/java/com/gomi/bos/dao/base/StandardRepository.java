package com.gomi.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gomi.bos.domain.base.Standard;

public interface StandardRepository extends JpaRepository<Standard, Integer> {
			List<Standard>findByName(String name);//name为Standard类对应属性
			List<Standard>findByNameLike(String name);//模糊查询
			Standard findByNameAndMinLength(String name,Integer minLength);//多列等值查询
			@Query(value="from  Standard where name=?",nativeQuery=false)
			Standard  queryName(String name);//JPAL自定义查询
			@Query(value="select * from t_standard where c_name=?",nativeQuery=true)
			Standard  nativeQueryName(String name);//本地SQL查询
			//带条件的DML语句（）
			@Modifying
			@Query("update Standard set minLength=?2 where name=?1")
			@Transactional
			public void updateMinLengthByName(String name,Integer minLength);
			
			
			
			
}
