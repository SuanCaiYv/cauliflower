package com.codewithbuff.cauliflower.secure.dao.service;

import com.codewithbuff.cauliflower.secure.entity.SysRoleResources;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author 十三月之夜
 * @time 2021/4/23 12:28 下午
 */
@Service
public interface SysRoleResourcesMapper {

    Mono<SysRoleResources> insert(SysRoleResources sysRoleResources);

    Mono<Void> deleteById(Long id);

    Mono<Void> deleteRoleResource(SysRoleResources sysRoleResources);

    Mono<Void> deleteRoleResources(Long roleId);

    Mono<SysRoleResources> selectById(Long id);

    Flux<Long> selectResourceIdByRoleId(Long roleId);

    Flux<String> selectResourceNameByRoleId(Long roleId);

    Flux<SysRoleResources> selectRoleResourcesByRoleId(Long roleId);
}
