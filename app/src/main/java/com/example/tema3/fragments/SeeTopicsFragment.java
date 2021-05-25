package com.example.tema3.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tema3.R;
import com.example.tema3.adapters.MyAdapter;
import com.example.tema3.models.Element;
import com.example.tema3.models.Topic;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SeeTopicsFragment extends Fragment {
    private View view;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<Element> elementList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.see_topics_fragment, container, false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        RecyclerView recyclerView = view.findViewById(R.id.topic_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new MyAdapter(elementList));
        ArrayList<Topic> topicList = new ArrayList<>();
        databaseReference.setValue(topicList);
        return view;
    }
}
