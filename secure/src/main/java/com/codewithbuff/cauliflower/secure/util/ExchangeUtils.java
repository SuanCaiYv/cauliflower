package com.codewithbuff.cauliflower.secure.util;

import io.netty.buffer.UnpooledByteBufAllocator;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author 十三月之夜
 * @time 2021/5/28 3:06 下午
 */
public class ExchangeUtils {

    private Mono<Void> responseWrite(ServerWebExchange exchange, HttpStatus statusCode,
                                     byte[] content, Map<String, Object> headers) {

        exchange.getResponse().setStatusCode(statusCode);
        if (headers != null) {
            headers.forEach((key, value) -> exchange.getResponse().getHeaders().add(key, String.valueOf(value)));
        }
        if (content != null) {
            return exchange.getResponse().writeWith(Flux.create(sink -> {
                NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(new UnpooledByteBufAllocator(false));
                DataBuffer dataBuffer = nettyDataBufferFactory.wrap(content);
                sink.next(dataBuffer);
                sink.complete();
            }));
        } else {
            return exchange.getResponse().setComplete();
        }
    }
}
