package com.tvfootballhd.liveandstream.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.tvfootballhd.liveandstream.Adapter.PlayerStateAdapter;
import com.tvfootballhd.liveandstream.AdsManager;
import com.tvfootballhd.liveandstream.Model.PlayerStateResponse;
import com.tvfootballhd.liveandstream.R;

public class PlayerStateFragment extends Fragment {
    private PlayerStateResponse playerStateResponse;
    private RecyclerView recyclerView;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_player_state, viewGroup, false);
        AdsManager.LoadInterstitialAd2(getActivity());
        this.playerStateResponse = (PlayerStateResponse) getActivity().getIntent().getSerializableExtra("PlayerStateResponseClass");
        RecyclerView recyclerView2 = (RecyclerView) inflate.findViewById(R.id.recyclerView);
        this.recyclerView = recyclerView2;
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
        this.recyclerView.setAdapter(new PlayerStateAdapter(this.playerStateResponse, getContext()));
        return inflate;
    }
}
