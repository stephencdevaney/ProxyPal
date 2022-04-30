package com.example.ppstart;

import org.junit.Test;
import static org.junit.Assert.*;

public class PromosTest {
    @Test
    public void noDescriptionEditMode(){
        PromosFuncTesting funcTesting = new PromosFuncTesting();
        String result = funcTesting.promoEditingTest("", "edit", 1, 1, 1, 2);
        assertEquals("Empty", result);
    }

    @Test
    public void noDescriptionAddMode(){
        PromosFuncTesting funcTesting = new PromosFuncTesting();
        String result = funcTesting.promoEditingTest("", "add", 1, 1, 1, 2);
        assertEquals("Empty", result);
    }

    @Test
    public void editDescription(){
        PromosFuncTesting funcTesting = new PromosFuncTesting();
        String desc = "An updated description";
        String result = funcTesting.promoEditingTest(desc, "edit", 1, 1, 1, 2);
        assertEquals(desc, result);
    }

    @Test
    public void addPromotionNotFound1(){
        PromosFuncTesting funcTesting = new PromosFuncTesting();
        String desc = "An new promotion";
        String result = funcTesting.promoEditingTest(desc, "add", 1, 1, 1, 2);
        assertEquals("Added", result);
    }

    @Test
    public void addPromotionNotFound2(){
        PromosFuncTesting funcTesting = new PromosFuncTesting();
        String desc = "An new promotion";
        String result = funcTesting.promoEditingTest(desc, "add", 1, 2, 1, 1);
        assertEquals("Added", result);
    }

    @Test
    public void addPromotionFound(){
        PromosFuncTesting funcTesting = new PromosFuncTesting();
        String desc = "An new promotion";
        String result = funcTesting.promoEditingTest(desc, "add", 1, 1, 1, 1);
        assertEquals("Already Exists", result);
    }
}
