package be.example.petplanet.petplanet.Activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import be.example.petplanet.petplanet.R;

public class scoreDialog extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Dialog_Alert);
        builder.setTitle("Scanned")
                .setMessage("Score has been added")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent;
                        intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }
                });
        return builder.create();

    }
}
