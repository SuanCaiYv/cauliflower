package com.codewithbuff.cauliflower.secure.dao.impl.repository;

import com.codewithbuff.cauliflower.secure.entity.SysRole;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface SysRoleRepository extends ReactiveCrudRepository<SysRole, Long> {

    Mono<SysRole> findByName(String name);

}