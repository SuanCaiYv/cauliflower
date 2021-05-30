package com.codewithbuff.cauliflower.secure.dao.impl;

import com.codewithbuff.cauliflower.secure.dao.impl.repository.SysUserRolesRepository;
import com.codewithbuff.cauliflower.secure.dao.service.SysUserRolesMapper;
import com.codewithbuff.cauliflower.secure.entity.SysUserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author 十三月之夜
 * @time 2021/4/23 12:47 下午
 */
@Service
public class SysUserRolesMapperImpl implements SysUserRolesMapper {

    @Autowired
    private SysUserRolesRepository sysUserRolesRepository;

    @Override
    public Mono<SysUserRoles> insert(SysUserRoles sysUserRoles) {
        return sysUserRolesRepository.save(sysUserRoles);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return sysUserRolesRepository.deleteById(id);
    }

    @Override
    public Mono<Void> deleteUserRole(SysUserRoles sysUserRoles) {
        return sysUserRolesRepository.deleteById(sysUserRoles.getId());
    }

    @Override
    public Mono<Void> deleteUserRoles(Long userId) {
        return sysUserRolesRepository.deleteByUserId(userId);
    }

    @Override
    public Mono<SysUserRoles> selectById(Long id) {
        return sysUserRolesRepository.findById(id);
    }

    @Override
    public Flux<Long> selectRolesIdByUserId(Long userId) {
        return sysUserRolesRepository.findRoleIdByUserId(userId);
    }

    @Override
    public Flux<String> selectRolesNameByUserId(Long userId) {
        return sysUserRolesRepository.selectRolesNameByUserId(userId);
    }

    @Override
    public Flux<SysUserRoles> selectUserRolesByUserId(Long userId) {
        return sysUserRolesRepository.findByUserId(userId);
    }
}
