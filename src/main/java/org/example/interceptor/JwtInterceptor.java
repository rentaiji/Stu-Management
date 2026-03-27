NEW_FILE_CODE
package org.example.interceptor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.header}")
    private String jwtHeader;

    @Value("${jwt.prefix}")
    private String jwtPrefix;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(jwtHeader);
        
        if (StringUtils.hasText(token) && token.startsWith(jwtPrefix)) {
            token = token.substring(jwtPrefix.length());
        }
        
        if (!StringUtils.hasText(token)) {
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write("{\"code\":401,\"message\":\"未登录\",\"data\":null}");
            writer.flush();
            return false;
        }
        
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();
            
            request.setAttribute("userId", claims.get("userId"));
            request.setAttribute("username", claims.getSubject());
            request.setAttribute("userType", claims.get("userType"));
            
            return true;
        } catch (Exception e) {
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write("{\"code\":401,\"message\":\"登录已过期或无效\",\"data\":null}");
            writer.flush();
            return false;
        }
    }
}
