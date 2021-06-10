package com.codewithbuff.cauliflower.secure.handler;

import com.codewithbuff.cauliflower.secure.dao.service.*;
import com.codewithbuff.cauliflower.secure.entity.*;
import com.codewithbuff.cauliflower.secure.exception.UnhandledException;
import com.codewithbuff.cauliflower.secure.result.HttpStatusExternal;
import com.codewithbuff.cauliflower.secure.result.ResultBean;
import com.codewithbuff.cauliflower.secure.system.SystemConstant;
import com.codewithbuff.cauliflower.secure.util.BaseUtils;
import com.codewithbuff.cauliflower.secure.util.JwtUtils;
import com.codewithbuff.cauliflower.secure.util.ReactiveJwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 十三月之夜
 * @time 2021/5/30 10:47 下午
 * 赶时间，不搞分层了，直接把业务写在这里
 */
@Component
public class UserHandler {

    private final Logger logger = LoggerFactory.getLogger(UserHandler.class);

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysUserRolesMapper sysUserRolesMapper;

    @Autowired
    private UserAuthsMapper userAuthsMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private ReactiveJwtUtils reactiveJwtUtils;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    @Autowired
    private TransactionalOperator transactionalOperator;

    /**
     * 登录
     */
    public Mono<ServerResponse> loginIn(ServerRequest serverRequest) {
        return serverRequest.multipartData()
                .flatMap(multiValueMap -> {
                    Part credentialPart = multiValueMap.getFirst("credential");
                    Part identifierPart = multiValueMap.getFirst("identifier");
                    Part usernamePart = multiValueMap.getFirst("username");
                    if ((identifierPart == null && usernamePart == null) || credentialPart == null) {
                        ResultBean<Void> errorBean = ResultBean.requireMoreParam(credentialPart == null ? "credential" : ("username or identifier"));
                        return responseBuilder(errorBean.getCode(), null, errorBean);
                    }
                    return credentialPart.content()
                            .collectList()
                            .flatMap(list -> {
                                StringBuilder credentialBuilder = new StringBuilder();
                                for (DataBuffer dataBuffer : list) {
                                    int i = dataBuffer.readableByteCount();
                                    byte[] bytes = new byte[i];
                                    dataBuffer.read(bytes);
                                    credentialBuilder.append(new String(bytes));
                                }
                                String credential = credentialBuilder.toString();
                                if (usernamePart == null) {
                                    return identifierPart.content()
                                            .collectList()
                                            .map(list1 -> {
                                                StringBuilder identifierBuilder = new StringBuilder();
                                                for (DataBuffer dataBuffer : list1) {
                                                    int i = dataBuffer.readableByteCount();
                                                    byte[] bytes = new byte[i];
                                                    dataBuffer.read(bytes);
                                                    identifierBuilder.append(new String(bytes));
                                                }
                                                return identifierBuilder.toString();
                                            })
                                            .flatMap(identifier -> generateToken(credential, identifier, null));
                                } else {
                                    return usernamePart.content()
                                            .collectList()
                                            .map(list2 -> {
                                                StringBuilder usernameBuilder = new StringBuilder();
                                                for (DataBuffer dataBuffer : list2) {
                                                    int i = dataBuffer.readableByteCount();
                                                    byte[] bytes = new byte[i];
                                                    dataBuffer.read(bytes);
                                                    usernameBuilder.append(new String(bytes));
                                                }
                                                return usernameBuilder.toString();
                                            })
                                            .flatMap(username -> generateToken(credential, null, username));
                                }
                            });
                });
    }

    /**
     * 登出
     */
    public Mono<ServerResponse> loginOut(ServerRequest serverRequest) {
        return Mono.empty();
    }

