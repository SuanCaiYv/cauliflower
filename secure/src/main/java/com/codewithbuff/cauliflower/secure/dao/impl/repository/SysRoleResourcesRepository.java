package com.codewithbuff.cauliflower.secure.dao.impl.repository;

import com.codewithbuff.cauliflower.secure.entity.SysRoleResources;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SysRoleResourcesRepository extends ReactiveCrudRepository<SysRoleResources, Long> {

    Mono<Void> deleteByRoleId(Long roleId);

    Flux<Long> findResourceIdByRoleId(Long roleId);

    @Query("select sr.name from sys_resource sr where sr.id in (select srr.resource_id from sys_role_resources srr where srr.role_id = :roleId)")
    Flux<String> selectResourceNameByRoleId(Long roleId);

    Flux<SysRoleResources> findByRoleId(Long roleId);

}