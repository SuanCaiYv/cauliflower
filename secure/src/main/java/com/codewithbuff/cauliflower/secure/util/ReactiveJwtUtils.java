package com.codewithbuff.cauliflower.secure.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.codewithbuff.cauliflower.secure.dao.service.SysUserMapper;
import com.codewithbuff.cauliflower.secure.dao.service.SysUserRolesMapper;
import com.codewithbuff.cauliflower.secure.exception.UnhandledException;
import com.codewithbuff.cauliflower.secure.result.HttpStatusExternal;
import com.codewithbuff.cauliflower.secure.system.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

/**
 * @author 十三月之夜
 * @time 2021/5/30 11:07 下午
 */
@Component
public class ReactiveJwtUtils {

    @Autowired
    private ReactiveRedisTemplate<String, Object> redisTemplate;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserRolesMapper sysUserRolesMapper;

    private Mono<String> createAccessToken(DecodedJWT decodedJWT) {
        String jwtId = decodedJWT.getId();
        return redisTemplate.opsForValue().get(jwtId)
                .flatMap(secretKeyObj -> {
                    if (secretKeyObj == null) {
                        return Mono
                                .error(UnhandledException.builder()
                                        .errCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                        .errMsg("签发访问令牌失败")
                                        .errStackTraceElement(Thread.currentThread().getStackTrace()[1])
                                        .errTime(LocalDateTime.now())
                                        .build()
                                );
                    }
                    String secretKey = secretKeyObj.toString();
                    String username = decodedJWT.getClaim(SystemConstant.TOKEN_USERNAME).asString();
                    return sysUserMapper.selectByUsername(username)
                            .flatMap(sysUser -> {
                                long userId = sysUser.getId();
                                return sysUserRolesMapper.selectRolesNameByUserId(userId)
                                        .collectList()
                                        .map(roleNames -> {
                                            Algorithm algorithm = Algorithm.HMAC256(secretKey);
                                            Date issueDate = new Date();
                                            Date expiredDate = new Date(System.currentTimeMillis() + SystemConstant.ACCESS_TOKEN_EXPIRED_TIME);
                                            String accessToken = JWT.create()
                                                    .withIssuedAt(issueDate)
                                                    .withExpiresAt(expiredDate)
                                                    .withIssuer(SystemConstant.ISSUER)
                                                    .withAudience(username)
                                                    .withClaim(SystemConstant.TOKEN_USERNAME, username)
                                                    .withClaim(SystemConstant.TOKEN_USER_ID, userId)
                                                    .withClaim(SystemConstant.TOKEN_USER_ROLES, roleNames)
                                                    .sign(algorithm);
                                            return accessToken;
                                        });

                            });
                });
    }

    private Mono<DecodedJWT> decodeJWT(String jwt) {
        return Mono.create(monoSink -> {
            if (jwt == null) {
                monoSink.error(UnhandledException.builder()
                        .errCode(HttpStatus.BAD_REQUEST.value())
                        .errMsg("令牌不可为空")
                        .errStackTraceElement(Thread.currentThread().getStackTrace()[1])
                        .errTime(LocalDateTime.now())
                        .build());
            } else {
                DecodedJWT decodedJWT = JWT.decode(jwt);
                Date issueDate = decodedJWT.getIssuedAt();
                Date expiredDate = decodedJWT.getExpiresAt();
                String issuer = decodedJWT.getIssuer();
                if (issueDate.after(new Date()) || issueDate.after(expiredDate)) {
                    monoSink.error(
                            UnhandledException.builder()
                                    .errCode(HttpStatusExternal.TOKEN_SIGN_DATE_ERROR.value())
                                    .errMsg("令牌签发日期异常")
                                    .errStackTraceElement(Thread.currentThread().getStackTrace()[1])
                                    .errTime(LocalDateTime.now())
                                    .build()
                    );
                }
                if (expiredDate.before(new Date())) {
                    monoSink.error(
                            UnhandledException.builder()
                                    .errCode(HttpStatusExternal.TOKEN_EXPIRED.value())
                                    .errMsg("令牌已过期")
                                    .errStackTraceElement(Thread.currentThread().getStackTrace()[1])
                                    .errTime(LocalDateTime.now())
                                    .build()
                    );
                }
                if (!SystemConstant.ISSUER.equals(issuer)) {
                    monoSink.error(
                            UnhandledException.builder()
                                    .errCode(HttpStatusExternal.TOKEN_ISSUER_FAILED.value())
                                    .errMsg("此令牌不属于本系统")
                                    .errStackTraceElement(Thread.currentThread().getStackTrace()[1])
                                    .errTime(LocalDateTime.now())
                                    .build()
                    );
                }
                monoSink.success(decodedJWT);
            }
        });
    }

