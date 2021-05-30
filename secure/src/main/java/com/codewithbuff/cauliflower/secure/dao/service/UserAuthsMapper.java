package com.codewithbuff.cauliflower.secure.dao.service;

import com.codewithbuff.cauliflower.secure.entity.UserAuths;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author 十三月之夜
 * @time 2021/4/23 12:29 下午
 */
@Service
public interface UserAuthsMapper {

    Mono<UserAuths> insert(UserAuths userAuths);

    Mono<Void> deleteById(Long id);

    Mono<Void> delete(UserAuths userAuths);

    Mono<Void> deleteByUserId(Long userId);

    Mono<Void> deleteByIdentityTypeAndIdentifier(String identityType, String identifier);

    Mono<Void> update(UserAuths userAuths);

    Mono<UserAuths> selectById(Long id);

    Mono<UserAuths> selectByIdentityTypeAndIdentifier(String identityType, String identifier);

    Flux<UserAuths> selectByUserId(Long userId);
}
