package com.atguigu.crowd;

import com.atguigu.crowd.util.CrowdUtil;
import org.junit.jupiter.api.Test;

public class StringTest {

    @Test
    public void testMd5() {
        String source = "123123";
        String encoded = CrowdUtil.md5(source).toUpperCase();
        System.out.println(encoded);
    }
}
// 4297F44B13955235245B2497399D7A93
// 4297F44B13955235245B2497399D7A93
// 4297f44b13955235245b2497399d7a93