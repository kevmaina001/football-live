package com.tvfootballhd.liveandstream.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.airbnb.lottie.LottieAnimationView;
import com.tvfootballhd.liveandstream.R;

public final class FragmentTournamentBinding implements ViewBinding {
    public final View headingLine;
    public final RelativeLayout headingRl;
    public final SearchView leagueSearchViewId;
    public final LinearLayout leagueSearchViewLL;
    public final LottieAnimationView lottiId;
    public final ImageView menuIcon;
    public final RecyclerView recyclerView;
    private final FrameLayout rootView;

    private FragmentTournamentBinding(FrameLayout frameLayout, View view, RelativeLayout relativeLayout, SearchView searchView, LinearLayout linearLayout, LottieAnimationView lottieAnimationView, ImageView imageView, RecyclerView recyclerView2) {
        this.rootView = frameLayout;
        this.headingLine = view;
        this.headingRl = relativeLayout;
        this.leagueSearchViewId = searchView;
        this.leagueSearchViewLL = linearLayout;
        this.lottiId = lottieAnimationView;
        this.menuIcon = imageView;
        this.recyclerView = recyclerView2;
    }

    public FrameLayout getRoot() {
        return this.rootView;
    }

    public static FragmentTournamentBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static FragmentTournamentBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.fragment_tournament, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    public static FragmentTournamentBinding bind(View view) {
        int i = R.id.headingLine;
        View findChildViewById = ViewBindings.findChildViewById(view, i);
        if (findChildViewById != null) {
            i = R.id.headingRl;
            RelativeLayout relativeLayout = (RelativeLayout) ViewBindings.findChildViewById(view, i);
            if (relativeLayout != null) {
                i = R.id.leagueSearchViewId;
                SearchView searchView = (SearchView) ViewBindings.findChildViewById(view, i);
                if (searchView != null) {
                    i = R.id.leagueSearchViewLL;
                    LinearLayout linearLayout = (LinearLayout) ViewBindings.findChildViewById(view, i);
                    if (linearLayout != null) {
                        i = R.id.lottiId;
                        LottieAnimationView lottieAnimationView = (LottieAnimationView) ViewBindings.findChildViewById(view, i);
                        if (lottieAnimationView != null) {
                            i = R.id.menuIcon;
                            ImageView imageView = (ImageView) ViewBindings.findChildViewById(view, i);
                            if (imageView != null) {
                                i = R.id.recyclerView;
                                RecyclerView recyclerView2 = (RecyclerView) ViewBindings.findChildViewById(view, i);
                                if (recyclerView2 != null) {
                                    return new FragmentTournamentBinding((FrameLayout) view, findChildViewById, relativeLayout, searchView, linearLayout, lottieAnimationView, imageView, recyclerView2);
                                }
                            }
                        }
                    }
                }
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i)));
    }
}
