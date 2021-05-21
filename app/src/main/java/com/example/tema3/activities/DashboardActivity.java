package com.example.tema3.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.tema3.R;
import com.example.tema3.fragments.DashboardFragment;
import com.example.tema3.interfaces.DashboardActivityFragmentCommunication;

public class DashboardActivity extends AppCompatActivity implements DashboardActivityFragmentCommunication {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);
        openDashboardFragment();
    }

    @Override
    public void openDashboardFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = DashboardFragment.class.getName();
        DashboardFragment dashboardFragment = new DashboardFragment();
        FragmentTransaction addTransaction = transaction.add(
                R.id.dashboard_frame_layout, dashboardFragment, tag
        );
        addTransaction.commit();
    }
}
