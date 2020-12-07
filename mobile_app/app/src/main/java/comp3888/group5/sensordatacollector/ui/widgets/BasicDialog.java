package comp3888.group5.sensordatacollector.ui.widgets;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import comp3888.group5.sensordatacollector.R;

public class BasicDialog extends AppCompatDialogFragment {
    private final int ID;
    private View view;
    public BasicDialog(int id){
        ID = id;
    }
    public BasicDialog(int id, View view){
        ID = id;
        this.view = view;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if(view == null) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            view = inflater.inflate(ID, null);
        }
        builder.setView(view)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }

}
