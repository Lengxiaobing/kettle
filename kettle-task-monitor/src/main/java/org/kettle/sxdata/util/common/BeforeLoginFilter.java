package org.kettle.sxdata.util.common;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @description: 登录前过滤器
 *
 * @author:   ZX
 * @date:     2018/11/20 12:21
 */
public class BeforeLoginFilter implements Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest)arg0;
        HttpServletResponse response=(HttpServletResponse)arg1;
        HttpSession session=request.getSession();
            if (null!=session.getAttribute("login")) {
                response.sendRedirect(request.getContextPath()+"/index.jsp");
                return;
            } else {
                chain.doFilter(arg0, arg1);
                return;
            }
    }

    @Override
    public void destroy() {

    }
}
