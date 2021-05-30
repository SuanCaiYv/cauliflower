package com.codewithbuff.cauliflower.secure.dao.service;

import com.codewithbuff.cauliflower.secure.entity.SysRole;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author 十三月之夜
 * @time 2021/4/23 12:27 上午
 */
@Service
public interface SysRoleMapper {

    Mono<SysRole> insert(SysRole sysRole);

    Mono<Void> delete(SysRole sysRole);

    Mono<Void> deleteById(Long id);

    Mono<Void> update(SysRole sysRole);

    Mono<SysRole> selectById(Long id);

    Mono<SysRole> selectByName(String name);

    Flux<SysRole> selectAll();
}
