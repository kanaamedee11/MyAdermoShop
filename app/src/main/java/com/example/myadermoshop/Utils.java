package com.example.myadermoshop;

import android.content.Context;
import android.content.Intent;

/* loaded from: classes.dex */
public class Utils {
    public static boolean checkAndDisplayClosure(Context context, DatabaseHelper databaseHelper) {
        if (!databaseHelper.isClosureDataExistsForToday()) {
            return false;
        }
        context.startActivity(new Intent(context, ClosureCheckActivity.class));
        return true;
    }
}