    public Mono<DecodedJWT> verifyRefreshToken(String refreshToken) {
        return decodeJWT(refreshToken)
                .flatMap(decodedJWT -> {
                    String audience = decodedJWT.getAudience().get(0);
                    String jwtId = decodedJWT.getId();
                    String username = decodedJWT.getClaim(SystemConstant.TOKEN_USERNAME).asString();
                    if (!SystemConstant.ACCESS_TOKEN.equals(audience)) {
                        return Mono.error(
                                UnhandledException.builder()
                                        .errCode(HttpStatusExternal.TOKEN_AUDIENCE_FAILED.value())
                                        .errMsg("此令牌不是刷新令牌")
                                        .errStackTraceElement(Thread.currentThread().getStackTrace()[1])
                                        .errTime(LocalDateTime.now())
                                        .build()
                        );
                    }
                    return redisTemplate.opsForValue().get(jwtId)
                            .flatMap(secretKeyObj -> {
                                if (secretKeyObj == null) {
                                    return Mono.error(
                                            UnhandledException.builder()
                                                    .errCode(HttpStatusExternal.TOKEN_EXCLUDED.value())
                                                    .errMsg("您已被强制下线")
                                                    .errStackTraceElement(Thread.currentThread().getStackTrace()[1])
                                                    .errTime(LocalDateTime.now())
                                                    .build()
                                    );
                                }
                                String secretKey = (String) secretKeyObj;
                                Algorithm algorithm = Algorithm.HMAC256(secretKey);
                                JWTVerifier jwtVerifier = JWT.require(algorithm)
                                        .withJWTId(jwtId)
                                        .withClaim(SystemConstant.TOKEN_USERNAME, username)
                                        .build();
                                try {
                                    jwtVerifier.verify(refreshToken);
                                } catch (AlgorithmMismatchException e) {
                                    return Mono.error(UnhandledException.builder()
                                            .errCode(HttpStatusExternal.TOKEN_ALGORITHM_FAILED.value())
                                            .errMsg("令牌加密算法不匹配")
                                            .errStackTraceElement(Thread.currentThread().getStackTrace()[1])
                                            .errTime(LocalDateTime.now())
                                            .build()
                                    );
                                } catch (SignatureVerificationException e) {
                                    return Mono.error(UnhandledException.builder()
                                            .errCode(HttpStatusExternal.TOKEN_SIGNATURE_FAILED.value())
                                            .errMsg("签名非法")
                                            .errStackTraceElement(Thread.currentThread().getStackTrace()[1])
                                            .errTime(LocalDateTime.now())
                                            .build());
                                } catch (TokenExpiredException e) {
                                    return Mono.error(UnhandledException.builder()
                                            .errCode(HttpStatusExternal.TOKEN_EXPIRED.value())
                                            .errMsg("令牌已过期")
                                            .errStackTraceElement(Thread.currentThread().getStackTrace()[1])
                                            .errTime(LocalDateTime.now())
                                            .build());
                                } catch (InvalidClaimException e) {
                                    return Mono.error(UnhandledException.builder()
                                            .errCode(HttpStatusExternal.TOKEN_CLAIM_MATCH_FAILED.value())
                                            .errMsg("声明错误")
                                            .errStackTraceElement(Thread.currentThread().getStackTrace()[1])
                                            .errTime(LocalDateTime.now())
                                            .build());
                                }
                                return Mono.just(decodedJWT);
                            });
                });
    }

