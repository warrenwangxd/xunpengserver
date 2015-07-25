package com.warren.contact.server.dal;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;

public class RedisTemplate {
	
	private Logger logger = Logger.getLogger(RedisTemplate.class);
	
	public Object execute(Object defaultReturnObj ,RedisExecutor executor) {
		Jedis redisClient=RedisClient.getJedis();
		Object result =null;
		try {
			result=executor.execute(redisClient);
			if(null!=result) {
				return result;
			} else {
				return defaultReturnObj;
			}
		
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			RedisClient.returnBrokenJedis(redisClient);
			return defaultReturnObj;
		} finally {
			RedisClient.returnResource(redisClient);
		}
	}
	

}
