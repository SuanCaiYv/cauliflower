package com.codewithbuff.cauliflower.secure.sureness.processor;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.codewithbuff.cauliflower.secure.exception.UnhandledException;
import com.codewithbuff.cauliflower.secure.util.JwtUtils;
import com.usthe.sureness.processor.BaseProcessor;
import com.usthe.sureness.processor.exception.SurenessAuthenticationException;
import com.usthe.sureness.subject.Subject;
import com.usthe.sureness.subject.support.JwtSubject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 十三月之夜
 * @time 2021/5/29 3:12 下午
 */
@Component
public class SecureJwtProcessor extends BaseProcessor {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean canSupportSubjectClass(Class<?> var) {
        return var == JwtSubject.class;
    }

    @Override
    public Class<?> getSupportSubjectClass() {
        return JwtSubject.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Subject authenticated(Subject subject) throws SurenessAuthenticationException {
        JwtSubject jwtSubject = (JwtSubject) subject;
        String jwtStr = (String) jwtSubject.getCredential();
        DecodedJWT decodedJWT = null;
        try {
            decodedJWT = jwtUtils.verifyAccessToken(jwtStr);
            List<String> ownRoles = (List<String>) jwtSubject.getOwnRoles();
            ownRoles.addAll(jwtUtils.getRoles(decodedJWT));
            return jwtSubject;
        } catch (UnhandledException e) {
            throw new SurenessAuthenticationException(e.toJsonString());
        }
    }

    /**
     * 这里可以选择重写授权方法，但是也可以不重写，因为调用完了checkIn之后，会在认证完成之后自动调用授权方法，默认的授权方法
     * 是遍历supportRoles(可以访问目标资源的角色)和ownRoles(当前用户持有的角色)，然后看是否有匹配的，很简单的双循环，那么问题来了：
     * ownRoles我们刚刚设置过了，那supportRoles是什么时候设置的呢？答案就是在checkIn中，会使用字典树的matchRole方法对传入进去的Subject的
     * supportRoles进行设置，所以有这样的流程：
     * checkIn()=>createSubject()=>for-each SubjectList=>authentication()=>matchRole()=>setSupportRoles()=>authorization()=>Done
     */
}
