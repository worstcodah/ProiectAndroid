package com.example.tema3.interfaces;

import com.example.tema3.models.Topic;

public interface ManagementActivityFragmentCommunication {
    void openManagementFragment();

    void openAddTopicFragment();

    void deleteSelectedTopics();

    void openSelectedTopic(Topic topic, boolean canEdit);
}
