package com.example.tema3.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.tema3.R;
import com.example.tema3.fragments.ManagementFragment;
import com.example.tema3.interfaces.ManagementActivityFragmentCommunication;

public class ManagementActivity extends AppCompatActivity implements ManagementActivityFragmentCommunication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.management_activity);
        openManagementFragment();
    }

    @Override
    public void openManagementFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = ManagementFragment.class.getName();
        ManagementFragment managementFragment = new ManagementFragment();
        FragmentTransaction addTransaction = transaction.add(
                R.id.management_frame_layout, managementFragment, tag
        );
        addTransaction.commit();
    }
}
