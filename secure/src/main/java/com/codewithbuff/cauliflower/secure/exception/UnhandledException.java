package com.codewithbuff.cauliflower.secure.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author 十三月之夜
 * @time 2021/4/22 1:07 下午
 */
@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UnhandledException extends Exception {

    private int errCode;

    private String errMsg;

    private StackTraceElement errStackTraceElement;

    private LocalDateTime errTime;

    public String toJsonString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException ignored) {
            ;
        }
        return "{}";
    }
}
