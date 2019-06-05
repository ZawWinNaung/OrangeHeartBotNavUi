package com.example.zwn.orangeheartbotnavui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

public class DownloadLaterAdapter extends RecyclerView.Adapter<DownloadLaterAdapter.MyViewHolder> {
    private List<DownloadLaterPosts> postsList;
    private Context context;
    private DownloadLaterPostsDb db;

    DownloadLaterAdapter(List<DownloadLaterPosts> postsList, Context context, DownloadLaterPostsDb db) {
        this.postsList = postsList;
        this.context = context;
        this.db = db;
    }

    @NonNull
    @Override
    public DownloadLaterAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.download_later_item, viewGroup, false);
        return new DownloadLaterAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadLaterAdapter.MyViewHolder myViewHolder, final int i) {
        final DownloadLaterPosts posts = postsList.get(i);
        if (i == postsList.size() - 1) {
            GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) myViewHolder.contentView.getLayoutParams();
            layoutParams.setMargins(0, 16, 0, 16);
            myViewHolder.contentView.setLayoutParams(layoutParams);
        }

        myViewHolder.txtArtist.setText(posts.getArtist());
        myViewHolder.txtTitle.setText(posts.getTitle());
        Glide.with(myViewHolder.imgCoverArt).load(posts.getImageUrl()).into(myViewHolder.imgCoverArt);
        myViewHolder.txtDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(posts.getLink());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });
        myViewHolder.menuMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = v.getContext();
                PopupMenu popupMenu = new PopupMenu(context, v);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.menu_for_download_later_tracks, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menuReport:
                                Toast.makeText(context, "Reported to server", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.menuRemove:
                                Toast.makeText(context, "Track removed from download later", Toast.LENGTH_SHORT).show();
                                new DeleteTask(db).execute(posts);
                                postsList.remove(i);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
            }
        });
    }

    void setPostsList(List<DownloadLaterPosts> postList) {
        this.postsList = postList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout contentView;
        TextView txtArtist, txtTitle, txtDownload;
        ImageView imgCoverArt, menuMore;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            contentView = itemView.findViewById(R.id.content);
            txtArtist = itemView.findViewById(R.id.txtArtist);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDownload = itemView.findViewById(R.id.txtDownload);
            imgCoverArt = itemView.findViewById(R.id.imgCoverArt);
            menuMore = itemView.findViewById(R.id.menuMore);
        }
    }

    private static class DeleteTask extends AsyncTask<DownloadLaterPosts, Void, Void> {

        private DownloadLaterPostsDb db;

        DeleteTask(DownloadLaterPostsDb db) {
            this.db = db;
        }

        @Override
        protected Void doInBackground(DownloadLaterPosts... posts) {
            db.daoAccess().deletePost(posts[0]);
            return null;
        }
    }
}
