package com.warren.contact.server.dal;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

public class RedisClient {
	private static  Logger logger =Logger.getLogger(RedisClient.class);

	private static JedisPool jedisPool;// ����Ƭ���ӳ�

	private static ShardedJedisPool shardedJedisPool;// ��Ƭ���ӳ�
	static {
		initialPool();

	}

	/**
	 * ��ȡJedisʵ��
	 * 
	 * @return
	 */
	public synchronized static Jedis getJedis() {
		try {
			if (jedisPool != null) {
				Jedis resource = jedisPool.getResource();
				return resource;
			} else {
				return null;
			}
		} catch (Exception e) {
		   logger.error(e.getMessage(),e);
			return null;
		}
	}

	/**
	 * �ͷ�jedis��Դ
	 * 
	 * @param jedis
	 */
	public static void returnResource(final Jedis jedis) {
		if (jedis != null) {
			jedisPool.returnResource(jedis);
		}
	}

	public static void returnBrokenJedis(final Jedis jedis) {
		jedisPool.returnBrokenResource(jedis);
	}

	/**
	 * ��ʼ������Ƭ��
	 */
	private static void initialPool() {
		// �ػ�������
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxActive(20);
		config.setMaxIdle(5);
		config.setMaxWait(1000l);
		config.setTestOnBorrow(true);
		config.setTestOnReturn(true);

		jedisPool = new JedisPool(config, "127.0.0.1", 6379);
	}

	/**
	 * ��ʼ����Ƭ��
	 */
	private void initialShardedPool() {
		// �ػ�������
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxActive(20);
		config.setMaxIdle(5);
		config.setMaxWait(1000l);
		config.setTestOnBorrow(false);
		// slave����
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		shards.add(new JedisShardInfo("127.0.0.1", 6379, "master"));

		// �����
		shardedJedisPool = new ShardedJedisPool(config, shards);
	}



}
