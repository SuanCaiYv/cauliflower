package com.codewithbuff.cauliflower.secure.result;

import lombok.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * @author 十三月之夜
 * @time 2021/4/22 5:13 下午
 */
@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultBean<T> {

    private int code;

    private String msg;

    private T data;

    private LocalDateTime timestamp;

    public static ResultBean<Void> requireMoreParam(String paramName) {
        ResultBean<Void> resultBean = new ResultBean<>();
        resultBean.setCode(HttpStatusExternal.CLIENT_PARAMETER_MISSING.value());
        resultBean.setData(null);
        resultBean.setMsg("parameter: " + paramName + " missing");
        resultBean.setTimestamp(LocalDateTime.now());
        return resultBean;
    }

    /**
     * 创建一个Hot版ResultBean
     * @return new ResultBean
     */
    public static <T> Mono<ResultBean<T>> createReactiveResultBean() {
        return Mono.just(new ResultBean<>());
    }
}
