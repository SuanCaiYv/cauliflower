package com.codewithbuff.cauliflower.secure.dao.service;

import com.codewithbuff.cauliflower.secure.entity.UserInfo;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author CodeWithBuff(给代码来点Buff)
 * @device iMacPro
 * @time 2021/6/10 2:17 下午
 */
@Service
public interface UserInfoMapper {

    Mono<UserInfo> insert(UserInfo userInfo);
}
