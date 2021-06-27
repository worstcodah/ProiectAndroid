package com.example.tema3.interfaces;

import com.example.tema3.models.Topic;

public interface TopicsActivityFragmentCommunication {
    void openTopicsFragment();

    void openSelectedTopic(Topic topic);

    void openAddTopicFragment();
}