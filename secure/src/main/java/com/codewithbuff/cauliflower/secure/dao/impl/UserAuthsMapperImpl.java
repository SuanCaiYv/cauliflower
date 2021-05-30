package com.codewithbuff.cauliflower.secure.dao.impl;

import com.codewithbuff.cauliflower.secure.dao.impl.repository.UserAuthsRepository;
import com.codewithbuff.cauliflower.secure.dao.service.UserAuthsMapper;
import com.codewithbuff.cauliflower.secure.entity.UserAuths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author 十三月之夜
 * @time 2021/4/23 1:04 下午
 */
@Service
public class UserAuthsMapperImpl implements UserAuthsMapper {

    @Autowired
    private UserAuthsRepository userAuthsRepository;

    @Override
    public Mono<UserAuths> insert(UserAuths userAuths) {
        return userAuthsRepository.save(userAuths);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return userAuthsRepository.deleteById(id);
    }

    @Override
    public Mono<Void> delete(UserAuths userAuths) {
        return userAuthsRepository.deleteById(userAuths.getId());
    }

    @Override
    public Mono<Void> deleteByUserId(Long userId) {
        return userAuthsRepository.deleteByUserId(userId);
    }

    @Override
    public Mono<Void> deleteByIdentityTypeAndIdentifier(String identityType, String identifier) {
        return userAuthsRepository.deleteByIdentityTypeAndIdentifier(identityType, identifier);
    }

    @Override
    public Mono<Void> update(UserAuths userAuths) {
        return userAuthsRepository.findById(userAuths.getId())
                .map(p -> {
                    userAuths.setId(p.getId());
                    return userAuths;
                })
                .flatMap(userAuthsRepository::save)
                .then();
    }

    @Override
    public Mono<UserAuths> selectById(Long id) {
        return userAuthsRepository.findById(id);
    }

    @Override
    public Mono<UserAuths> selectByIdentityTypeAndIdentifier(String identityType, String identifier) {
        return userAuthsRepository.findByIdentityTypeAndIdentifier(identityType, identifier);
    }

    @Override
    public Flux<UserAuths> selectByUserId(Long userId) {
        return userAuthsRepository.findByUserId(userId);
    }
}
