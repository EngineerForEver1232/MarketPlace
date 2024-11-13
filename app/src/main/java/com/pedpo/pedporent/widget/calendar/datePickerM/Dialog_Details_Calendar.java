package com.pedpo.pedporent.widget.calendar.datePickerM;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AlertDialog;
import com.pedpo.pedporent.R;


public class Dialog_Details_Calendar implements View.OnClickListener {

    private ImageButton imgClose ;
    private  AlertDialog dialog ;

    private static Dialog_Details_Calendar dialog_details_calendar = new Dialog_Details_Calendar();

    public static Dialog_Details_Calendar newInstance()
    {
        return dialog_details_calendar ;
    }

    public void showDialog(Context context) {

        if (dialog!=null && dialog.isShowing())
            dialog.dismiss();

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_details_calendar, null);
        builder.setView(view);

        imgClose = view.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(this);



        dialog = builder.create();


        dialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.imgClose:
                dialog.cancel();
                break;
        }

    }

}
