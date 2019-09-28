package com.example.iquarium;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.iquarium.R;


public class InputDialog extends DialogFragment{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        setCancelable(false);

        //Inflate layout
        builder.setView(inflater.inflate(R.layout.dialog, null));
        //Set buttons

        builder.setPositiveButton(R.string.btn_OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onDialogPositiveClick(InputDialog.this);

            }
        });


        builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onDialogNegativeClick(InputDialog.this);
            }
        });

        return builder.create();
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface InputDialogListener{

        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    InputDialogListener listener;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            listener = (InputDialogListener) activity;
        }catch(ClassCastException ex){
            throw new ClassCastException(activity.toString()
                    + " must implement InputDialogListener");
        }
    }


    private void toasMessage(String message)
    {
        Toast.makeText(getActivity(),message, Toast.LENGTH_LONG).show();
    }
}
