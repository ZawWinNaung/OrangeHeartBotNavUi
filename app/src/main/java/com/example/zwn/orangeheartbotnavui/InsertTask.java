package com.example.zwn.orangeheartbotnavui;

import android.os.AsyncTask;

public class InsertTask extends AsyncTask<DownloadLaterPosts, Void, Boolean> {
    private DownloadLaterPostsDb db;

    InsertTask(DownloadLaterPostsDb db) {
        this.db = db;
    }

    @Override
    protected Boolean doInBackground(DownloadLaterPosts... posts) {
        db.daoAccess().insertPost(posts[0]);
        return true;
    }
}
