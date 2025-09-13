package com.tvfootballhd.liveandstream.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.tvfootballhd.liveandstream.Model.TopScorerResponse;
import com.tvfootballhd.liveandstream.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class PlayerStatesAdapter extends RecyclerView.Adapter<PlayerStatesAdapter.PlayerStatesAdapterHolder> {
    private Context context;
    private OnClickListener onClickListener;
    private ArrayList<TopScorerResponse.Response> responseArrayList;

    public interface OnClickListener {
        void onClick(int i);
    }

    public PlayerStatesAdapter(Context context2, ArrayList<TopScorerResponse.Response> arrayList) {
        this.context = context2;
        this.responseArrayList = arrayList;
    }

    public void setOnClickListener(OnClickListener onClickListener2) {
        this.onClickListener = onClickListener2;
    }

    public PlayerStatesAdapterHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new PlayerStatesAdapterHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.player_list, viewGroup, false), this.onClickListener);
    }

    public void onBindViewHolder(PlayerStatesAdapterHolder playerStatesAdapterHolder, int i) {
        TopScorerResponse.Response response = this.responseArrayList.get(i);
        if (response.getPlayer().getName() != null) {
            playerStatesAdapterHolder.name.setText(response.getPlayer().getName());
        }
        if (response.getStatistics().get(0).getTeam().getName() != null) {
            playerStatesAdapterHolder.team.setText(response.getStatistics().get(0).getTeam().getName());
        }
        if (response.getPlayer().getPhoto() != null) {
            Picasso.get().load(response.getPlayer().getPhoto()).into(playerStatesAdapterHolder.profileImage);
        }
    }

    public int getItemCount() {
        return this.responseArrayList.size();
    }

    public class PlayerStatesAdapterHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView profileImage;
        TextView team;

        public PlayerStatesAdapterHolder(View view, final OnClickListener onClickListener) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.playerNameId);
            this.team = (TextView) view.findViewById(R.id.playerTeamId);
            this.profileImage = (ImageView) view.findViewById(R.id.profileImageViewId);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    onClickListener.onClick(PlayerStatesAdapterHolder.this.getAdapterPosition());
                }
            });
        }
    }
}
