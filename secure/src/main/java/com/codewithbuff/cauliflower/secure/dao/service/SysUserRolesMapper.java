package com.codewithbuff.cauliflower.secure.dao.service;

import com.codewithbuff.cauliflower.secure.entity.SysUserRoles;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author 十三月之夜
 * @time 2021/4/23 12:27 下午
 */
@Service
public interface SysUserRolesMapper {

    Mono<SysUserRoles> insert(SysUserRoles sysUserRoles);

    Mono<Void> deleteById(Long id);

    Mono<Void> deleteUserRole(SysUserRoles sysUserRoles);

    Mono<Void> deleteUserRoles(Long userId);

    Mono<SysUserRoles> selectById(Long id);

    Flux<Long> selectRolesIdByUserId(Long userId);

    Flux<String> selectRolesNameByUserId(Long userId);

    Flux<SysUserRoles> selectUserRolesByUserId(Long userId);
}
