package com.collegeninja.college.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.fdscollege.college.R;

public class ShowMessageDialog {
    public void displayDialog(Activity activity, String msg){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_message);
        TextView text = (TextView) dialog.findViewById(R.id.yn_dialog_message);
        text.setText(msg);

        Button dialogButton = (Button) dialog.findViewById(R.id.yndialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
