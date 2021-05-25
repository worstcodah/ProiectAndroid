package com.example.tema3.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tema3.R;
import com.example.tema3.models.Element;
import com.example.tema3.models.Topic;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Element> elementList;

    public MyAdapter(ArrayList<Element> elementList) {
        this.elementList = elementList;
    }

    @Override
    public int getItemViewType(int position) {
        if (elementList.get(position) instanceof Topic) {
            return 0;
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
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class TopicViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView description;
        private TextView authorEmail;

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        void bind(Topic topic) {

        }
    }
}
