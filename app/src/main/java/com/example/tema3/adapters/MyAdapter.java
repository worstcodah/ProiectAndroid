package com.example.tema3.adapters;

import android.graphics.Color;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tema3.R;
import com.example.tema3.constants.Constants;
import com.example.tema3.interfaces.OnCommentClickListener;
import com.example.tema3.interfaces.OnTopicClickListener;
import com.example.tema3.interfaces.OnUpdateClickListener;
import com.example.tema3.models.Comment;
import com.example.tema3.models.Element;
import com.example.tema3.models.Topic;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Element> elementList;
    OnTopicClickListener onTopicClickListener;
    OnCommentClickListener onCommentClickListener;
    OnUpdateClickListener onUpdateClickListener;
    boolean canEdit = false;

    public MyAdapter(ArrayList<Element> elementList, OnTopicClickListener onTopicClickListener) {
        this.elementList = elementList;
        this.onTopicClickListener = onTopicClickListener;
    }

    public MyAdapter(ArrayList<Element> elementList, OnCommentClickListener onCommentClickListener, OnUpdateClickListener onUpdateClickListener) {
        this.elementList = elementList;
        this.onCommentClickListener = onCommentClickListener;
        this.onUpdateClickListener = onUpdateClickListener;
    }

    public MyAdapter(ArrayList<Element> elementList) {
        this.elementList = elementList;
    }

    public void setEditRights(boolean canEdit) {
        this.canEdit = canEdit;
    }

    @Override
    public int getItemViewType(int position) {
        if (elementList.get(position) instanceof Topic) {
            return 0;
        }
        if (elementList.get(position) instanceof Comment) {
            return 1;
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == 0) {
            View view = inflater.inflate(R.layout.item_topic, parent, false);
            return new TopicViewHolder(view);
        }
        if (viewType == 1) {
            View view = inflater.inflate(R.layout.item_comment, parent, false);
            return new CommentViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TopicViewHolder) {
            Topic topic = (Topic) elementList.get(position);
            ((TopicViewHolder) holder).bind(topic);
        }
        if (holder instanceof CommentViewHolder) {
            Comment comment = (Comment) elementList.get(position);
            ((CommentViewHolder) holder).bind(comment);
        }
    }

    @Override
    public int getItemCount() {
        return elementList.size();
    }

    class TopicViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView description;
        private final TextView authorEmail;
        private final Button updateTopicButton;

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.topic_title);
            description = itemView.findViewById(R.id.topic_description);
            authorEmail = itemView.findViewById(R.id.topic_author);
            updateTopicButton = itemView.findViewById(R.id.update_topic_button);
        }

        void bind(Topic topic) {
            title.setText(topic.getTitle());
            title.setClickable(canEdit);
            description.setClickable(canEdit);
            updateTopicButton.setVisibility(canEdit ? View.VISIBLE : View.GONE);
            description.setText(topic.getDescription());
            authorEmail.setText(topic.getAuthorEmail());
            if (onTopicClickListener != null) {
                itemView.setOnClickListener(v -> onTopicClickListener.openSelectedTopic(topic));
            } else {
                if (canEdit) {
                    title.setOnClickListener(v -> {
                        title.setCursorVisible(true);
                        title.setFocusableInTouchMode(true);
                        title.setInputType(InputType.TYPE_CLASS_TEXT);
                        title.requestFocus();
                    });
                    description.setOnClickListener(v -> {
                        description.setCursorVisible(true);
                        description.setFocusableInTouchMode(true);
                        description.setInputType(InputType.TYPE_CLASS_TEXT);
                        description.requestFocus();
                    });
                    updateTopicButton.setOnClickListener(v -> onUpdateClickListener.updateSelectedTopic());
                } else {
                    itemView.setOnClickListener(v -> {
                        topic.setSelected(!topic.isSelected());
                        LinearLayout itemLayout = itemView.findViewById(R.id.topic_layout);
                        itemLayout.setBackgroundColor(topic.isSelected() ? Color.GREEN : Color.parseColor(Constants.DARKER_GRAY_COLOR_CODE));
                    });
                }
            }
        }
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        private final TextView commentAuthor;
        private final TextView content;
        private final ImageView solutionImage;
        private final Button deleteButton;
        private final Button markSolutionButton;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentAuthor = itemView.findViewById(R.id.comment_author);
            content = itemView.findViewById(R.id.comment_content);
            solutionImage = itemView.findViewById(R.id.comment_solution_image);
            deleteButton = itemView.findViewById(R.id.delete_comment_button);
            markSolutionButton = itemView.findViewById(R.id.mark_solution_button);
        }

        void bind(Comment comment) {
            commentAuthor.setText(comment.getAuthorEmail());
            content.setText(comment.getContent());
            solutionImage.setVisibility(comment.isSolution() ? View.VISIBLE : View.GONE);
            deleteButton.setVisibility(canEdit ? View.VISIBLE : View.GONE);
            deleteButton.setOnClickListener(v -> onCommentClickListener.deleteComment(comment));
            markSolutionButton.setVisibility(comment.isSolution() || comment.isDefault() || !canEdit ? View.GONE : View.VISIBLE);
            markSolutionButton.setOnClickListener(v -> onCommentClickListener.markCommentAsSolution(comment));
        }
    }
}
