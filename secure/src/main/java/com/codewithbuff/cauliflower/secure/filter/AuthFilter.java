package com.codewithbuff.cauliflower.secure.filter;

import com.codewithbuff.cauliflower.secure.exception.UnhandledException;
import com.codewithbuff.cauliflower.secure.util.ExchangeUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usthe.sureness.mgt.SurenessSecurityManager;
import com.usthe.sureness.processor.exception.SurenessAuthenticationException;
import com.usthe.sureness.subject.SubjectSum;
import com.usthe.sureness.util.SurenessContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author 十三月之夜
 * @time 2021/5/29 3:42 下午
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    private final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        try {
            SubjectSum subject = SurenessSecurityManager.getInstance().checkIn(request);
            // 绑定当前Subject到线程上下文中，但是要记得在最后移除
            if (subject != null) {
                SurenessContextHolder.bindSubject(subject);
            }
        } catch (SurenessAuthenticationException surenessAuthenticationException) {
            try {
                UnhandledException unhandledException = objectMapper.readValue(surenessAuthenticationException.getMessage(), UnhandledException.class);
                logger.warn("用户认证失败: {}", unhandledException.getErrMsg());
                return ExchangeUtils.responseWrite(exchange, unhandledException.getErrCode(), unhandledException.getErrMsg().getBytes(), null);
            } catch (JsonProcessingException ignored) {
                ;
            }
        }
        return chain.filter(exchange).doFinally(x -> SurenessContextHolder.unbindSubject());
    }

    @Override
    public int getOrder() {
        return -3;
    }
}
