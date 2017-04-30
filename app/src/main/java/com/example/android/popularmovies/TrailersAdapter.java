package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersAdapterViewHolder> {


    private  String[] trailerName;
    private String[] trailerKey;
    private final TrailerAdapterOnClickHandler mClickHandler;
    public interface TrailerAdapterOnClickHandler {
        void onClick(String key);
    }
    public TrailersAdapter(TrailerAdapterOnClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }

    @Override
    public TrailersAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new TrailersAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailersAdapterViewHolder holder, int position) {
            holder.trailerName1.setText(trailerName[position]);
            holder.playIcon.setImageResource(R.drawable.playfilled);
    }

    @Override
    public int getItemCount() {
        if (null == trailerName) return 0;
        return trailerName.length;
    }

    public void setTrailersData(ArrayList<Trailer> trailersData){
        if (trailersData!=null){
            trailerName=new String[trailersData.size()];
            trailerKey=new String[trailersData.size()];
            for (int i=0;i<trailersData.size();i++){
                Trailer currentTrailer=trailersData.get(i);
                trailerName[i]=currentTrailer.getTrailerName();
                trailerKey[i]=currentTrailer.getTrailerKey();
            }
            notifyDataSetChanged();
        }else {
            trailerName=null;
            trailerKey=null;
        }
    }



    public class TrailersAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView trailerName1;
        public final ImageView playIcon;

        public TrailersAdapterViewHolder(View itemView) {
            super(itemView);
            trailerName1=(TextView)itemView.findViewById(R.id.trailer_name);
            playIcon=(ImageView)itemView.findViewById(R.id.trailer_list_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickHandler.onClick(trailerKey[getAdapterPosition()]);
        }
    }
}