    /**
     * 注册
     */
    public Mono<ServerResponse> signIn(ServerRequest serverRequest) {
        return serverRequest.multipartData()
                .flatMap(multiValueMap -> {
                    Part credentialPart = multiValueMap.getFirst("credential");
                    Part identifierPart = multiValueMap.getFirst("identifier");
                    Part usernamePart = multiValueMap.getFirst("username");
                    Part verCodePart = multiValueMap.getFirst("verCode");
                    if (identifierPart == null || usernamePart == null || credentialPart == null || verCodePart == null) {
                        ResultBean<Void> errorBean = ResultBean.requireMoreParam(credentialPart == null ? "credential" : (usernamePart == null ? "username" : (identifierPart == null ? "identifier" : "verCode")));
                        return responseBuilder(errorBean.getCode(), null, errorBean);
                    }
                    return credentialPart.content()
                            .collectList()
                            .flatMap(list1 -> {
                                StringBuilder credentialBuilder = new StringBuilder();
                                for (DataBuffer dataBuffer : list1) {
                                    int i = dataBuffer.readableByteCount();
                                    byte[] bytes = new byte[i];
                                    dataBuffer.read(bytes);
                                    credentialBuilder.append(new String(bytes));
                                }
                                String credential = credentialBuilder.toString();
                                return identifierPart.content()
                                        .collectList()
                                        .flatMap(list2 -> {
                                            StringBuilder identifierBuilder = new StringBuilder();
                                            for (DataBuffer dataBuffer : list2) {
                                                int i = dataBuffer.readableByteCount();
                                                byte[] bytes = new byte[i];
                                                dataBuffer.read(bytes);
                                                identifierBuilder.append(new String(bytes));
                                            }
                                            String identifier = identifierBuilder.toString();
                                            return usernamePart.content()
                                                    .collectList()
                                                    .flatMap(list3 -> {
                                                        StringBuilder usernameBuilder = new StringBuilder();
                                                        for (DataBuffer dataBuffer : list3) {
                                                            int i = dataBuffer.readableByteCount();
                                                            byte[] bytes = new byte[i];
                                                            dataBuffer.read(bytes);
                                                            usernameBuilder.append(new String(bytes));
                                                        }
                                                        String username = usernameBuilder.toString();
                                                        return verCodePart.content()
                                                                .collectList()
                                                                .flatMap(list4 -> {
                                                                    StringBuilder verCodeBuilder = new StringBuilder();
                                                                    for (DataBuffer dataBuffer : list4) {
                                                                        int i = dataBuffer.readableByteCount();
                                                                        byte[] bytes = new byte[i];
                                                                        dataBuffer.read(bytes);
                                                                        verCodeBuilder.append(new String(bytes));
                                                                    }
                                                                    String verCode = verCodeBuilder.toString();
                                                                    logger.info("identifier: {}, username: {}, credential: {}, verCode: {}", identifier, username, credential, verCode);
                                                                    return reactiveRedisTemplate.opsForValue().get(SystemConstant.VER_CODE_PREFIX + identifier)
                                                                            .map(tmp -> tmp + "")
                                                                            .flatMap(verCodeCache -> {
                                                                                if (!verCodeCache.equals(verCode)) {
                                                                                    ResultBean<Void> errorBean = ResultBean.<Void>builder()
                                                                                            .code(HttpStatusExternal.USER_CREDENTIAL_FAILED.value())
                                                                                            .msg("验证码错误")
                                                                                            .data(null)
                                                                                            .timestamp(LocalDateTime.now())
                                                                                            .build();
                                                                                    return responseBuilder(errorBean.getCode(), null, errorBean);
                                                                                }
                                                                                return userAuthsMapper.selectByIdentityTypeAndIdentifier(UserAuths.IDENTITY_TYPE_EMAIL, identifier)
                                                                                        .flatMap(userAuths1 -> {
                                                                                            ResultBean<Void> errorBean = ResultBean.<Void>builder()
                                                                                                    .code(HttpStatusExternal.USER_CREDENTIAL_FAILED.value())
                                                                                                    .msg("邮箱已存在")
                                                                                                    .data(null)
                                                                                                    .timestamp(LocalDateTime.now())
                                                                                                    .build();
                                                                                            return responseBuilder(errorBean.getCode(), null, errorBean);
                                                                                        })
                                                                                        .switchIfEmpty(userAuthsMapper.selectByIdentityTypeAndIdentifier(UserAuths.IDENTITY_TYPE_USERNAME, username)
                                                                                                .flatMap(userAuths2 -> {
                                                                                                    ResultBean<Void> errorBean = ResultBean.<Void>builder()
                                                                                                            .code(HttpStatusExternal.USER_CREDENTIAL_FAILED.value())
                                                                                                            .msg("用户名已存在")
                                                                                                            .data(null)
                                                                                                            .timestamp(LocalDateTime.now())
                                                                                                            .build();
                                                                                                    return responseBuilder(errorBean.getCode(), null, errorBean);
                                                                                                })
                                                                                                .switchIfEmpty(ResultBean.createReactiveResultBean().flatMap(resultBean -> {
                                                                                                    logger.info("start signin...");
                                                                                                    SysUser sysUser = SysUser.builder()
                                                                                                            .createdTime(LocalDateTime.now())
                                                                                                            .updatedTime(LocalDateTime.now())
                                                                                                            .state(true)
                                                                                                            .flag(1)
                                                                                                            .username(BaseUtils.generateUsername())
                                                                                                            .salt(BaseUtils.getSalt())
                                                                                                            .build();
                                                                                                    return sysUserMapper.insert(sysUser)
                                                                                                            .flatMap(sysUser0 -> {
                                                                                                                UserAuths userAuthsIdentifier = UserAuths.builder()
                                                                                                                        .createdTime(LocalDateTime.now())
                                                                                                                        .updatedTime(LocalDateTime.now())
                                                                                                                        .available(true)
                                                                                                                        .userId(sysUser0.getId())
                                                                                                                        .identityType(UserAuths.IDENTITY_TYPE_EMAIL)
                                                                                                                        .identifier(identifier)
                                                                                                                        .credential(BaseUtils.MD5(credential, sysUser0.getSalt()))
                                                                                                                        .build();
                                                                                                                UserAuths userAuthsUsername = UserAuths.builder()
                                                                                                                        .createdTime(LocalDateTime.now())
                                                                                                                        .updatedTime(LocalDateTime.now())
                                                                                                                        .available(true)
                                                                                                                        .userId(sysUser0.getId())
                                                                                                                        .identityType(UserAuths.IDENTITY_TYPE_USERNAME)
                                                                                                                        .identifier(username)
                                                                                                                        .credential(userAuthsIdentifier.getCredential())
                                                                                                                        .build();
                                                                                                                logger.info("start insert sys_user: {}", sysUser);
                                                                                                                return userAuthsMapper.insert(userAuthsIdentifier)
                                                                                                                        .flatMap(userAuths1 -> {
                                                                                                                            return userAuthsMapper.insert(userAuthsUsername)
                                                                                                                                    .flatMap(userAuth2 -> sysRoleMapper.selectByName(SysRole.BALSFJORD)
                                                                                                                                            .flatMap(sysRole -> {
                                                                                                                                                SysUserRoles sysUserRoles = SysUserRoles.builder()
                                                                                                                                                        .createdTime(LocalDateTime.now())
                                                                                                                                                        .updatedTime(LocalDateTime.now())
                                                                                                                                                        .available(true)
                                                                                                                                                        .userId(sysUser0.getId())
                                                                                                                                                        .roleId(sysRole.getId())
                                                                                                                                                        .build();
                                                                                                                                                logger.info("start insert sys_user_roles: {}", sysUserRoles);
                                                                                                                                                return sysUserRolesMapper.insert(sysUserRoles)
                                                                                                                                                        .flatMap(ignored -> {
                                                                                                                                                            UserInfo userInfo = UserInfo.builder()
                                                                                                                                                                    .userId(sysUser.getId())
                                                                                                                                                                    .username(username)
                                                                                                                                                                    .email(identifier)
                                                                                                                                                                    .birthDate(LocalDateTime.of(LocalDate.of(2000, 4, 12), LocalTime.of(4, 30)))
                                                                                                                                                                    .build();
                                                                                                                                                            logger.info("start insert user_info: {}", userInfo);
                                                                                                                                                            return userInfoMapper.insert(userInfo)
                                                                                                                                                                    .flatMap(ignored0 -> {
                                                                                                                                                                        return reactiveJwtUtils.signRefreshToken(sysUser.getUsername())
                                                                                                                                                                                .flatMap(refreshToken -> {
                                                                                                                                                                                    return reactiveJwtUtils.signAccessToken(refreshToken)
                                                                                                                                                                                            .flatMap(accessToken -> {
                                                                                                                                                                                                HashMap<String, String> hashMap = new HashMap<>();
                                                                                                                                                                                                hashMap.put("REFRESH_TOKEN", refreshToken);
                                                                                                                                                                                                hashMap.put("ACCESS_TOKEN", accessToken);
                                                                                                                                                                                                resultBean.setCode(HttpStatus.OK.value());
                                                                                                                                                                                                resultBean.setMsg("");
                                                                                                                                                                                                resultBean.setData(hashMap);
                                                                                                                                                                                                resultBean.setTimestamp(LocalDateTime.now());
                                                                                                                                                                                                return responseBuilder(resultBean.getCode(), null, resultBean);
                                                                                                                                                                                            });
                                                                                                                                                                                });
                                                                                                                                                                    });
                                                                                                                                                        });
                                                                                                                                            }));
                                                                                                                        });
                                                                                                            });
                                                                                                }))
                                                                                        );
                                                                            })
                                                                            .switchIfEmpty(ResultBean.createReactiveResultBean().flatMap(errorResultBean -> {
                                                                                errorResultBean = errorResultBean
                                                                                        .withCode(HttpStatus.BAD_REQUEST.value())
                                                                                        .withMsg("验证码错误")
                                                                                        .withData(null)
                                                                                        .withTimestamp(LocalDateTime.now());
                                                                                return responseBuilder(errorResultBean.getCode(), null, errorResultBean);
                                                                            }));
                                                                });
                                                    });
                                        });
                            });
                })
                .as(transactionalOperator::transactional);
    }

