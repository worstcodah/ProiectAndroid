package com.example.tema3.interfaces;

import com.example.tema3.models.Comment;

public interface OnCommentClickListener {
    void deleteComment(Comment comment);
    void markCommentAsSolution(Comment comment);
}
