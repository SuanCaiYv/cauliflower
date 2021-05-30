package com.codewithbuff.cauliflower.secure.filter;

import com.usthe.sureness.mgt.SurenessSecurityManager;
import com.usthe.sureness.processor.exception.*;
import com.usthe.sureness.subject.SubjectSum;
import com.usthe.sureness.util.SurenessContextHolder;
import io.netty.buffer.UnpooledByteBufAllocator;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

/**
 * @author 十三月之夜
 * @time 2021/5/29 3:42 下午
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        try {
            SubjectSum subject = SurenessSecurityManager.getInstance().checkIn(request);
            // 绑定当前Subject到线程上下文中，但是要记得在最后移除
            if (subject != null) {
                SurenessContextHolder.bindSubject(subject);
            }
        } catch (IncorrectCredentialsException | UnknownAccountException | ExpiredCredentialsException e1) {
            return responseWrite(exchange, HttpStatus.UNAUTHORIZED, e1.getMessage(), null);
        } catch (NeedDigestInfoException e5) {
            return responseWrite(exchange, HttpStatus.UNAUTHORIZED,
                    "try once again with digest auth information",
                    Collections.singletonMap("WWW-Authenticate", e5.getAuthenticate()));
        } catch (UnauthorizedException e5) {
            return responseWrite(exchange, HttpStatus.FORBIDDEN, e5.getMessage(), null);
        } catch (RuntimeException e) {
            return responseWrite(exchange, HttpStatus.FORBIDDEN, e.getMessage(), null);
        }

        return chain.filter(exchange).doFinally(x -> SurenessContextHolder.unbindSubject());
    }

    @Override
    public int getOrder() {
        return -3;
    }

    /**
     * write response json data
     *
     * @param exchange   content
     * @param statusCode statusCode
     * @param message    message
     */
    private Mono<Void> responseWrite(ServerWebExchange exchange, HttpStatus statusCode,
                                     String message, Map<String, String> headers) {

        exchange.getResponse().setStatusCode(statusCode);
        if (headers != null) {
            headers.forEach((key, value) -> exchange.getResponse().getHeaders().add(key, value));
        }
        if (message != null) {
            return exchange.getResponse().writeWith(Flux.create(sink -> {
                NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(new UnpooledByteBufAllocator(false));
                DataBuffer dataBuffer = nettyDataBufferFactory.wrap(message.getBytes(StandardCharsets.UTF_8));
                sink.next(dataBuffer);
                sink.complete();
            }));
        } else {
            return exchange.getResponse().setComplete();
        }
    }
}
