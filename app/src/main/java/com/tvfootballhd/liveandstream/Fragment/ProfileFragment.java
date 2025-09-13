package com.tvfootballhd.liveandstream.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.tvfootballhd.liveandstream.AdsManager;
import com.tvfootballhd.liveandstream.Model.PlayerStateResponse;

import java.text.DecimalFormat;
import com.tvfootballhd.liveandstream.R;

public class ProfileFragment extends Fragment {
    private TextView ageTV;
    private TextView heightTV;
    private PlayerStateResponse playerStateResponse;
    private TextView positionTV;
    private TextView ratingTV;
    private TextView shirtTV;
    private TextView teamTV;
    private TextView weightTV;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_profile, viewGroup, false);
        AdsManager.LoadInterstitialAd2(getActivity());
        this.playerStateResponse = (PlayerStateResponse) getActivity().getIntent().getSerializableExtra("PlayerStateResponseClass");
        this.heightTV = (TextView) inflate.findViewById(R.id.heightId);
        this.weightTV = (TextView) inflate.findViewById(R.id.weightId);
        this.ageTV = (TextView) inflate.findViewById(R.id.ageId);
        this.teamTV = (TextView) inflate.findViewById(R.id.teamNameId);
        this.positionTV = (TextView) inflate.findViewById(R.id.positionId);
        this.shirtTV = (TextView) inflate.findViewById(R.id.shirtNumberId);
        this.ratingTV = (TextView) inflate.findViewById(R.id.ratingId);
        PlayerStateResponse.Response response = this.playerStateResponse.getResponse().get(this.playerStateResponse.getResponse().size() - 1);
        this.heightTV.setText(response.getPlayer().getHeight());
        this.weightTV.setText(response.getPlayer().getWeight());
        this.ageTV.setText(response.getPlayer().getAge() + "");
        this.teamTV.setText(response.getStatistics().get(0).getTeam().getName());
        this.positionTV.setText(response.getStatistics().get(0).getGames().getPosition());
        if (response.getStatistics().get(0).getGames().getRating() != null) {
            double parseDouble = Double.parseDouble(response.getStatistics().get(0).getGames().getRating());
            this.ratingTV.setText(new DecimalFormat("0.00").format(parseDouble));
        } else {
            this.ratingTV.setText("-");
        }
        if (response.getPlayer().getNationality() != null) {
            this.shirtTV.setText(response.getPlayer().getNationality());
        } else {
            this.shirtTV.setText("-");
        }
        return inflate;
    }
}
