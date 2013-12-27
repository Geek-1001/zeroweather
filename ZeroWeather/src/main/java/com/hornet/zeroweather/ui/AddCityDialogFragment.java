package com.hornet.zeroweather.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.hornet.zeroweather.R;

/**
 * Created by Ahmed on 26.12.13.
 */
public class AddCityDialogFragment extends DialogFragment {

    private DialogInterface.OnClickListener listener = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View mainDialogView = inflater.inflate(R.layout.dialog_add_city, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setPositiveButton("Add", listener);
        dialogBuilder.setView(mainDialogView);
        return dialogBuilder.create();
    }

    public void setListener(DialogInterface.OnClickListener listener) {
        this.listener = listener;
    }

}