    private void f() {
        throw new RuntimeException("aaa");
    }

    /**
     * 注销
     */
    public Mono<ServerResponse> signOut(ServerRequest serverRequest) {
        return Mono.empty();
    }

    /**
     * 签发新的访问令牌
     */
    public Mono<ServerResponse> accessToken(ServerRequest serverRequest) {
        Mono<MultiValueMap<String, String>> multiValueMapMono = serverRequest.formData();
        return multiValueMapMono.flatMap(multiValueMap -> {
            String refreshToken = multiValueMap.getFirst(SystemConstant.REFRESH_TOKEN);
            return reactiveJwtUtils.verifyRefreshToken(refreshToken)
                    .flatMap(decodedAccessToken -> reactiveJwtUtils.signAccessToken(refreshToken)
                            .flatMap(newAccessToken -> {
                                ResultBean<String> resultBean = ResultBean.<String>builder()
                                        .code(HttpStatus.OK.value())
                                        .msg("")
                                        .data(newAccessToken)
                                        .timestamp(LocalDateTime.now())
                                        .build();
                                return responseBuilder(resultBean.getCode(), null, resultBean);
                            }))
                    .onErrorResume(throwable -> {
                        UnhandledException unhandledException = (UnhandledException) throwable;
                        return responseBuilder(unhandledException);
                    });
        });
    }

