package com.codewithbuff.cauliflower.secure.dao.impl;

import com.codewithbuff.cauliflower.secure.dao.impl.repository.SysRoleResourcesRepository;
import com.codewithbuff.cauliflower.secure.dao.service.SysRoleResourcesMapper;
import com.codewithbuff.cauliflower.secure.entity.SysRoleResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author 十三月之夜
 * @time 2021/4/23 12:54 下午
 */
@Service
public class SysRoleResourcesMapperImpl implements SysRoleResourcesMapper {

    @Autowired
    private SysRoleResourcesRepository sysRoleResourcesRepository;

    @Override
    public Mono<SysRoleResources> insert(SysRoleResources sysRoleResources) {
        return sysRoleResourcesRepository.save(sysRoleResources);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return sysRoleResourcesRepository.deleteById(id);
    }

    @Override
    public Mono<Void> deleteRoleResource(SysRoleResources sysRoleResources) {
        return sysRoleResourcesRepository.deleteById(sysRoleResources.getId());
    }

    @Override
    public Mono<Void> deleteRoleResources(Long roleId) {
        return sysRoleResourcesRepository.deleteByRoleId(roleId);
    }

    @Override
    public Mono<SysRoleResources> selectById(Long id) {
        return sysRoleResourcesRepository.findById(id);
    }

    @Override
    public Flux<Long> selectResourceIdByRoleId(Long roleId) {
        return sysRoleResourcesRepository.findResourceIdByRoleId(roleId);
    }

    @Override
    public Flux<String> selectResourceNameByRoleId(Long roleId) {
        return sysRoleResourcesRepository.selectResourceNameByRoleId(roleId);
    }

    @Override
    public Flux<SysRoleResources> selectRoleResourcesByRoleId(Long roleId) {
        return sysRoleResourcesRepository.findByRoleId(roleId);
    }
}
