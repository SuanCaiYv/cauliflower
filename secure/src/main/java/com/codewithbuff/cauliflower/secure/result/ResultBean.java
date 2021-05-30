package com.codewithbuff.cauliflower.secure.result;

import lombok.*;

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
}
