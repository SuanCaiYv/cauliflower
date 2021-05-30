package com.codewithbuff.cauliflower.secure.dao.impl;

import com.codewithbuff.cauliflower.secure.dao.impl.repository.SysResourceRepository;
import com.codewithbuff.cauliflower.secure.dao.service.SysResourceMapper;
import com.codewithbuff.cauliflower.secure.entity.SysResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author 十三月之夜
 * @time 2021/4/23 12:54 下午
 */
@Service
public class SysResourceMapperImpl implements SysResourceMapper {

    @Autowired
    private SysResourceRepository sysResourceRepository;

    @Override
    public Mono<SysResource> insert(SysResource sysResource) {
        return sysResourceRepository.save(sysResource);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return sysResourceRepository.deleteById(id);
    }

    @Override
    public Mono<Void> delete(SysResource sysResource) {
        return sysResourceRepository.deleteById(sysResource.getId());
    }

    @Override
    public Mono<Void> update(SysResource sysResource) {
        return sysResourceRepository.findById(sysResource.getId())
                .map(p -> {
                    sysResource.setId(p.getId());
                    return sysResource;
                })
                .flatMap(sysResourceRepository::save)
                .then();
    }

    @Override
    public Mono<SysResource> selectByName(String name) {
        return sysResourceRepository.findByName(name);
    }

    @Override
    public Mono<SysResource> selectById(Long id) {
        return sysResourceRepository.findById(id);
    }
}
