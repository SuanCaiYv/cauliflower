package com.codewithbuff.cauliflower.secure.dao.impl;

import com.codewithbuff.cauliflower.secure.dao.impl.repository.UserInfoRepository;
import com.codewithbuff.cauliflower.secure.dao.service.UserInfoMapper;
import com.codewithbuff.cauliflower.secure.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author CodeWithBuff(给代码来点Buff)
 * @device iMacPro
 * @time 2021/6/10 2:25 下午
 */
@Service
public class UserInfoMapperImpl implements UserInfoMapper {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public Mono<UserInfo> insert(UserInfo userInfo) {
        return userInfoRepository.save(userInfo);
    }
}
