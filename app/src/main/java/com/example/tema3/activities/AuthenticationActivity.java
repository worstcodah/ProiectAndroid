package com.example.tema3.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.tema3.R;
import com.example.tema3.constants.Constants;
import com.example.tema3.fragments.LoginFragment;
import com.example.tema3.fragments.ResetPasswordFragment;
import com.example.tema3.fragments.SignUpFragment;
import com.example.tema3.interfaces.AuthenticationActivityFragmentCommunication;
import com.example.tema3.services.MyAlarm;

public class AuthenticationActivity extends AppCompatActivity implements AuthenticationActivityFragmentCommunication {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication_activity);
        openLoginFragment();
        setAlarm();
    }

    @Override
    public void openLoginFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = LoginFragment.class.getName();
        LoginFragment loginFragment = new LoginFragment();
        FragmentTransaction addTransaction = transaction.add(
                R.id.authentication_frame_layout, loginFragment, tag
        );
        addTransaction.commit();
    }

    @Override
    public void openSignUpFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = SignUpFragment.class.getName();
        SignUpFragment signUpFragment = new SignUpFragment();
        FragmentTransaction replaceTransaction = transaction.replace(
                R.id.authentication_frame_layout, signUpFragment, tag
        );
        replaceTransaction.commit();
    }


    @Override
    public void openChangePasswordFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = ResetPasswordFragment.class.getName();
        ResetPasswordFragment changePassword = new ResetPasswordFragment();
        FragmentTransaction replaceTransaction = transaction.replace(
                R.id.authentication_frame_layout, changePassword, tag
        );
        replaceTransaction.commit();
    }

    @Override
    public void openDashboardActivity() {
        Intent myIntent = new Intent(this, DashboardActivity.class);
        this.startActivity(myIntent);
    }

    private void setAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyAlarm.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        alarmManager.setRepeating(AlarmManager.RTC,
                System.currentTimeMillis(),
                Constants.ALARM_MILLISECONDS_INTERVAL_VALUE, pendingIntent);
    }
}