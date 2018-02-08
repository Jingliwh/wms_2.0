package com.gomi.test.redis;

import redis.clients.jedis.Jedis;

public class RedisTest {

	public static void main(String[] args) {
		Jedis  jedis=new Jedis("localhost");
		jedis.set("haha", "1213");
	}

}
