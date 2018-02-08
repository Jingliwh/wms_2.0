package com.gomi.test.redis;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class SpringRedisTest {
	
	@Autowired
	@Qualifier("redisTemplate1")
	private RedisTemplate<String, String> redisTemplate;

	@Test
	public void redisTemple(){
		//设置数据("")
		//redisTemplate.opsForValue().set("hello3", "world");
		redisTemplate.opsForValue().set("test", "11111", 20, TimeUnit.SECONDS);
	}
	
}
