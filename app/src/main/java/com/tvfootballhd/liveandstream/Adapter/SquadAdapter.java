package com.tvfootballhd.liveandstream.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.tvfootballhd.liveandstream.Model.SquadResponse;
import com.tvfootballhd.liveandstream.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class SquadAdapter extends RecyclerView.Adapter<SquadAdapter.SquadAdapterHolder> {
    private Context context;
    private EmranClickListener emranClickListener;
    private OnClickListener onClickListener;
    private ArrayList<SquadResponse.Player> responseArrayList;

    public interface EmranClickListener {
        void onClick(int i);
    }

    public interface OnClickListener {
        void onClick(int i);
    }

    public SquadAdapter(Context context2, ArrayList<SquadResponse.Player> arrayList) {
        this.context = context2;
        this.responseArrayList = arrayList;
    }

    public void setEmranClickListener(EmranClickListener emranClickListener2) {
        this.emranClickListener = emranClickListener2;
    }

    public void setOnClickListener(OnClickListener onClickListener2) {
        this.onClickListener = onClickListener2;
    }

    public SquadAdapterHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new SquadAdapterHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.squad_list, viewGroup, false), this.emranClickListener);
    }

    public void onBindViewHolder(SquadAdapterHolder squadAdapterHolder, int i) {
        SquadResponse.Player player = this.responseArrayList.get(i);
        if (player.getName() != null) {
            squadAdapterHolder.name.setText(player.getName());
        }
        if (player.getPosition() != null) {
            squadAdapterHolder.positionTV.setText(player.getPosition());
        }
        if (player.getPhoto() != null) {
            Picasso.get().load(player.getPhoto()).into(squadAdapterHolder.profileImage);
        }
        squadAdapterHolder.numberTV.setText(String.valueOf(player.getNumber()));
    }

    public int getItemCount() {
        return this.responseArrayList.size();
    }

    public class SquadAdapterHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView numberTV;
        TextView positionTV;
        ImageView profileImage;

        public SquadAdapterHolder(View view, EmranClickListener emranClickListener) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.playerNameId);
            this.positionTV = (TextView) view.findViewById(R.id.positionId);
            this.numberTV = (TextView) view.findViewById(R.id.playerNumberId);
            this.profileImage = (ImageView) view.findViewById(R.id.profileImageViewId);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    emranClickListener.onClick(getAdapterPosition());

                }
            });
        }


    }
}