    private Mono<ServerResponse> responseBuilder(UnhandledException unhandledException) {
        ResultBean<Void> errorBean = ResultBean.<Void>builder()
                .code(unhandledException.getErrCode())
                .msg(unhandledException.getErrMsg())
                .data(null)
                .timestamp(unhandledException.getErrTime())
                .build();
        return responseBuilder(unhandledException.getErrCode(), null, errorBean);
    }

    private Mono<ServerResponse> responseBuilder(int status, HashMap<String, String> headers, ResultBean<?> resultBean) {
        ServerResponse.BodyBuilder bodyBuilder = ServerResponse
                .status(status)
                .contentType(MediaType.APPLICATION_JSON);
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                bodyBuilder = bodyBuilder.header(entry.getKey(), entry.getValue());
            }
        }
        return bodyBuilder.body(Mono.just(resultBean), ResultBean.class);
    }

    private Mono<ServerResponse> generateToken(String credential, String identifier, String username) {
        if (username != null) {
            return userAuthsMapper.selectByIdentityTypeAndIdentifier(UserAuths.IDENTITY_TYPE_USERNAME, username)
                    .flatMap(userAuths -> sysUserMapper.selectById(userAuths.getUserId())
                            .flatMap(sysUser -> reactiveJwtUtils.signRefreshToken(sysUser.getUsername())
                                    .flatMap(refreshToken -> reactiveJwtUtils.signAccessToken(refreshToken)
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
                                            })
                                    )))
                    .switchIfEmpty(ResultBean.createReactiveResultBean()
                            .flatMap(resultBean -> {
                                resultBean = resultBean
                                        .withCode(HttpStatusExternal.USER_NOT_FOUND.value())
                                        .withMsg("用户不存在")
                                        .withData(null)
                                        .withTimestamp(LocalDateTime.now());
                                return responseBuilder(resultBean.getCode(), null, resultBean);
                            })
                    );
        } else if (identifier != null) {
            return userAuthsMapper.selectByIdentityTypeAndIdentifier(UserAuths.IDENTITY_TYPE_EMAIL, username)
                    .flatMap(userAuths -> sysUserMapper.selectById(userAuths.getUserId())
                            .flatMap(sysUser -> reactiveJwtUtils.signRefreshToken(sysUser.getUsername())
                                    .flatMap(refreshToken -> reactiveJwtUtils.signAccessToken(refreshToken)
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
                                            })
                                    )))
                    .switchIfEmpty(ResultBean.createReactiveResultBean()
                            .flatMap(resultBean -> {
                                resultBean = resultBean
                                        .withCode(HttpStatusExternal.USER_NOT_FOUND.value())
                                        .withMsg("用户不存在")
                                        .withData(null)
                                        .withTimestamp(LocalDateTime.now());
                                return responseBuilder(resultBean.getCode(), null, resultBean);
                            })
                    );
        } else {
            ResultBean<Void> resultBean = ResultBean.requireMoreParam("all");
            return responseBuilder(resultBean.getCode(), null, resultBean);
        }
    }
}
