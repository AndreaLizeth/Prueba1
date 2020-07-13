package com.example.prueba1;

import android.content.ContentProvider;
import android.os.Build;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseApp extends android.app.Application{

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

}
