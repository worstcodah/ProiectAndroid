package com.example.tema3.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tema3.R;
import com.example.tema3.constants.Constants;
import com.example.tema3.models.Topic;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class AddTopicFragment extends Fragment {
    private EditText topicTitleEt;
    private EditText topicDescriptionEt;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_topic_fragment, container, false);
        topicTitleEt = view.findViewById(R.id.add_topic_title);
        topicDescriptionEt = view.findViewById(R.id.add_topic_description);
        Button addTopicButton = view.findViewById(R.id.add_topic_button);
        addTopicButton.setOnClickListener(v -> addTopic(new Topic(topicTitleEt.getText().toString(), topicDescriptionEt.getText().toString(),
                requireContext().getSharedPreferences(Constants.SHARED_PREFERENCES_USER_EMAIL, Context.MODE_PRIVATE).getString("email", null))));
        return view;
    }

    private void addTopic(Topic topic) {
        if (!isValidInput()) {
            Toast.makeText(getContext(), Constants.INVALID_INPUT_MESSAGE, Toast.LENGTH_SHORT).show();
            return;
        }
        databaseReference = FirebaseDatabase.getInstance().getReference().child("topics");
        Query query = databaseReference.orderByChild("title").equalTo(topic.getTitle());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String userEmail = requireContext().getSharedPreferences(Constants.SHARED_PREFERENCES_USER_EMAIL, Context.MODE_PRIVATE).getString("email", null);
                        String title = Objects.requireNonNull(dataSnapshot.child("title").getValue()).toString();
                        if (userEmail.equals(topic.getAuthorEmail()) && title.equals(topic.getTitle())) {
                            Toast.makeText(getContext(), Constants.TOPIC_ALREADY_EXISTS_MESSAGE, Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    databaseReference.push().setValue(topic);
                    Toast.makeText(getContext(), Constants.SUCCESSFUL_ADDITION_MESSAGE, Toast.LENGTH_SHORT).show();
                    topicTitleEt.setText(Constants.STRING_EMPTY);
                    topicDescriptionEt.setText(Constants.STRING_EMPTY);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), Constants.CANCELLED_ADD_UPDATE_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidInput() {
        String descriptionText = topicDescriptionEt.getText().toString();
        String titleText = topicTitleEt.getText().toString();
        boolean isValid = true;
        if (TextUtils.isEmpty(descriptionText)) {
            topicDescriptionEt.setError(Constants.EMPTY_DESCRIPTION_MESSAGE);
            isValid = false;
        }
        if (TextUtils.isEmpty(titleText)) {
            topicTitleEt.setError(Constants.EMPTY_TITLE_MESSAGE);
            isValid = false;
        }
        return isValid;
    }
}