    public Mono<DecodedJWT> verifyAccessToken(String accessToken) {
        return decodeJWT(accessToken)
                .flatMap(decodedJWT -> {
                    String audience = decodedJWT.getAudience().get(0);
                    String jwtId = decodedJWT.getId();
                    String username = decodedJWT.getClaim(SystemConstant.TOKEN_USERNAME).asString();
                    Long userId = decodedJWT.getClaim(SystemConstant.TOKEN_USER_ID).asLong();
                    return redisTemplate.opsForValue().get(jwtId)
                            .flatMap(secretKeyObj -> {
                                if (secretKeyObj == null) {
                                    return Mono.error(
                                            UnhandledException.builder()
                                                    .errCode(HttpStatusExternal.TOKEN_EXCLUDED.value())
                                                    .errMsg("您已被强制下线")
                                                    .errStackTraceElement(Thread.currentThread().getStackTrace()[1])
                                                    .errTime(LocalDateTime.now())
                                                    .build()
                                    );
                                }
                                String secretKey = (String) secretKeyObj;
                                if (!username.equals(audience)) {
                                    return Mono.error(
                                            UnhandledException.builder()
                                                    .errCode(HttpStatusExternal.TOKEN_AUDIENCE_FAILED.value())
                                                    .errMsg("此令牌不属于此用户")
                                                    .errStackTraceElement(Thread.currentThread().getStackTrace()[1])
                                                    .errTime(LocalDateTime.now())
                                                    .build()
                                    );
                                }
                                Algorithm algorithm = Algorithm.HMAC256(secretKey);
                                JWTVerifier jwtVerifier = JWT.require(algorithm)
                                        .withClaim(SystemConstant.TOKEN_USERNAME, username)
                                        .withClaim(SystemConstant.TOKEN_USER_ID, userId)
                                        .build();
                                try {
                                    jwtVerifier.verify(accessToken);
                                } catch (AlgorithmMismatchException e) {
                                    return Mono.error(
                                            UnhandledException.builder()
                                                    .errCode(HttpStatusExternal.TOKEN_ALGORITHM_FAILED.value())
                                                    .errMsg("令牌加密算法不匹配")
                                                    .errStackTraceElement(Thread.currentThread().getStackTrace()[1])
                                                    .errTime(LocalDateTime.now())
                                                    .build()
                                    );
                                } catch (SignatureVerificationException e) {
                                    return Mono.error(
                                            UnhandledException.builder()
                                                    .errCode(HttpStatusExternal.TOKEN_SIGNATURE_FAILED.value())
                                                    .errMsg("签名非法")
                                                    .errStackTraceElement(Thread.currentThread().getStackTrace()[1])
                                                    .errTime(LocalDateTime.now())
                                                    .build()
                                    );
                                } catch (TokenExpiredException e) {
                                    return Mono.error(
                                            UnhandledException.builder()
                                                    .errCode(HttpStatusExternal.TOKEN_EXPIRED.value())
                                                    .errMsg("令牌已过期")
                                                    .errStackTraceElement(Thread.currentThread().getStackTrace()[1])
                                                    .errTime(LocalDateTime.now())
                                                    .build()
                                    );
                                } catch (InvalidClaimException e) {
                                    return Mono.error(
                                            UnhandledException.builder()
                                                    .errCode(HttpStatusExternal.TOKEN_CLAIM_MATCH_FAILED.value())
                                                    .errMsg("声明错误")
                                                    .errStackTraceElement(Thread.currentThread().getStackTrace()[1])
                                                    .errTime(LocalDateTime.now())
                                                    .build()
                                    );
                                }
                                return Mono.just(decodedJWT);
                            });
                });
    }

    public Mono<String> signRefreshToken(String username) {
        String secretKey = BaseUtils.getSalt();
        String jwtId = UUID.randomUUID().toString();
        return redisTemplate.opsForValue().set(jwtId, secretKey, Duration.ofMillis(SystemConstant.REFRESH_TOKEN_EXPIRED_TIME))
                .flatMap(ok1 -> {
                    return redisTemplate.opsForValue().set(username + SystemConstant.REFRESH_TOKEN_EXPIRED_TIME, jwtId, Duration.ofMillis(SystemConstant.REFRESH_TOKEN_EXPIRED_TIME))
                            .map(ok2 -> {
                                Algorithm algorithm = Algorithm.HMAC256(secretKey);
                                Date issueDate = new Date();
                                Date expiredDate = new Date(System.currentTimeMillis() + SystemConstant.REFRESH_TOKEN_EXPIRED_TIME);
                                // 控制jwtId来控制RefreshToken进而控制用户登录
                                String refreshToken = JWT.create()
                                        .withIssuedAt(issueDate)
                                        .withExpiresAt(expiredDate)
                                        .withJWTId(jwtId)
                                        .withIssuer(SystemConstant.ISSUER)
                                        .withAudience(SystemConstant.ACCESS_TOKEN)
                                        .withClaim(SystemConstant.TOKEN_USERNAME, username)
                                        .sign(algorithm);
                                return refreshToken;
                            });
                });
    }

    public Mono<String> signAccessToken(String refreshToken) {
        return decodeJWT(refreshToken)
                .flatMap(this::createAccessToken);
    }

    public Mono<Long> getUserId(String accessToken) {
        return decodeJWT(accessToken)
                .map(decodedJWT -> {
                    return decodedJWT.getClaim(SystemConstant.TOKEN_USER_ID).asLong();
                });
    }

    public Mono<Boolean> forceLogout(String username) {
        return redisTemplate.opsForValue().get(username + SystemConstant.REFRESH_TOKEN_EXPIRED_TIME)
                .flatMap(jwtId -> {
                    return redisTemplate.opsForValue().delete(jwtId.toString())
                            .map(ok -> {
                                redisTemplate.opsForValue().delete(username + SystemConstant.REFRESH_TOKEN_EXPIRED_TIME);
                                return ok;
                            });
                });
    }

    public Flux<String> getRoles(String accessToken) {
        return decodeJWT(accessToken)
                .map(decodedJWT -> {
                    return decodedJWT.getClaim(SystemConstant.TOKEN_USER_ROLES).asList(String.class);
                })
                .flatMapMany(Flux::fromIterable);
    }
}
