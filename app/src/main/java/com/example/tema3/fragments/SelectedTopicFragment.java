package com.example.tema3.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tema3.R;
import com.example.tema3.adapters.MyAdapter;
import com.example.tema3.constants.Constants;
import com.example.tema3.interfaces.OnCommentClickListener;
import com.example.tema3.interfaces.OnUpdateClickListener;
import com.example.tema3.models.Comment;
import com.example.tema3.models.Element;
import com.example.tema3.models.Topic;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Stack;

public class SelectedTopicFragment extends Fragment implements OnCommentClickListener, OnUpdateClickListener {
    private Topic selectedTopic;
    private ArrayList<Element> elementList;
    private MyAdapter myAdapter;
    private DatabaseReference databaseReference;
    private EditText commentContentEt;
    private RecyclerView recyclerView;
    private boolean isTopicOwner;

    public SelectedTopicFragment() {

    }

    public static SelectedTopicFragment newInstance(Topic topic, boolean isTopicOwner) {
        SelectedTopicFragment selectedTopicFragment = new SelectedTopicFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isTopicOwner", isTopicOwner);
        bundle.putParcelable("selectedTopic", topic);
        selectedTopicFragment.setArguments(bundle);
        return selectedTopicFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.selected_topic_fragment, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            selectedTopic = bundle.getParcelable("selectedTopic");
            isTopicOwner = bundle.getBoolean("isTopicOwner");
        }
        elementList = new ArrayList<>();
        Button addCommentButton = view.findViewById(R.id.add_comment_button);
        commentContentEt = view.findViewById(R.id.comment_content_et);
        recyclerView = view.findViewById(R.id.selected_topic_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        myAdapter = new MyAdapter(elementList, this, this);
        myAdapter.setEditRights(isTopicOwner);
        recyclerView.setAdapter(myAdapter);
        elementList.add(selectedTopic);
        try {
            getDefaultComments();
        } catch (InterruptedException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        addTopicComments(getContext());
        addCommentButton.setOnClickListener(v -> addComment());
        return view;
    }


    private void addComment() {
        String commentContent = commentContentEt.getText().toString();
        if (!TextUtils.isEmpty(commentContent)) {
            Query query = databaseReference.orderByChild("title").equalTo(selectedTopic.getTitle());
            String userEmail = requireContext().getSharedPreferences(Constants.SHARED_PREFERENCES_USER_EMAIL, Context.MODE_PRIVATE).
                    getString("email", null);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            DataSnapshot authorEmail = dataSnapshot.child("authorEmail");
                            DataSnapshot commentList = dataSnapshot.child("commentList");
                            if (Objects.requireNonNull(authorEmail.getValue()).toString().equals(selectedTopic.getAuthorEmail())) {
                                Comment newComment = new Comment(commentContent, userEmail, false, false);
                                String commentKey = commentList.getRef().push().getKey();
                                assert commentKey != null;
                                commentList.child(commentKey).getRef().setValue(newComment);
                                newComment.setKey(commentKey);
                                elementList.add(newComment);
                                Toast.makeText(getContext(), Constants.SUCCESSFUL_COMMENT_ADDITION_MESSAGE, Toast.LENGTH_SHORT).show();
                                myAdapter.notifyDataSetChanged();
                                commentContentEt.setText(Constants.STRING_EMPTY);
                                return;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    Toast.makeText(getContext(), Constants.CANCELLED_COMMENT_ADDITION_MESSAGE, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            commentContentEt.setError(Constants.EMPTY_COMMENT_MESSAGE);
        }
    }

    private void getDefaultComments() throws InterruptedException {
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = Constants.DEFAULT_COMMENTS_URL;
        StringRequest getAlbumsRequest = new StringRequest(
                Request.Method.GET,
                url,
                response -> {
                    try {
                        handleCommentsResponse(response);
                    } catch (JSONException exception) {
                        Log.e(Constants.DEFAULT_COMMENTS_LOG_ERROR, exception.getMessage());
                    }
                },
                error -> Toast.makeText(getContext(), Constants.DEFAULT_COMMENTS_LOADING_ERROR, Toast.LENGTH_SHORT).show()
        );
        queue.add(getAlbumsRequest);
        Thread.sleep(Constants.THREAD_SLEEP_VALUE);
    }

    private void handleCommentsResponse(String response) throws JSONException {
        JSONArray photosJSONArray = new JSONArray(response);
        for (int index = 0; index < photosJSONArray.length(); ++index) {
            JSONObject commentJSON = (JSONObject) photosJSONArray.get(index);
            String content = commentJSON.getString("content");
            String authorEmail = commentJSON.getString("authorEmail");
            elementList.add(new Comment(content, authorEmail, false, true));
        }
        myAdapter.notifyDataSetChanged();
    }

    private void deleteNonDefaultComments() {
        Stack<Element> commentsToDelete = new Stack<>();
        for (Element element : elementList) {
            if (element instanceof Comment) {
                Comment comment = (Comment) element;
                if (!comment.isDefault()) {
                    commentsToDelete.push(element);
                }
            }
        }
        while (!commentsToDelete.empty()) {
            elementList.remove(commentsToDelete.pop());
        }
    }

    private void addTopicComments(Context context) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("topics");
        Query query = databaseReference.orderByChild("title").equalTo(selectedTopic.getTitle());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                deleteNonDefaultComments();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot comment : dataSnapshot.child("commentList").getChildren()) {
                            String authorEmail = Objects.requireNonNull(comment.child("authorEmail").getValue()).toString();
                            String content = Objects.requireNonNull(comment.child("content").getValue()).toString();
                            boolean isSolution = (boolean) comment.child("solution").getValue();
                            Comment newComment = new Comment(content, authorEmail, isSolution, false);
                            newComment.setKey(comment.getKey());
                            elementList.add(newComment);
                            myAdapter.notifyDataSetChanged();
                        }
                    }
                    Toast.makeText(context, Constants.COMMENT_LIST_UPDATE_MESSAGE, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(getContext(), Constants.CANCELLED_SPECIFIC_COMMENTS_ADDITION_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void deleteComment(Comment comment) {
        elementList.remove(comment);
        if (!comment.isDefault()) {
            deleteCommentFromDatabase(comment);
        } else {
            Toast.makeText(getContext(), Constants.SUCCESSFUL_DEFAULT_COMMENT_DELETION_MESSAGE, Toast.LENGTH_SHORT).show();
        }
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void markCommentAsSolution(Comment comment) {
        Query query = databaseReference.orderByChild("authorEmail").equalTo(selectedTopic.getAuthorEmail());
        String commentKey = comment.getKey();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String title = Objects.requireNonNull(dataSnapshot.child("title").getValue()).toString();
                        if (title.equals(selectedTopic.getTitle())) {
                            for (DataSnapshot comment : dataSnapshot.child("commentList").getChildren()) {
                                if ((boolean) comment.child("solution").getValue()) {
                                    comment.child("solution").getRef().setValue(false);
                                    setFalseSolution(comment.getKey());
                                }
                                if (Objects.equals(comment.getKey(), commentKey)) {
                                    comment.child("solution").getRef().setValue(true);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(getContext(), Constants.CANCELLED_COMMENT_SOLUTION_MARKING_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        });
        comment.setSolution(true);
        myAdapter.notifyDataSetChanged();
    }

    private void setFalseSolution(String commentKey) {
        for (Element element : elementList) {
            if (element instanceof Comment) {
                Comment comment = (Comment) element;
                if (!comment.isDefault()) {
                    if (comment.getKey().equals(commentKey)) {
                        comment.setSolution(false);
                    }
                }
            }
        }
        myAdapter.notifyDataSetChanged();
    }

    private void deleteCommentFromDatabase(Comment comment) {
        Query query = databaseReference.orderByChild("authorEmail").equalTo(selectedTopic.getAuthorEmail());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot commentList : dataSnapshot.child("commentList").getChildren()) {
                            if (Objects.equals(commentList.getKey(), comment.getKey())) {
                                commentList.getRef().removeValue().addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), Constants.SUCCESSFUL_DATABASE_COMMENT_DELETION_MESSAGE, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), Constants.FAILED_DELETION_MESSAGE, Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return;
                            }
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), Constants.CANCELLED_DELETION_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void updateSelectedTopic() {
        String titleText = ((TextView) Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(0)).itemView.findViewById(R.id.topic_title)).getText().toString();
        String descriptionText = ((TextView) Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(0)).itemView.findViewById(R.id.topic_description)).getText().toString();
        HashMap hashMap = new HashMap();
        hashMap.put("title", titleText);
        hashMap.put("description", descriptionText);
        Query query = databaseReference.orderByChild("title").equalTo(selectedTopic.getTitle());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String authorEmail = Objects.requireNonNull(dataSnapshot.child("authorEmail").getValue()).toString();
                        if (authorEmail.equals(selectedTopic.getAuthorEmail())) {
                            databaseReference.child(Objects.requireNonNull(dataSnapshot.getKey())).updateChildren(hashMap).addOnCompleteListener(task -> {
                                Toast.makeText(getContext(), Constants.SUCCESSFUL_TOPIC_UPDATE_MESSAGE, Toast.LENGTH_SHORT).show();
                                selectedTopic.setTitle(titleText);
                                selectedTopic.setDescription(descriptionText);
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(getContext(), Constants.CANCELLED_TOPIC_UPDATE_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        });
    }
}