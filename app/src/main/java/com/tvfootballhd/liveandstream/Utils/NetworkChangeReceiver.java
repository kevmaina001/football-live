package com.tvfootballhd.liveandstream.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tvfootballhd.liveandstream.R;

public class NetworkChangeReceiver extends BroadcastReceiver {
    AlertDialog alertDialog;

    public void onReceive(Context context, Intent intent) {
        if (isNetworkConnected(context)) {
            AlertDialog alertDialog2 = this.alertDialog;
            if (alertDialog2 != null) {
                alertDialog2.dismiss();
                return;
            }
            return;
        }
        showAlertDialogForNoNetwork(context);
    }

    private void showAlertDialogForNoNetwork(Context context) {
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(context);
        materialAlertDialogBuilder.setView(LayoutInflater.from(context).inflate(R.layout.alert_dialog_for_no_internet, (ViewGroup) null));
        AlertDialog create = materialAlertDialogBuilder.create();
        this.alertDialog = create;
        create.setCancelable(true);
        this.alertDialog.show();
    }

    private boolean isNetworkConnected(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
