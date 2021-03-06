package com.example.tema3.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tema3.R;
import com.example.tema3.adapters.MyAdapter;
import com.example.tema3.constants.Constants;
import com.example.tema3.interfaces.OnTopicClickListener;
import com.example.tema3.interfaces.TopicsActivityFragmentCommunication;
import com.example.tema3.models.Element;
import com.example.tema3.models.Topic;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class SeeTopicsFragment extends Fragment implements OnTopicClickListener {
    private ArrayList<Element> elementList;
    private TopicsActivityFragmentCommunication topicsActivityFragmentCommunication;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof TopicsActivityFragmentCommunication) {
            topicsActivityFragmentCommunication = (TopicsActivityFragmentCommunication) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.see_topics_fragment, container, false);
        elementList = new ArrayList<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("topics");
        ImageView addTopicIv = view.findViewById(R.id.see_topics_add_iv);
        RecyclerView recyclerView = view.findViewById(R.id.topics_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        MyAdapter myAdapter = new MyAdapter(elementList, this);
        recyclerView.setAdapter(myAdapter);
        addTopicIv.setOnClickListener(v -> topicsActivityFragmentCommunication.openAddTopicFragment());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                elementList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String title = Objects.requireNonNull(dataSnapshot.child("title").getValue()).toString();
                    String description = Objects.requireNonNull(dataSnapshot.child("description").getValue()).toString();
                    String authorEmail = Objects.requireNonNull(dataSnapshot.child("authorEmail").getValue()).toString();
                    elementList.add(new Topic(title, description, authorEmail));
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), Constants.CANCELLED_TOPICS_REALTIME_CONNECTION_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void openSelectedTopic(Topic topic) {
        topicsActivityFragmentCommunication.openSelectedTopic(topic);
    }

}