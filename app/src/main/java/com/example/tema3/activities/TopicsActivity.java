package com.example.tema3.activities;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.tema3.R;
import com.example.tema3.constants.Constants;
import com.example.tema3.fragments.SeeTopicsFragment;
import com.example.tema3.fragments.SelectedTopicFragment;
import com.example.tema3.interfaces.TopicsActivityFragmentCommunication;
import com.example.tema3.models.Topic;

public class TopicsActivity extends AppCompatActivity implements TopicsActivityFragmentCommunication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topics_activity);
        openTopicsFragment();
    }

    @Override
    public void openTopicsFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = SeeTopicsFragment.class.getName();
        SeeTopicsFragment seeTopicsFragment = new SeeTopicsFragment();
        FragmentTransaction addTransaction = transaction.add(
                R.id.topics_frame_layout, seeTopicsFragment, tag
        );
        addTransaction.commit();
    }

    @Override
    public void openSelectedTopic(Topic topic) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = SelectedTopicFragment.class.getName();
        boolean isTopicOwner = getSharedPreferences(Constants.SHARED_PREFERENCES_USER_EMAIL, Context.MODE_PRIVATE).getString("email", null)
                .equals(topic.getAuthorEmail());
        SelectedTopicFragment selectedTopicFragment = new SelectedTopicFragment(topic, isTopicOwner);
        FragmentTransaction replaceTransaction = transaction.replace(
                R.id.topics_frame_layout, selectedTopicFragment, tag
        );
        replaceTransaction.commit();
    }
}
