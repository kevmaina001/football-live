package com.tvfootballhd.liveandstream.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.airbnb.lottie.LottieAnimationView;
import com.smarteist.autoimageslider.SliderView;
import com.tvfootballhd.liveandstream.R;

public final class FragmentHomeBinding implements ViewBinding {
    public final View headingLine;
    public final RelativeLayout headingRl;
    public final RelativeLayout leagueHeading;
    public final RecyclerView leagueNameRecyclerViewId;
    public final LinearLayout linearLayout;
    public final LottieAnimationView lottiId;
    public final ImageView menuIcon;
    public final RelativeLayout recentHeading;
    public final RecyclerView recentMatchRecyclerview;
    private final FrameLayout rootView;
    public final SearchView searchView;
    public final TextView seeMoreId;
    public final ConstraintLayout sliderLL;
    public final SliderView sliderLeagueUpcomingGame;
    public final TextView text;

    private FragmentHomeBinding(FrameLayout frameLayout, View view, RelativeLayout relativeLayout, RelativeLayout relativeLayout2, RecyclerView recyclerView, LinearLayout linearLayout2, LottieAnimationView lottieAnimationView, ImageView imageView, RelativeLayout relativeLayout3, RecyclerView recyclerView2, SearchView searchView2, TextView textView, ConstraintLayout constraintLayout, SliderView sliderView, TextView textView2) {
        this.rootView = frameLayout;
        this.headingLine = view;
        this.headingRl = relativeLayout;
        this.leagueHeading = relativeLayout2;
        this.leagueNameRecyclerViewId = recyclerView;
        this.linearLayout = linearLayout2;
        this.lottiId = lottieAnimationView;
        this.menuIcon = imageView;
        this.recentHeading = relativeLayout3;
        this.recentMatchRecyclerview = recyclerView2;
        this.searchView = searchView2;
        this.seeMoreId = textView;
        this.sliderLL = constraintLayout;
        this.sliderLeagueUpcomingGame = sliderView;
        this.text = textView2;
    }

    public FrameLayout getRoot() {
        return this.rootView;
    }

    public static FragmentHomeBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static FragmentHomeBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.fragment_home, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    public static FragmentHomeBinding bind(View view) {
        View view2 = view;
        int i = R.id.headingLine;
        View findChildViewById = ViewBindings.findChildViewById(view2, i);
        if (findChildViewById != null) {
            i = R.id.headingRl;
            RelativeLayout relativeLayout = (RelativeLayout) ViewBindings.findChildViewById(view2, i);
            if (relativeLayout != null) {
                i = R.id.leagueHeading;
                RelativeLayout relativeLayout2 = (RelativeLayout) ViewBindings.findChildViewById(view2, i);
                if (relativeLayout2 != null) {
                    i = R.id.leagueNameRecyclerViewId;
                    RecyclerView recyclerView = (RecyclerView) ViewBindings.findChildViewById(view2, i);
                    if (recyclerView != null) {
                        i = R.id.linearLayout;
                        LinearLayout linearLayout2 = (LinearLayout) ViewBindings.findChildViewById(view2, i);
                        if (linearLayout2 != null) {
                            i = R.id.lottiId;
                            LottieAnimationView lottieAnimationView = (LottieAnimationView) ViewBindings.findChildViewById(view2, i);
                            if (lottieAnimationView != null) {
                                i = R.id.menuIcon;
                                ImageView imageView = (ImageView) ViewBindings.findChildViewById(view2, i);
                                if (imageView != null) {
                                    i = R.id.recentHeading;
                                    RelativeLayout relativeLayout3 = (RelativeLayout) ViewBindings.findChildViewById(view2, i);
                                    if (relativeLayout3 != null) {
                                        i = R.id.recentMatchRecyclerview;
                                        RecyclerView recyclerView2 = (RecyclerView) ViewBindings.findChildViewById(view2, i);
                                        if (recyclerView2 != null) {
                                            i = R.id.searchView;
                                            SearchView searchView2 = (SearchView) ViewBindings.findChildViewById(view2, i);
                                            if (searchView2 != null) {
                                                i = R.id.seeMoreId;
                                                TextView textView = (TextView) ViewBindings.findChildViewById(view2, i);
                                                if (textView != null) {
                                                    i = R.id.sliderLL;
                                                    ConstraintLayout constraintLayout = (ConstraintLayout) ViewBindings.findChildViewById(view2, i);
                                                    if (constraintLayout != null) {
                                                        i = R.id.sliderLeagueUpcomingGame;
                                                        SliderView sliderView = (SliderView) ViewBindings.findChildViewById(view2, i);
                                                        if (sliderView != null) {
                                                            i = R.id.text;
                                                            TextView textView2 = (TextView) ViewBindings.findChildViewById(view2, i);
                                                            if (textView2 != null) {
                                                                return new FragmentHomeBinding((FrameLayout) view2, findChildViewById, relativeLayout, relativeLayout2, recyclerView, linearLayout2, lottieAnimationView, imageView, relativeLayout3, recyclerView2, searchView2, textView, constraintLayout, sliderView, textView2);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
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
