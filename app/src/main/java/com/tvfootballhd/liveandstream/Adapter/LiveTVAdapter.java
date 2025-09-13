package com.tvfootballhd.liveandstream.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.tvfootballhd.liveandstream.Model.LiveTvResponse;
import com.tvfootballhd.liveandstream.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class LiveTVAdapter extends RecyclerView.Adapter<LiveTVAdapter.LiveTVAdapterHolder> {
    private Context context;
    private OnclickListener onclickListener;
    private ArrayList<LiveTvResponse> responseArrayList;

    public interface OnclickListener {
        void onClick(int i);
    }

    public void setOnclickListener(OnclickListener onclickListener2) {
        this.onclickListener = onclickListener2;
    }

    public LiveTVAdapter(Context context2, ArrayList<LiveTvResponse> arrayList) {
        this.context = context2;
        this.responseArrayList = arrayList;
    }

    public LiveTVAdapterHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new LiveTVAdapterHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tv_channel_list, viewGroup, false), this.onclickListener);
    }

    public void onBindViewHolder(LiveTVAdapterHolder liveTVAdapterHolder, int i) {
        LiveTvResponse liveTvResponse = this.responseArrayList.get(i);
        if (liveTvResponse.getChannel_title() != null) {
            liveTVAdapterHolder.channelNameTV.setText(liveTvResponse.getChannel_title());
        }
        if (liveTvResponse.getChannel_thumbnail() != null) {
            Picasso.get().load(liveTvResponse.getChannel_thumbnail()).into(liveTVAdapterHolder.channelImage);
        }
    }

    public int getItemCount() {
        return this.responseArrayList.size();
    }

    public class LiveTVAdapterHolder extends RecyclerView.ViewHolder {
        ImageView channelImage;
        TextView channelNameTV;

        public LiveTVAdapterHolder(View view, final OnclickListener onclickListener) {
            super(view);
            this.channelNameTV = (TextView) view.findViewById(R.id.channelNameId);
            this.channelImage = (ImageView) view.findViewById(R.id.channelImageView);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    onclickListener.onClick(LiveTVAdapterHolder.this.getAdapterPosition());
                }
            });
        }
    }
}
