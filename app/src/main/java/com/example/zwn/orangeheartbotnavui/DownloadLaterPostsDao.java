package com.example.zwn.orangeheartbotnavui;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface DownloadLaterPostsDao {
    @Insert
    void insertPost(DownloadLaterPosts posts);

    @Delete
    void  deletePost(DownloadLaterPosts posts);

    @Query("SELECT * FROM DownloadLaterPosts")
    LiveData<List<DownloadLaterPosts>> getAllPost();
}
