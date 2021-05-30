package com.codewithbuff.cauliflower.secure.dao.service;

import com.codewithbuff.cauliflower.secure.entity.SysUser;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author 十三月之夜
 * @time 2021/4/21 11:45 下午
 */
@Service
public interface SysUserMapper {

    Mono<SysUser> insert(SysUser sysUser);

    Mono<Void> delete(SysUser sysUser);

    Mono<Void> deleteById(Long id);

    Mono<Void> deleteByUsername(String username);

    Mono<Void> update(SysUser sysUser);

    Mono<SysUser> selectById(Long id);

    Mono<SysUser> selectByUsername(String username);
}
