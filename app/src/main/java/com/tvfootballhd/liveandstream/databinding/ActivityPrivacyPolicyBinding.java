package com.tvfootballhd.liveandstream.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.viewbinding.ViewBinding;

import com.tvfootballhd.liveandstream.R;

public final class ActivityPrivacyPolicyBinding implements ViewBinding {
    private final LinearLayout rootView;

    private ActivityPrivacyPolicyBinding(LinearLayout linearLayout) {
        this.rootView = linearLayout;
    }

    public LinearLayout getRoot() {
        return this.rootView;
    }

    public static ActivityPrivacyPolicyBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static ActivityPrivacyPolicyBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.activity_privacy_policy, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    public static ActivityPrivacyPolicyBinding bind(View view) {
        if (view != null) {
            return new ActivityPrivacyPolicyBinding((LinearLayout) view);
        }
        throw new NullPointerException("rootView");
    }
}
