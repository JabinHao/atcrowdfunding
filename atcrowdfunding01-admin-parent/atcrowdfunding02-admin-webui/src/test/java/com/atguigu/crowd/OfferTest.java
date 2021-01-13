package com.atguigu.crowd;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

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

    @Test
    public void testOffer04() {
        int[][] matrix = {{1,2,3},{4,5,6}};
        int[][] test = new int[0][0];
//        System.out.println(test[0][2]);
//        System.out.println(matrix[0][1]);
        System.out.println(test.length);
    }
}
