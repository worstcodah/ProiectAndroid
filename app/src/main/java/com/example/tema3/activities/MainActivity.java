package com.example.tema3.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.tema3.R;
import com.example.tema3.interfaces.ActivityFragmentCommunication;

public class MainActivity extends AppCompatActivity implements ActivityFragmentCommunication {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void openLoginFragment() {

    }
}