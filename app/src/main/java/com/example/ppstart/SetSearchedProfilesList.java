package com.example.ppstart;

import java.util.ArrayList;

public class SetSearchedProfilesList {
    public ArrayList<Profile> setSearchedArrList(ArrayList<Profile> all_browsable_profiles, String search_box_entry){

        boolean exists = false;
        ArrayList<Profile> searched_array_list = new ArrayList<>();

        for (Profile profile : all_browsable_profiles) {
            //.equalsIgnoreCase ensures the search is case-insensitive -Blake
            if (profile.getBusiness_name().equalsIgnoreCase(search_box_entry)) {
                //if the name of a business queried from the database exactly matches what is entered into the
                //search box, then it is stored into the array list of items successfully searched -Blake
                searched_array_list.add(profile);
            }

            //split the profile being iterated through by the for-each loop by spaces and
            //store the result in a string array -Blake
            String[] partial_search = profile.getBusiness_name().split(" ");
            for (int i = 0; i < partial_search.length; i++) {
                //if the user enters a partial search and it matches a string stored
                //in the partial search array, then check if the profile was already added to the
                //array list of successful searches -Blake
                if (search_box_entry.equalsIgnoreCase(partial_search[i])) {
                    for (Profile profile_search : searched_array_list) {
                        //if the profile being iterated through by the first for-each loop
                        //was already added to the list of successful searches, then indicate
                        //this by setting the exists boolean value to true -Blake
                        if (profile_search.getProfile_id() == profile.getProfile_id()) {
                            exists = true;
                        }
                    }

                    //if the profile was not already added to the list of successful searches,
                    //then add it to this list, meaning that it was found by partial search and not
                    //exact match -Blake
                    if (!exists) {
                        searched_array_list.add(profile);
                    }
                }
            }
        }



        return searched_array_list;


    }
}
