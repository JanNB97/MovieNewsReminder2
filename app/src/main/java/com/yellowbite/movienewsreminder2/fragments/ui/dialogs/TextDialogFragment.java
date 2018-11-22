package com.yellowbite.movienewsreminder2.fragments.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.yellowbite.movienewsreminder2.R;

public class TextDialogFragment extends DialogFragment
{
    private DialogClickListener dialogClickListener;

    private TextView movieTextView;

    public static TextDialogFragment newInstance(DialogClickListener callOnPositiveClicked)
    {
        TextDialogFragment alertDialogFragment = new TextDialogFragment();
        alertDialogFragment.setDialogClickListener(callOnPositiveClicked);
        return alertDialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = super.getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog, null);
        builder.setView(view);
        this.movieTextView = view.findViewById(R.id.wishedMoviesDialogEditText);

        builder.setMessage("Film hinzufügen")
                .setPositiveButton("OK", (dialogInterface, i) -> {
                    try
                    {
                        this.dialogClickListener.onDialogPositiveClicked(movieTextView.getText().toString());
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                })
                .setNegativeButton("Negative", (dialogInterface, i) -> {});

        return builder.create();
    }

    public void setDialogClickListener(DialogClickListener dialogClickListener)
    {
        this.dialogClickListener = dialogClickListener;
    }

    public interface DialogClickListener
    {
        void onDialogPositiveClicked(String name);
    }
}
