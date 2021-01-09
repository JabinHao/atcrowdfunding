package com.atguigu.crowd;

public class OfferTest {
    public int findRepeatNumber(int[] nums) {
        int len = nums.length;
        boolean[] b = new boolean[len];

        for (int num:nums) {
            if (b[num]){
                return num;
            }else {
                b[num] = true;
            }
        }

        return 0;
    }
}
