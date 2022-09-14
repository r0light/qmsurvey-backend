package de.uniba.dsg.cna.qmsurvey;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx)
            throws IOException {
        response.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        /*response.getWriter().write(new JSONObject()
                .appendField("statusCode", "401")
                .appendField("message", "Access denied")
                .toString());
                */
        response.getWriter().write("{statusCode: 401, message: \"Access denied\"}");
    }

    @Override
    public void afterPropertiesSet() {
        setRealmName("qmsurvey");
        super.afterPropertiesSet();
    }
}