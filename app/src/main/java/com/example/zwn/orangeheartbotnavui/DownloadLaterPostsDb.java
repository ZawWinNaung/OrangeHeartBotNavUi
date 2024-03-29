package com.example.zwn.orangeheartbotnavui;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {DownloadLaterPosts.class}, version = 1)
public abstract class DownloadLaterPostsDb extends RoomDatabase {

    private static DownloadLaterPostsDb INSTANCE;

    public abstract DownloadLaterPostsDao daoAccess();

    static DownloadLaterPostsDb getDownloadLaterPostsDataBase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DownloadLaterPostsDb.class, MainActivity.DB_NAME)
// allow queries on the main thread.
// Don’t do this on a real app! See PersistenceBasicSample for an example.
// .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
