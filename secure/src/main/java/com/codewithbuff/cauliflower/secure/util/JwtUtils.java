package com.codewithbuff.cauliflower.secure.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.codewithbuff.cauliflower.secure.exception.UnhandledException;
import com.codewithbuff.cauliflower.secure.result.HttpStatusExternal;
import com.codewithbuff.cauliflower.secure.system.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author 十三月之夜
 * @time 2021/5/29 4:06 下午
 */
@Component
public class JwtUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private DecodedJWT decodeJWT(String jwt) throws UnhandledException {
        if (jwt == null) {
            throw UnhandledException.builder()
                    .errCode(HttpStatus.BAD_REQUEST.value())
                    .errMsg("令牌不可为空")
                    .errStackTraceElement(Thread.currentThread().getStackTrace()[1])
                    .errTime(LocalDateTime.now())
                    .build();
        } else {
            DecodedJWT decodedJWT = JWT.decode(jwt);
            Date issueDate = decodedJWT.getIssuedAt();
            Date expiredDate = decodedJWT.getExpiresAt();
            String issuer = decodedJWT.getIssuer();
            if (issueDate.after(new Date()) || issueDate.after(expiredDate)) {
                throw UnhandledException.builder()
                        .errCode(HttpStatusExternal.TOKEN_SIGN_DATE_ERROR.value())
                        .errMsg("令牌签发日期异常")
                        .errStackTraceElement(Thread.currentThread().getStackTrace()[1])
                        .errTime(LocalDateTime.now())
                        .build();
            }
            if (expiredDate.before(new Date())) {
                throw UnhandledException.builder()
                        .errCode(HttpStatusExternal.TOKEN_EXPIRED.value())
                        .errMsg("令牌已过期")
                        .errStackTraceElement(Thread.currentThread().getStackTrace()[1])
                        .errTime(LocalDateTime.now())
                        .build();
            }
            if (!SystemConstant.ISSUER.equals(issuer)) {
                throw UnhandledException.builder()
                        .errCode(HttpStatusExternal.TOKEN_ISSUER_FAILED.value())
                        .errMsg("此令牌不属于本系统")
                        .errStackTraceElement(Thread.currentThread().getStackTrace()[1])
                        .errTime(LocalDateTime.now())
                        .build();
            }
            return decodedJWT;
        }
    }

    public DecodedJWT verifyRefreshToken(String refreshToken) throws UnhandledException {
        DecodedJWT decodedJWT = decodeJWT(refreshToken);
        String audience = decodedJWT.getAudience().get(0);
        String jwtId = decodedJWT.getId();
        String username = decodedJWT.getClaim(SystemConstant.TOKEN_USERNAME).asString();
        if (!SystemConstant.ACCESS_TOKEN.equals(audience)) {
            throw UnhandledException.builder()
                    .errCode(HttpStatusExternal.TOKEN_AUDIENCE_FAILED.value())
                    .errMsg("此令牌不是刷新令牌")
                    .errStackTraceElement(Thread.currentThread().getStackTrace()[1])
                    .errTime(LocalDateTime.now())
                    .build();
        }
        String secretKey = (String) redisTemplate.opsForValue().get(jwtId);
        if (secretKey == null) {
            throw UnhandledException.builder()
                    .errCode(HttpStatusExternal.TOKEN_EXCLUDED.value())
                    .errMsg("您已被强制下线")
                    .errStackTraceElement(Thread.currentThread().getStackTrace()[1])
                    .errTime(LocalDateTime.now())
                    .build();
        }
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier jwtVerifier = JWT.require(algorithm)
                .withJWTId(jwtId)
                .withClaim(SystemConstant.TOKEN_USERNAME, username)
                .build();
        try {
            jwtVerifier.verify(refreshToken);
        } catch (AlgorithmMismatchException e) {
            throw UnhandledException.builder()
                    .errCode(HttpStatusExternal.TOKEN_ALGORITHM_FAILED.value())
                    .errMsg("令牌加密算法不匹配")
                    .errStackTraceElement(Thread.currentThread().getStackTrace()[1])
                    .errTime(LocalDateTime.now())
                    .build();
        } catch (SignatureVerificationException e) {
            throw UnhandledException.builder()
                    .errCode(HttpStatusExternal.TOKEN_SIGNATURE_FAILED.value())
                    .errMsg("签名非法")
                    .errStackTraceElement(Thread.currentThread().getStackTrace()[1])
                    .errTime(LocalDateTime.now())
                    .build();
        } catch (TokenExpiredException e) {
            throw UnhandledException.builder()
                    .errCode(HttpStatusExternal.TOKEN_EXPIRED.value())
                    .errMsg("令牌已过期")
                    .errStackTraceElement(Thread.currentThread().getStackTrace()[1])
                    .errTime(LocalDateTime.now())
                    .build();
        } catch (InvalidClaimException e) {
            throw UnhandledException.builder()
                    .errCode(HttpStatusExternal.TOKEN_CLAIM_MATCH_FAILED.value())
                    .errMsg("声明错误")
                    .errStackTraceElement(Thread.currentThread().getStackTrace()[1])
                    .errTime(LocalDateTime.now())
                    .build();
        }
        return decodedJWT;
    }

    public DecodedJWT verifyAccessToken(String accessToken) throws UnhandledException {
        DecodedJWT decodedJWT = decodeJWT(accessToken);
        String audience = decodedJWT.getAudience().get(0);
        String jwtId = decodedJWT.getId();
        String username = decodedJWT.getClaim(SystemConstant.TOKEN_USERNAME).asString();
        Long userId = decodedJWT.getClaim(SystemConstant.TOKEN_USER_ID).asLong();
        String secretKey = (String) redisTemplate.opsForValue().get(jwtId);
        if (secretKey == null) {
            throw UnhandledException.builder()
                    .errCode(HttpStatusExternal.TOKEN_EXCLUDED.value())
                    .errMsg("您已被强制下线")
                    .errStackTraceElement(Thread.currentThread().getStackTrace()[1])
                    .errTime(LocalDateTime.now())
                    .build();
        }
        if (!username.equals(audience)) {
            throw UnhandledException.builder()
                    .errCode(HttpStatusExternal.TOKEN_AUDIENCE_FAILED.value())
                    .errMsg("此令牌不属于此用户")
                    .errStackTraceElement(Thread.currentThread().getStackTrace()[1])
                    .errTime(LocalDateTime.now())
                    .build();
        }
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier jwtVerifier = JWT.require(algorithm)
                .withClaim(SystemConstant.TOKEN_USERNAME, username)
                .withClaim(SystemConstant.TOKEN_USER_ID, userId)
                .build();
        try {
            jwtVerifier.verify(accessToken);
        } catch (AlgorithmMismatchException e) {
            throw UnhandledException.builder()
                    .errCode(HttpStatusExternal.TOKEN_ALGORITHM_FAILED.value())
                    .errMsg("令牌加密算法不匹配")
                    .errStackTraceElement(Thread.currentThread().getStackTrace()[1])
                    .errTime(LocalDateTime.now())
                    .build();
        } catch (SignatureVerificationException e) {
            throw UnhandledException.builder()
                    .errCode(HttpStatusExternal.TOKEN_SIGNATURE_FAILED.value())
                    .errMsg("签名非法")
                    .errStackTraceElement(Thread.currentThread().getStackTrace()[1])
                    .errTime(LocalDateTime.now())
                    .build();
        } catch (TokenExpiredException e) {
            throw UnhandledException.builder()
                    .errCode(HttpStatusExternal.TOKEN_EXPIRED.value())
                    .errMsg("令牌已过期")
                    .errStackTraceElement(Thread.currentThread().getStackTrace()[1])
                    .errTime(LocalDateTime.now())
                    .build();
        } catch (InvalidClaimException e) {
            throw UnhandledException.builder()
                    .errCode(HttpStatusExternal.TOKEN_CLAIM_MATCH_FAILED.value())
                    .errMsg("声明错误")
                    .errStackTraceElement(Thread.currentThread().getStackTrace()[1])
                    .errTime(LocalDateTime.now())
                    .build();
        }
        return decodedJWT;
    }

    public long getUserId(DecodedJWT decodedJWT) throws UnhandledException {
        return decodedJWT.getClaim(SystemConstant.TOKEN_USER_ID).asLong();
    }

    public void forceLogout(String username) {
        String jwtId = (String) redisTemplate.opsForValue().get(username + SystemConstant.REFRESH_TOKEN_EXPIRED_TIME);
        redisTemplate.delete(jwtId);
        redisTemplate.delete(username + SystemConstant.REFRESH_TOKEN_EXPIRED_TIME);
    }

    public List<String> getRoles(DecodedJWT decodedJWT) throws UnhandledException {
        return decodedJWT.getClaim(SystemConstant.TOKEN_USER_ROLES).asList(String.class);
    }
}
