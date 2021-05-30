package com.codewithbuff.cauliflower.secure.dao.service;

import com.codewithbuff.cauliflower.secure.entity.SysResource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author 十三月之夜
 * @time 2021/4/23 12:28 下午
 */
@Service
public interface SysResourceMapper {

    Mono<SysResource> insert(SysResource sysResource);

    Mono<Void> deleteById(Long id);

    Mono<Void> delete(SysResource sysResource);

    Mono<Void> update(SysResource sysResource);

    Mono<SysResource> selectByName(String name);

    Mono<SysResource> selectById(Long id);
}
