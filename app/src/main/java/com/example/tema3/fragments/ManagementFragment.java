package com.example.tema3.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tema3.R;
import com.example.tema3.adapters.MyAdapter;
import com.example.tema3.constants.Constants;
import com.example.tema3.interfaces.ManagementActivityFragmentCommunication;
import com.example.tema3.interfaces.OnTopicClickListener;
import com.example.tema3.models.Element;
import com.example.tema3.models.Topic;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManagementFragment extends Fragment implements OnTopicClickListener {
    private View view;
    private ArrayList<Element> elementList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ManagementActivityFragmentCommunication managementActivityFragmentCommunication;
    private MyAdapter myAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ManagementActivityFragmentCommunication) {
            managementActivityFragmentCommunication = (ManagementActivityFragmentCommunication) context;
        }
    }

    public ManagementFragment(ArrayList<Element> elementList) {
        this.elementList = elementList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.management_fragment, container, false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("topics");
        RecyclerView recyclerView = view.findViewById(R.id.personal_topics_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        myAdapter = new MyAdapter(elementList);
        recyclerView.setAdapter(myAdapter);
        getFilteredTopics();
        return view;
    }

    public void getFilteredTopics() {
        String currentUserEmail = getContext().getSharedPreferences(Constants.SHARED_PREFERENCES_USER_EMAIL, Context.MODE_PRIVATE).getString("email", null);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                elementList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String title = dataSnapshot.child("title").getValue().toString();
                    String description = dataSnapshot.child("description").getValue().toString();
                    String authorEmail = dataSnapshot.child("authorEmail").getValue().toString();
                    if (currentUserEmail.equals(authorEmail)) {
                        elementList.add(new Topic(title, description, authorEmail));
                    }
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), Constants.CANCELLED_MANAGEMENT_REALTIME_CONNECTION_MESSAGE, Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    public void openSelectedTopic(Topic topic) {
        managementActivityFragmentCommunication.openSelectedTopic(topic, false);
    }

}
