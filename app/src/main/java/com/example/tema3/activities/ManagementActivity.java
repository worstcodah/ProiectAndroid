package com.example.tema3.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.tema3.R;
import com.example.tema3.constants.Constants;
import com.example.tema3.fragments.AddTopicFragment;
import com.example.tema3.fragments.ManagementFragment;
import com.example.tema3.fragments.SelectedTopicFragment;
import com.example.tema3.interfaces.ManagementActivityFragmentCommunication;
import com.example.tema3.models.Element;
import com.example.tema3.models.Topic;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManagementActivity extends AppCompatActivity implements ManagementActivityFragmentCommunication, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private TextView email;
    private ArrayList<Element> elementList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.management_activity);
        elementList = new ArrayList<>();
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        toolbar = findViewById(R.id.nav_toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        email = navigationView.getHeaderView(0).findViewById(R.id.nav_header_email);
        email.setText(getApplicationContext().getSharedPreferences(Constants.SHARED_PREFERENCES_USER_EMAIL, Context.MODE_PRIVATE).getString("email", null));
        openManagementFragment();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.see_personal_topics_item:
                openManagementFragment();
                break;
            case R.id.add_topic_item:
                openAddTopicFragment();
                break;
            case R.id.delete_topic_item:
                deleteSelectedTopics();
                break;
            case R.id.edit_topic_item:
                editSelectedTopic();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void editSelectedTopic() {
        boolean selectedOnlyOne = false;
        Topic selectedTopic = null;
        for (Element element : elementList) {
            if (((Topic) element).isSelected()) {
                if (!selectedOnlyOne) {
                    selectedOnlyOne = true;
                    selectedTopic = (Topic) element;
                } else {
                    Toast.makeText(getApplicationContext(), Constants.TOO_MANY_TOPICS_SELECTED_MESSAGE, Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
        if (!selectedOnlyOne) {
            Toast.makeText(getApplicationContext(), Constants.NO_TOPICS_SELECTED_MESSAGE, Toast.LENGTH_SHORT).show();
        } else {
            openSelectedTopic(selectedTopic, true);
        }
    }

    public void deleteSelectedTopics() {
        boolean selectedAtLeastOne = false;
        for (Element element : elementList) {
            if (((Topic) element).isSelected()) {
                selectedAtLeastOne = true;
                deleteTopic((Topic) element);
            }
        }
        if (!selectedAtLeastOne) {
            Toast.makeText(getApplicationContext(), Constants.NO_TOPICS_SELECTED_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteTopic(Topic topic) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("topics");
        Query query = databaseReference.orderByChild("title").equalTo(topic.getTitle());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String title = dataSnapshot.child("title").getValue().toString();
                        if (title.equals(topic.getTitle())) {
                            databaseReference.child(dataSnapshot.getKey()).removeValue().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), Constants.SUCCESSFUL_DELETION_MESSAGE, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), Constants.FAILED_DELETION_MESSAGE, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), Constants.CANCELLED_DELETION_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void openManagementFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = ManagementFragment.class.getName();
        ManagementFragment managementFragment = new ManagementFragment(elementList);
        FragmentTransaction addTransaction = transaction.add(
                R.id.management_frame_layout, managementFragment, tag
        );
        addTransaction.commit();
    }

    @Override
    public void openAddTopicFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = AddTopicFragment.class.getName();
        AddTopicFragment addTopicFragment = new AddTopicFragment();
        FragmentTransaction replaceTransaction = transaction.replace(
                R.id.management_frame_layout, addTopicFragment, tag
        );
        replaceTransaction.addToBackStack(null);
        replaceTransaction.commit();
    }


    @Override
    public void openSelectedTopic(Topic topic, boolean canEdit) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        String tag = SelectedTopicFragment.class.getName();
        SelectedTopicFragment selectedTopicFragment = new SelectedTopicFragment(topic, true);
        FragmentTransaction replaceTransaction = transaction.replace(
                R.id.management_frame_layout, selectedTopicFragment, tag
        );
        replaceTransaction.addToBackStack(null);
        replaceTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            Log.i("ManagementActivity", "popping backstack");
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

}
