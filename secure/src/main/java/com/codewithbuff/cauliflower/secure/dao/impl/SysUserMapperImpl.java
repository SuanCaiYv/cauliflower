package com.codewithbuff.cauliflower.secure.dao.impl;

import com.codewithbuff.cauliflower.secure.dao.impl.repository.SysUserRepository;
import com.codewithbuff.cauliflower.secure.dao.service.SysUserMapper;
import com.codewithbuff.cauliflower.secure.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author 十三月之夜
 * @time 2021/4/21 11:50 下午
 */
@Service
public class SysUserMapperImpl implements SysUserMapper {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Override
    public Mono<SysUser> insert(SysUser sysUser) {
        return sysUserRepository.save(sysUser);
    }

    @Override
    public Mono<Void> delete(SysUser sysUser) {
        return sysUserRepository.deleteById(sysUser.getId());
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return sysUserRepository.deleteById(id);
    }

    @Override
    public Mono<Void> deleteByUsername(String username) {
        return sysUserRepository.deleteByUsername(username);
    }

    @Override
    public Mono<Void> update(SysUser sysUser) {
        return sysUserRepository.findById(sysUser.getId())
                .map(old -> {
                    sysUser.withId(old.getId());
                    return sysUser;
                })
                .flatMap(sysUserRepository::save)
                .then();
    }

    @Override
    public Mono<SysUser> selectById(Long id) {
        return sysUserRepository.findById(id);
    }

    @Override
    public Mono<SysUser> selectByUsername(String username) {
        return sysUserRepository.findByUsername(username);
    }
}
