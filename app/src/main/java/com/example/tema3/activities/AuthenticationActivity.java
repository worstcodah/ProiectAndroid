package com.example.tema3.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.tema3.R;
import com.example.tema3.fragments.ResetPasswordFragment;
import com.example.tema3.fragments.DashboardFragment;
import com.example.tema3.fragments.LoginFragment;
import com.example.tema3.fragments.SignUpFragment;
import com.example.tema3.interfaces.AuthenticationActivityFragmentCommunication;

public class AuthenticationActivity extends AppCompatActivity implements AuthenticationActivityFragmentCommunication {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        openLoginFragment();
    }

    @Override
    public void openLoginFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = LoginFragment.class.getName();
        LoginFragment loginFragment = new LoginFragment();
        FragmentTransaction addTransaction = transaction.add(
                R.id.frame_layout, loginFragment, tag
        );
        addTransaction.commit();
    }

    @Override
    public void openSignUpFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = SignUpFragment.class.getName();
        SignUpFragment signUpFragment = new SignUpFragment();
        FragmentTransaction addTransaction = transaction.add(
                R.id.frame_layout, signUpFragment, tag
        );
        addTransaction.commit();
    }

    @Override
    public void openDashboardFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = DashboardFragment.class.getName();
        DashboardFragment dashboardFragment = new DashboardFragment();
        FragmentTransaction addTransaction = transaction.replace(
                R.id.frame_layout, dashboardFragment, tag
        );
        addTransaction.commit();
    }

    @Override
    public void openChangePasswordFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = ResetPasswordFragment.class.getName();
        ResetPasswordFragment changePassword = new ResetPasswordFragment();
        FragmentTransaction addTransaction = transaction.replace(
                R.id.frame_layout, changePassword, tag
        );
        addTransaction.commit();
    }
}