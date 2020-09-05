package com.example.cse110project.Tester;

import android.content.Context;

import static java.security.AccessController.getContext;

public class Tester {
    String thisAppUserName;
    String thisUserDocID;
    Context context;
    public Tester(Context context){

        this.context = context;
        this.thisAppUserName = context.getSharedPreferences("APPUSER", context.MODE_PRIVATE)
                                    .getString("USERNAME", "");

        this.thisUserDocID = context.getSharedPreferences("APPUSER", context.MODE_PRIVATE)
                .getString("DOC_ID", "");

    }

}
