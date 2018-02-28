package com.gomi.bos.dao.base;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gomi.bos.domain.base.Promotion;
@Repository
public interface PromotionReposity  extends JpaRepository<Promotion, Integer> {
	@Query("update Promotion  set status='2' where endDate<? and status='1'")
	@Modifying
	void updateStatus(Date date);
}
