package com.example.ppstart;

//**********************************************************
//Blake's Unit Test Case
//This unit test is for testing the SetSearchedProfilesList class
//**********************************************************

import static org.junit.Assert.*;
import java.util.ArrayList;

import org.junit.Test;

import java.util.Arrays;

public class SetSearchedProfilesListTest {

    String search_box_entry;

    Profile test_profile1 = new Profile(124, 235, "Jackie's", "www.image.url");
    Profile test_profile2 = new Profile(14124, 1245, "Ray's Rackets", "www.stockphoto.url");
    Profile test_profile3 = new Profile(9481, 12412, "Xtra", "www.genericimage.url");
    Profile test_profile4 = new Profile(1058, 1246, "Dimeland Rackets", "www.imgur.jpg");

    ArrayList<Profile> empty_profiles_list = new ArrayList<>();
    ArrayList<Profile> populated_profiles_list = new ArrayList<>(Arrays.asList(test_profile1, test_profile2, test_profile3, test_profile4));

    @Test
    public void SearchListEmptyIfProfListEmpty(){
        search_box_entry = "Cars";
        SetSearchedProfilesList setSearchedProfilesList = new SetSearchedProfilesList();
        ArrayList<Profile> searched_profiles_test = setSearchedProfilesList.setSearchedArrList(empty_profiles_list, search_box_entry);
        assertEquals(0, searched_profiles_test.size());
    }

    @Test
    public void SearchListEmptyIfSearchBoxEmpty(){
        search_box_entry = "";
        SetSearchedProfilesList setSearchedProfilesList = new SetSearchedProfilesList();
        ArrayList<Profile> searched_profiles_test = setSearchedProfilesList.setSearchedArrList(populated_profiles_list, search_box_entry);
        assertEquals(0, searched_profiles_test.size());
    }

    @Test
    public void CompleteSearchHasCorrectSize(){
        search_box_entry = "Xtra";
        SetSearchedProfilesList setSearchedProfilesList = new SetSearchedProfilesList();
        ArrayList<Profile> searched_profiles_test = setSearchedProfilesList.setSearchedArrList(populated_profiles_list, search_box_entry);
        assertEquals(1, searched_profiles_test.size());
    }

    @Test
    public void CompleteSearchContainsMatchingProfiles(){
        search_box_entry = "Xtra";
        SetSearchedProfilesList setSearchedProfilesList = new SetSearchedProfilesList();
        ArrayList<Profile> searched_profiles_test  = setSearchedProfilesList.setSearchedArrList(populated_profiles_list, search_box_entry);
        assertTrue(searched_profiles_test.contains(test_profile3));
    }

    @Test
    public void PartialSearchHasCorrectSize(){
        search_box_entry = "Rackets";
        SetSearchedProfilesList setSearchedProfilesList = new SetSearchedProfilesList();
        ArrayList<Profile> searched_profiles_test  = setSearchedProfilesList.setSearchedArrList(populated_profiles_list, search_box_entry);
        assertEquals(2, searched_profiles_test.size());
    }

    @Test
    public void PartialSearchContainsMatchingProfiles(){
        search_box_entry = "Rackets";
        SetSearchedProfilesList setSearchedProfilesList = new SetSearchedProfilesList();
        ArrayList<Profile> searched_profiles_test  = setSearchedProfilesList.setSearchedArrList(populated_profiles_list, search_box_entry);
        assertTrue(searched_profiles_test.contains(test_profile2));
        assertTrue(searched_profiles_test.contains(test_profile4));
    }

}