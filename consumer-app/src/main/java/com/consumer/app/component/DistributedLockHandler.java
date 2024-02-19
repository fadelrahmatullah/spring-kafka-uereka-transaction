package com.consumer.app.component;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import com.consumer.app.vo.LockJob;

@Component
public class DistributedLockHandler {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(DistributedLockHandler.class);
    
    private final static long LOCK_EXPIRE = 30 * 1000L;//30s
    private final static long LOCK_TRY_INTERVAL = 1000L;//1000ms
    private final static long LOCK_TRY_TIMEOUT = 20 * 1000L;//20s

    public boolean tryLock(LockJob lock) {
        return getLock(lock, LOCK_TRY_TIMEOUT, LOCK_TRY_INTERVAL, LOCK_EXPIRE);
    }

    public boolean getLock(LockJob lock, long timeout, long tryInterval, long lockExpireTime) {
        try {
            if (StringUtils.isEmpty(lock.getNameJob()) || StringUtils.isEmpty(lock.getValJob())) {
                return false;
            }
            long startTime = System.currentTimeMillis();
            do {
                if (setLock(lock.getNameJob(), lockExpireTime)){
                    return true;
                }
                if (System.currentTimeMillis() - startTime > timeout) {
                    return false;
                }
                Thread.sleep(tryInterval);
            }
            while (true);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    public boolean setLock(String key, long expire) {
        try {
            RedisCallback<String> callback = (connection) -> {
                Jedis jedis = (Jedis) connection.getNativeConnection();
                SetParams params = new SetParams().nx().px(expire);
                String uuid = UUID.randomUUID().toString();
                return jedis.set(key, uuid, params);
            };
            
            String result = redisTemplate.execute(callback);
            return !StringUtils.isEmpty(result);
        } catch (Exception e) {
            logger.error("set redis occured an exception", e);
        }
        return false;
    }
}
