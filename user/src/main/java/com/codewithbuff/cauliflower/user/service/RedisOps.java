package com.codewithbuff.cauliflower.user.service;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author CodeWithBuff(给代码来点Buff)
 * @device MacBookPro
 * @time 2021/6/14 21:02
 */
@Service
public interface RedisOps {

    Boolean follow(Long userId, Long followedUserId);

    List<Long> following(Long userId);

    List<Long> followed(Long userId);

    List<Long> commonFriends(Long userId1, Long userId2);

    List<Long> published(Long userId);

    List<Long> likes(Long userId);

    /**
     * 可能认识
     */
    List<Long> findMore(Long userId);
}
