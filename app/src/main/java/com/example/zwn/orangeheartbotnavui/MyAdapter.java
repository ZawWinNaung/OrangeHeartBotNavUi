package com.example.zwn.orangeheartbotnavui;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements Filterable {
    private List<Posts> postsList = new ArrayList<>();
    private List<Posts> postsListFull;
    private Context context;
    private DownloadLaterPosts downloadLaterPosts;

    MyAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    void setData(List<Posts> postsList) {
        this.postsList = postsList;
        postsListFull = new ArrayList<>(postsList);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull final MyAdapter.MyViewHolder viewHolder, final int i) {
        final Posts posts = postsList.get(i);
        if (postsList.size()-1 == i) {
            GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) viewHolder.contentView.getLayoutParams();
            layoutParams.setMargins(0, 16, 0, 16);
            viewHolder.contentView.setLayoutParams(layoutParams);
        }

        viewHolder.txtArtist.setText(posts.getArtist());
        viewHolder.txtTitle.setText(posts.getTitle());

        int tabIconColor = ContextCompat.getColor(context, R.color.colorPrimary);
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        circularProgressDrawable.setStrokeWidth(8);
        circularProgressDrawable.setCenterRadius(30);
        circularProgressDrawable.start();
        Glide.with(viewHolder.imgCoverArt).load(posts.getImageUrl()).placeholder(circularProgressDrawable).into(viewHolder.imgCoverArt);

        final Uri uri = Uri.parse(posts.getLink());
        viewHolder.txtDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });
        viewHolder.menuMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = v.getContext();
                PopupMenu popupMenu = new PopupMenu(context, v);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.menu_for_tracks, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        DownloadLaterPostsDb db;
                        int i1 = item.getItemId();
//                        if (i1 == R.id.menuReport) {
//                            return true;
//                        } else
                        if (i1 == R.id.menuDownloadLater) {
                            db = DownloadLaterPostsDb.getDownloadLaterPostsDataBase(context);
                            downloadLaterPosts = new DownloadLaterPosts();
                            downloadLaterPosts.setArtist(posts.getArtist());
                            downloadLaterPosts.setTitle(posts.getTitle());
                            downloadLaterPosts.setLink(posts.getLink());
                            downloadLaterPosts.setImageUrl(posts.getImageUrl());
                            try {
                                Boolean success = new InsertTask(db).execute(downloadLaterPosts).get();
                                if (success) {
                                    Toast.makeText(context, "Added to download later", Toast.LENGTH_SHORT).show();
                                }
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                            return true;
                        } else if (i1 == R.id.menuShare) {
                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.putExtra(Intent.EXTRA_TEXT, posts.getArtist() + "-" + posts.getTitle() + "\n" + posts.getLink());
                            shareIntent.setType("text/plain");
                            context.startActivity(Intent.createChooser(shareIntent, "Orange Heart"));
                        }
                        return false;
                    }
                });
            }
        });
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

    @Override
    public Filter getFilter() {
        return postFilter;
    }

    private Filter postFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Posts> filteredPosts = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredPosts.addAll(postsListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Posts posts : postsListFull) {
                    if (posts.getTitle().toLowerCase().contains(filterPattern) || posts.getArtist().toLowerCase().contains(filterPattern)) {
                        filteredPosts.add(posts);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredPosts;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            postsList.clear();
            postsList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
