package com.codewithbuff.cauliflower.secure.dao.impl.repository;

import com.codewithbuff.cauliflower.secure.entity.SysUserRoles;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SysUserRolesRepository extends ReactiveCrudRepository<SysUserRoles, Long> {

    Mono<Void> deleteByUserId(Long userId);

    // @Query("select sur.role_id from sys_user_roles sur where sur.user_id = :userId")
    Flux<Long> findRoleIdByUserId(Long userId);

    @Query("select sr.name from sys_role sr where sr.id in (select sur.role_id from sys_user_roles sur where sur.user_id = :userId)")
    Flux<String> selectRolesNameByUserId(Long userId);

    Flux<SysUserRoles> findByUserId(Long userId);

}