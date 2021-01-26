package com.atguigu.crowd.constant;

import java.util.HashSet;
import java.util.Set;

public class AccessPassResources {

    public static final Set<String> PASS_RES_SET = new HashSet<>();

    static {
        PASS_RES_SET.add("/");
        PASS_RES_SET.add("/auth/member/to/reg/page");
        PASS_RES_SET.add("/auth/member/to/login/page");
        PASS_RES_SET.add("/auth/member/logout");
        PASS_RES_SET.add("/auth/member/do/login");
        PASS_RES_SET.add("/auth/do/member/register");
        PASS_RES_SET.add("/auth/member/send/short/message.json");
    }

    public static final Set<String> STATIC_RES_SET = new HashSet<>();

    static {
        STATIC_RES_SET.add("bootstrap");
        STATIC_RES_SET.add("css");
        STATIC_RES_SET.add("fonts");
        STATIC_RES_SET.add("img");
        STATIC_RES_SET.add("jquery");
        STATIC_RES_SET.add("layer");
        STATIC_RES_SET.add("script");
        STATIC_RES_SET.add("ztree");
    }

    /**
     * 用于判断某个 ServletPath 值是否对应一个静态资源
     * @param servletPath
     * @return
     *      true：是静态资源
     *      false：不是静态资源
     */
    public static boolean judgeCurrentServletPathWhetherStaticResource(String servletPath) {

        if (servletPath == null || servletPath.length() == 0) {
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }

        String[] split = servletPath.split("/");

        String firstLevelPath = split[1];

        return STATIC_RES_SET.contains(firstLevelPath);
    }
}
