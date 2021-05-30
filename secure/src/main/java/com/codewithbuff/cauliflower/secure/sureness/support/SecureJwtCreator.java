package com.codewithbuff.cauliflower.secure.sureness.support;

import com.codewithbuff.cauliflower.secure.system.SystemConstant;
import com.usthe.sureness.subject.Subject;
import com.usthe.sureness.subject.SubjectCreate;
import com.usthe.sureness.subject.support.JwtSubject;
import com.usthe.sureness.subject.support.SinglePrincipalMap;
import com.usthe.sureness.util.JsonWebTokenUtil;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author 十三月之夜
 * @time 2021/5/29 3:27 下午
 */
@Component
public class SecureJwtCreator implements SubjectCreate {

    @Override
    public boolean canSupportSubject(Object requestObj) {
        if (requestObj instanceof ServerHttpRequest) {
            ServerHttpRequest request = (ServerHttpRequest) requestObj;
            String authorization = request.getHeaders().getFirst(SystemConstant.AUTHORIZATION);
            if (authorization != null && authorization.startsWith(SystemConstant.BEARER)) {
                String jwtValue = authorization.replace(SystemConstant.BEARER, "").trim();
                return !JsonWebTokenUtil.isNotJsonWebToken(jwtValue);
            }
        }
        return false;
    }

    @Override
    public Subject createSubject(Object requestObj) {
        ServerHttpRequest request = (ServerHttpRequest) requestObj;
        String authorization = request.getHeaders().getFirst(SystemConstant.AUTHORIZATION);
        if (authorization != null && authorization.startsWith(SystemConstant.BEARER)) {
            String jwtValue = authorization.replace(SystemConstant.BEARER, "").trim();
            if (JsonWebTokenUtil.isNotJsonWebToken(jwtValue)) {
                return null;
            }
            InetSocketAddress remoteAddress = request.getRemoteAddress();
            String remoteHost = remoteAddress == null ? "" : remoteAddress.getHostString() + remoteAddress.getPort();
            String requestUri = request.getPath().value();
            String requestType = request.getMethodValue();
            String targetResource = requestUri.concat("===").concat(requestType.toLowerCase());
            return JwtSubject.builder(jwtValue)
                    .setRemoteHost(remoteHost)
                    .setTargetResource(targetResource)
                    .setPrincipal(UUID.randomUUID().toString())
                    .setOwnRoles(new ArrayList<>(2))
                    .setPrincipalMap(new SinglePrincipalMap(new HashMap<>()))
                    .build();
        }
        return null;
    }
}
