package com.codewithbuff.cauliflower.secure.dao.impl.repository;

import com.codewithbuff.cauliflower.secure.entity.UserAuths;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserAuthsRepository extends ReactiveCrudRepository<UserAuths, Long> {

    Mono<Void> deleteByUserId(Long userId);

    Mono<Void> deleteByIdentityTypeAndIdentifier(String identityType, String identifier);

    Mono<UserAuths> findByIdentityTypeAndIdentifier(String identityType, String identifier);

    Flux<UserAuths> findByUserId(Long userId);
}