package com.example.ppstart;

import junit.framework.TestCase;

import org.junit.Test;

public class Find_Closest_Store_ActivityTest extends TestCase {
    @Test
    void verifyLocation(){
        Find_Closest_Store_Activity test = new Find_Closest_Store_Activity();
        double[] var = new double[2];
        var[0] = 30.477751;
        var[1] = -90.107140;
        //adjust this to your current location
        assertEquals(var, test.getLastLocation(50));
    }


}