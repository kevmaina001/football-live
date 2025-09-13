package com.tvfootballhd.liveandstream.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tvfootballhd.liveandstream.Activity.PrivacyPolicyActivity;
import com.tvfootballhd.liveandstream.R;

public class SettingFragment extends Fragment {
    private RelativeLayout aboutUsRelativeLayout;
    private RelativeLayout privacyPolicyRelativeLayout;
    private RelativeLayout rateMeRelativeLayout;
    private RelativeLayout shareRelativeLayout;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_setting, viewGroup, false);
        this.aboutUsRelativeLayout = (RelativeLayout) inflate.findViewById(R.id.aboutUsId);
        this.shareRelativeLayout = (RelativeLayout) inflate.findViewById(R.id.shareId);
        this.rateMeRelativeLayout = (RelativeLayout) inflate.findViewById(R.id.rateRLId);
        this.privacyPolicyRelativeLayout = (RelativeLayout) inflate.findViewById(R.id.privacyPolicyId);
        this.aboutUsRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog(getActivity().getString(R.string.about_us_text), "About Us");
            }
        });
        this.rateMeRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + getActivity().getPackageName())));
            }
        });
        this.shareRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareMe(view);
            }
        });
        this.privacyPolicyRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PrivacyPolicyActivity.class));

            }
        });
        return inflate;
    }



    public void shareMe(View view) {
        try {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.SUBJECT", "Share This Apps");
            intent.putExtra("android.intent.extra.TEXT", "\nLet me recommend you this application\n\nhttps://play.google.com/store/apps/details?id=" + getActivity().getPackageName());
            startActivity(Intent.createChooser(intent, "Choose one"));
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showAlertDialog(String str, String str2) {
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getContext());
        materialAlertDialogBuilder.setTitle((CharSequence) str2);
        materialAlertDialogBuilder.setMessage((CharSequence) str);
        materialAlertDialogBuilder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog create = materialAlertDialogBuilder.create();
        create.show();
        create.setCancelable(false);
    }
}
