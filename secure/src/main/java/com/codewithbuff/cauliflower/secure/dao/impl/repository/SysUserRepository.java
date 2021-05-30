package com.codewithbuff.cauliflower.secure.dao.impl.repository;

import com.codewithbuff.cauliflower.secure.entity.SysUser;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * @author 十三月之夜
 * @time 2021/4/21 9:40 下午
 */
@Repository
public interface SysUserRepository extends ReactiveCrudRepository<SysUser, Long> {

    Mono<Void> deleteByUsername(String username);

    Mono<SysUser> findByUsername(String username);
}
