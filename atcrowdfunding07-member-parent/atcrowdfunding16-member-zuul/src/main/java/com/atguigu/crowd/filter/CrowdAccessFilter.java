package com.atguigu.crowd.filter;

import com.atguigu.crowd.constant.AccessPassResources;
import com.atguigu.crowd.constant.CrowdConstant;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * filterType：返回过滤器的类型。有pre、route、post、error等几种取值，分别对应几种过滤器。
 * filterOrder：返回一个int值来指定过滤器的执行顺序，不同的过滤器允许返回相同的数字。
 * shouldFilter：返回一个boolean值来判断该过滤器是否要执行，true表示执行，false表示不执行。
 * run：过滤器的具体逻辑。本例中，我们让它打印了请求的HTTP方法以及请求的地址
 */
@Component
public class CrowdAccessFilter extends ZuulFilter {
    @Override
    public String filterType() {

        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {

        // 1.获取RequestContext对象
        RequestContext requestContext = RequestContext.getCurrentContext();

        // 2.通过RequestContext对象获取当前请求对象（ 框架底层是借助 ThreadLocal 从当前线程上获取事先绑定的 Request 对象）
        HttpServletRequest request = requestContext.getRequest();

        // 3.获取 servletPath 值
        String servletPath = request.getServletPath();

        // 4.根据 servletPath 判断当前请求是否对应可以直接放行的特定功能
        boolean containsResult = AccessPassResources.PASS_RES_SET.contains(servletPath);
        // 如果当前请求是可以直接放行的特定功能请求则返回 false 放行
        if (containsResult){
            return false;
        }

        // 5.判断当前请求是否为静态资源
        return !AccessPassResources.judgeCurrentServletPathWhetherStaticResource(servletPath);
    }

    @Override
    public Object run() throws ZuulException {

        // 1.获取当前请求对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        // 2.获取当前Session对象
        HttpSession session = request.getSession();

        // 3.尝试从 Session 对象中获取已登录用户
        Object loginMember = session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);

        // 4.判断 loginMember 是否为空
        if (loginMember == null) {

            // 5.从requestContext对象中获取Response对象
            HttpServletResponse response = requestContext.getResponse();

            // 6.将提示消息存入session域
            session.setAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_ACCESS_FORBIDDEN);

            // 7.重定向到 auth-consumer 工程中的登录页面
            try {
                response.sendRedirect("/auth/member/to/login/page");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
