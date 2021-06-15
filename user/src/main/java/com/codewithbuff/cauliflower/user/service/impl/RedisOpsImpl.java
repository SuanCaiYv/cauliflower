package com.codewithbuff.cauliflower.user.service.impl;

import com.codewithbuff.cauliflower.user.service.RedisOps;
import com.codewithbuff.cauliflower.user.system.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author CodeWithBuff(给代码来点Buff)
 * @device MacBookPro
 * @time 2021/6/14 21:02
 */
@Service
public class RedisOpsImpl implements RedisOps {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Boolean follow(Long userId, Long followedUserId) {
        redisTemplate.opsForZSet().add(SystemConstant.FOLLOWING_USER_PREFIX + userId, followedUserId, System.currentTimeMillis());
        redisTemplate.opsForZSet().add(SystemConstant.FOLLOWED_USER_PREFIX + followedUserId, userId, System.currentTimeMillis());
        return true;
    }

    @Override
    public List<Long> following(Long userId) {
        Set<Object> range = redisTemplate.opsForZSet().range(SystemConstant.FOLLOWED_USER_PREFIX + userId, 0, Integer.MAX_VALUE);
        return null;
    }

    @Override
    public List<Long> followed(Long userId) {
        return null;
    }

    @Override
    public List<Long> commonFriends(Long userId1, Long userId2) {
        return null;
    }

    @Override
    public List<Long> published(Long userId) {
        return null;
    }

    @Override
    public List<Long> likes(Long userId) {
        return null;
    }

    @Override
    public List<Long> findMore(Long userId) {
        return null;
    }
}
