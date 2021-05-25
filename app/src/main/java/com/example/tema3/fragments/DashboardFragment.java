package com.example.tema3.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.tema3.R;
import com.example.tema3.activities.AuthenticationActivity;
import com.example.tema3.interfaces.DashboardActivityFragmentCommunication;
import com.example.tema3.user.CurrentUser;

public class DashboardFragment extends Fragment {
    private View view;
    private DashboardActivityFragmentCommunication dashboardActivityFragmentCommunication;
    private CardView topicsCard;
    private CardView manageCard;
    private CardView logoutCard;
    private TextView userEmailTv;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof DashboardActivityFragmentCommunication) {
            dashboardActivityFragmentCommunication = (DashboardActivityFragmentCommunication) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dashboard_fragment, container, false);
        logoutCard = view.findViewById(R.id.logout_card);
        topicsCard = view.findViewById(R.id.topics_card);
        manageCard = view.findViewById(R.id.manage_card);
        userEmailTv = view.findViewById(R.id.user_email_tv);
        userEmailTv.setText("Hello, " + CurrentUser.currentUserEmail);
        logoutCard.setOnClickListener(v -> openAuthenticationActivity());
        topicsCard.setOnClickListener(v -> dashboardActivityFragmentCommunication.openTopicsActivity());
        manageCard.setOnClickListener(v -> dashboardActivityFragmentCommunication.openManagementActivity());
        return view;
    }

    public void openAuthenticationActivity() {
        Intent myIntent = new Intent(getActivity(), AuthenticationActivity.class);
        this.startActivity(myIntent);
    }
}
