package com.codewithbuff.cauliflower.secure.dao.impl;

import com.codewithbuff.cauliflower.secure.dao.impl.repository.SysRoleRepository;
import com.codewithbuff.cauliflower.secure.dao.service.SysRoleMapper;
import com.codewithbuff.cauliflower.secure.entity.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author 十三月之夜
 * @time 2021/4/23 12:30 上午
 */
@Service
public class SysRoleMapperImpl implements SysRoleMapper {

    @Autowired
    private SysRoleRepository sysRoleRepository;

    @Override
    public Mono<SysRole> insert(SysRole sysRole) {
        return sysRoleRepository.save(sysRole);
    }

    @Override
    public Mono<Void> delete(SysRole sysRole) {
        return sysRoleRepository.deleteById(sysRole.getId());
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return sysRoleRepository.deleteById(id);
    }

    @Override
    public Mono<Void> update(SysRole sysRole) {
        return sysRoleRepository.findById(sysRole.getId())
                .map(old -> {
                    sysRole.withId(old.getId());
                    return sysRole;
                })
                .flatMap(sysRoleRepository::save)
                .then();
    }

    @Override
    public Mono<SysRole> selectById(Long id) {
        return sysRoleRepository.findById(id);
    }

    @Override
    public Mono<SysRole> selectByName(String name) {
        return sysRoleRepository.findByName(name);
    }

    @Override
    public Flux<SysRole> selectAll() {
        return sysRoleRepository.findAll();
    }
}
