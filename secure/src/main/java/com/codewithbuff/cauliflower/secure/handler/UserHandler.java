package com.codewithbuff.cauliflower.secure.handler;

import com.codewithbuff.cauliflower.secure.dao.service.SysRoleMapper;
import com.codewithbuff.cauliflower.secure.dao.service.SysUserMapper;
import com.codewithbuff.cauliflower.secure.dao.service.SysUserRolesMapper;
import com.codewithbuff.cauliflower.secure.dao.service.UserAuthsMapper;
import com.codewithbuff.cauliflower.secure.entity.SysUser;
import com.codewithbuff.cauliflower.secure.entity.UserAuths;
import com.codewithbuff.cauliflower.secure.result.HttpStatusExternal;
import com.codewithbuff.cauliflower.secure.result.ResultBean;
import com.codewithbuff.cauliflower.secure.system.SystemConstant;
import com.codewithbuff.cauliflower.secure.util.BaseUtils;
import com.codewithbuff.cauliflower.secure.util.ReactiveJwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author 十三月之夜
 * @time 2021/5/30 10:47 下午
 * 赶时间，不搞分层了，直接把业务写在这里
 */
@Component
public class UserHandler {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysUserRolesMapper sysUserRolesMapper;

    @Autowired
    private UserAuthsMapper userAuthsMapper;

    @Autowired
    private ReactiveJwtUtils reactiveJwtUtils;

    @Autowired
    private ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    public Mono<ServerResponse> loginIn(ServerRequest serverRequest) {
        Mono<MultiValueMap<String, String>> multiValueMapMono = serverRequest.formData();
        return multiValueMapMono.flatMap(stringStringMultiValueMap -> {
            String identifier = stringStringMultiValueMap.getFirst("identifier");
            String credential = stringStringMultiValueMap.getFirst("credential");
            // 暂时默认只用邮箱
            Mono<UserAuths> userAuthsMono = userAuthsMapper.selectByIdentityTypeAndIdentifier(UserAuths.IDENTITY_TYPE_EMAIL, identifier);
            return userAuthsMono.flatMap(userAuths -> {
                if (userAuths == null) {
                    ResultBean<Void> resultBean = ResultBean.<Void>builder()
                            .code(HttpStatusExternal.USER_NOT_FOUND.value())
                            .msg("用户未注册")
                            .data(null)
                            .timestamp(LocalDateTime.now())
                            .build();
                    return responseBuilder(resultBean.getCode(), null, resultBean);
                } else {
                    long userId = userAuths.getUserId();
                    Mono<SysUser> sysUserMono = sysUserMapper.selectById(userId);
                    return sysUserMono.flatMap(sysUser -> {
                        String salt = sysUser.getSalt();
                        String encrypt = BaseUtils.MD5(credential, salt);
                        if (!encrypt.equals(userAuths.getCredential())) {
                            ResultBean<Void> resultBean = ResultBean.<Void>builder()
                                    .code(HttpStatusExternal.USER_CREDENTIAL_FAILED.value())
                                    .msg("认证失败")
                                    .data(null)
                                    .timestamp(LocalDateTime.now())
                                    .build();
                            return responseBuilder(resultBean.getCode(), null, resultBean);
                        } else {
                            return reactiveJwtUtils.signRefreshToken(sysUser.getUsername())
                                    .flatMap(refreshToken -> {
                                        return reactiveJwtUtils.signAccessToken(refreshToken)
                                                .flatMap(accessToken -> {
                                                    HashMap<String, String> hashMap = new HashMap<>();
                                                    hashMap.put("refreshToken", refreshToken);
                                                    hashMap.put("accessToken", accessToken);
                                                    ResultBean<HashMap<String, String>> resultBean = ResultBean.<HashMap<String, String>>builder()
                                                            .code(HttpStatus.OK.value())
                                                            .msg("")
                                                            .data(hashMap)
                                                            .timestamp(LocalDateTime.now())
                                                            .build();
                                                    return responseBuilder(resultBean.getCode(), null, resultBean);
                                                });
                                    });
                        }
                    });
                }
            });
        });
    }

    public Mono<ServerResponse> loginOut(ServerRequest serverRequest) {
        return Mono.empty();
    }

    public Mono<ServerResponse> signIn(ServerRequest serverRequest) {
        Mono<MultiValueMap<String, String>> multiValueMapMono = serverRequest.formData();
        return multiValueMapMono.flatMap(multiValueMap -> {
            String identifier = multiValueMap.getFirst("identifier");
            String credential = multiValueMap.getFirst("credential");
            String verCode = multiValueMap.getFirst("verCode");
            return reactiveRedisTemplate.opsForValue().get(SystemConstant.VER_CODE_PREFIX + identifier)
                    .map(p1 -> (String) p1)
                    .flatMap(p2 -> {
                        if (!p2.equals(verCode)) {
                            ;
                        } else {
                            SysUser sysUser = SysUser.builder()
                                    .createdTime(LocalDateTime.now())
                                    .updatedTime(LocalDateTime.now())
                                    .state(true)
                                    .flag(1)
                                    // TODO 新的Username设置
                                    .username(UUID.randomUUID().toString())
                                    .salt(BaseUtils.getSalt())
                                    .build();
                            return sysUserMapper.insert(sysUser)
                                    .flatMap(p3 -> {
                                        UserAuths userAuths = UserAuths.builder()
                                                .createdTime(LocalDateTime.now())
                                                .updatedTime(LocalDateTime.now())
                                                .available(true)
                                                .userId(p3.getId())
                                                .identityType(UserAuths.IDENTITY_TYPE_EMAIL)
                                                .identifier(identifier)
                                                .credential(BaseUtils.MD5(credential, p3.getSalt()))
                                                .build();
                                        return userAuthsMapper.insert(userAuths)
                                                .flatMap(p4 -> {
                                                    ;
                                                });
                                    });
                        }
                    });
        });

        return Mono.empty();
    }

    public Mono<ServerResponse> signOut(ServerRequest serverRequest) {
        return Mono.empty();
    }

    public Mono<ServerResponse> accessToken(ServerRequest serverRequest) {
        return Mono.empty();
    }

    private Mono<ServerResponse> responseBuilder(int status, HashMap<String, String> headers, ResultBean<?> resultBean) {
        return ServerResponse
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(resultBean), ResultBean.class);
    }
}
