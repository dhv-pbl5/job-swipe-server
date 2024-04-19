package org.dhv.pbl5server.common_service.repository;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Repository
@Log4j2
public class RedisRepositoryImpl implements RedisRepository {
    public final RedisTemplate<String, Object> redisTemplate;
    public final HashOperations<String, String, Object> hashOperations;
    private final String DEBUG_PREFIX = "REDIS ==> ";

    @Autowired
    public RedisRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void save(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
        log.info("%s Saved key: %s value: %s".formatted(DEBUG_PREFIX, key, value));
    }

    @Override
    public void expire(String key, long timeToLiveInDay) {
        redisTemplate.expire(key, timeToLiveInDay, TimeUnit.DAYS);
        log.info("%s Set expiration for key: %s in %s days".formatted(DEBUG_PREFIX, key, String.valueOf(timeToLiveInDay)));
    }

    @Override
    public void save(String key, String hashKey, Object value) {
        hashOperations.put(key, hashKey, value);
        log.info("%s Saved key: %s hashKey: %s value: %s".formatted(DEBUG_PREFIX, key, hashKey, String.valueOf(value)));
    }

    @Override
    public boolean hashExist(String key, String hashKey) {
        log.info("%s Check if hashKey: %s exists in key: %s".formatted(DEBUG_PREFIX, hashKey, key));
        return hashOperations.hasKey(key, hashKey);
    }

    @Override
    public Object findByKey(String key) {
        log.info("%s Find value by key: %s".formatted(DEBUG_PREFIX, key));
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Map<String, Object> findHashFieldByKey(String key) {
        log.info("%s Find all hash fields by key: %s".formatted(DEBUG_PREFIX, key));
        return hashOperations.entries(key);
    }

    @Override
    public Object findByHashKey(String key, String hashKey) {
        log.info("%s Find value by key: %s hashKey: %s".formatted(DEBUG_PREFIX, key, hashKey));
        return hashOperations.get(key, hashKey);
    }

    @Override
    public List<Object> findAllByHashKeyPrefix(String key, String hashKeyPrefix) {
        log.info("%s Find all values by key: %s hashKeyPrefix: %s".formatted(DEBUG_PREFIX, key, hashKeyPrefix));
        return hashOperations.entries(key)
            .entrySet()
            .stream()
            .filter(entry -> entry.getKey().startsWith(hashKeyPrefix))
            .map(Map.Entry::getValue)
            .toList();
    }

    @Override
    public Set<String> getFieldPrefixes(String key) {
        log.info("%s Get all field prefixes by key: %s".formatted(DEBUG_PREFIX, key));
        return hashOperations.entries(key).keySet();
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
        log.info("%s Delete key: %s".formatted(DEBUG_PREFIX, key));
    }

    @Override
    public void delete(String key, String hashKey) {
        hashOperations.delete(key, hashKey);
        log.info("%s Delete key: %s hashKey: %s".formatted(DEBUG_PREFIX, key, hashKey));
    }

    @Override
    public void delete(String key, List<String> hashKeys) {
        hashOperations.delete(key, hashKeys.toArray());
        log.info("%s Delete key: %s hashKeys: %s".formatted(DEBUG_PREFIX, key, String.valueOf(hashKeys)));
    }
}
