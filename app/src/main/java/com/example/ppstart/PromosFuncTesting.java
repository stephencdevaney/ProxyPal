package com.example.ppstart;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PromosFuncTesting {

    public String promoEditingTest(String s, String mode, int sid_o, int iid_o, int sid_f, int iid_f) {

        s = s.trim();

        if (s.isEmpty()) {
            return "Empty";
        } else {
            if (mode.equals("edit")) {
                return s;
            } else {
                try {
                    if (sid_o != sid_f || iid_o != iid_f) {
                        return "Added";
                    } else {
                        return "Already Exists";
                    }
                } catch (SQLiteException e) {
                    e.printStackTrace();
                }
            }
        }
        return "No Catch";
    }
}
