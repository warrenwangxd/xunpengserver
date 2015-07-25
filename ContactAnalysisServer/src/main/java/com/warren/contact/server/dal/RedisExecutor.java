package com.warren.contact.server.dal;

import redis.clients.jedis.Jedis;

public interface RedisExecutor {
	
	public Object execute(Jedis jedis);

}
