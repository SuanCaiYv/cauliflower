package com.codewithbuff.cauliflower.secure.handler;

import com.codewithbuff.cauliflower.secure.dao.service.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author 十三月之夜
 * @time 2021/5/30 10:47 下午
 */
@Component
public class UserHandler {

    @Autowired
    private SysUserMapper sysUserMapper;

    public Mono<ServerResponse> loginIn(ServerRequest serverRequest) {
        return Mono.empty();
    }

    public Mono<ServerResponse> loginOut(ServerRequest serverRequest) {
        return Mono.empty();
    }

    public Mono<ServerResponse> signIn(ServerRequest serverRequest) {
        return Mono.empty();
    }

    public Mono<ServerResponse> signOut(ServerRequest serverRequest) {
        return Mono.empty();
    }

    public Mono<ServerResponse> accessToken(ServerRequest serverRequest) {
        return Mono.empty();
    }
}
