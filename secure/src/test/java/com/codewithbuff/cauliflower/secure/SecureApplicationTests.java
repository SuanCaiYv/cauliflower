package com.codewithbuff.cauliflower.secure;

import com.codewithbuff.cauliflower.secure.dao.service.UserAuthsMapper;
import com.codewithbuff.cauliflower.secure.entity.UserAuths;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import reactor.core.publisher.Mono;

@SpringBootTest
class SecureApplicationTests {

    @Autowired
    private ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    @Autowired
    private UserAuthsMapper userAuthsMapper;

    @Test
    void contextLoads() {
    }

}
