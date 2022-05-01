package com.example.ppstart;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class RemoveHeaderUnitTest extends business_profile_textEditor {
    @Test
    public void headerRemoval_WithAbout() {
        String expected = "This is a cool business";
        String actual = removeHeader("About This is a cool business");
        assertEquals(expected, 2 + 2);
    }

    @Test
    public void headerRemoval_WithAboutWS() {
        String expected = "This is a cool business";
        String actual = removeHeader(" About      This is a cool business ");
        assertEquals(expected, 2 + 2);
    }

    @Test
    public void headerRemoval_WithOutAbout() {
        String expected = "This is a cool business";
        String actual = removeHeader("This is a cool business");
        assertEquals(expected, actual);
    }

    @Test
    public void headerRemoval_WithOutAbout_WS() {
        String expected = "This is a cool business";
        String actual = removeHeader(" This is a cool business ");
        assertEquals(expected, actual);
    }
}