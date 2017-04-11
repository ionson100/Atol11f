package assa.com.example.user.atol11fstringvalue;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Map;



public class DialogEditKmmTable extends DialogFragment {



    private Tiny mTiny;
    private IActionE iActionE;
    private Map<Integer,String> mapT1;
    private Map<Integer,String> mapT2;





    public void setActite(Tiny mTity, Map<Integer,String> map1, Map<Integer,String> map2, IActionE iActionE){
        this.mTiny =mTity;
        this.iActionE=iActionE;
        this.mapT1 =map1;
        this.mapT2 =map2;


    }

    EditText editTextString;
    EditText editTextValue;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View mView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_edit_kmm_table, null);
        builder.setView(mView);
        mView.findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mView.findViewById(R.id.bt_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mTiny.table==1){
                    mTiny.value=editTextString.getText().toString();
                }

                if(mTiny.table==2){
                    int d=0;
                    try{
                        d=Integer.parseInt(editTextValue.getText().toString());
                    }catch (Exception e){}
                    mTiny.value=String.valueOf(d);
                }
                iActionE.action(mTiny);

                dismiss();
            }
        });

        editTextString= (EditText) mView.findViewById(R.id.edit_string);
        editTextValue= (EditText) mView.findViewById(R.id.edit_value);

        if(mTiny.table==1){
            editTextValue.setVisibility(View.GONE);
            editTextString.setText(mTiny.value);
            editTextString.setVisibility(View.VISIBLE);
        }

        if(mTiny.table==2){
            editTextString.setVisibility(View.GONE);
            editTextValue.setText(mTiny.value);
            editTextValue.setVisibility(View.VISIBLE);
        }

        TextView textView= (TextView) mView.findViewById(R.id.text_descriptions);

        if(mTiny.table==2){
            if(mapT2.containsKey(mTiny.purpose)){
                textView.setText(mapT2.get(mTiny.purpose));
            }else {
                textView.setVisibility(View.GONE);
            }
        }

        if(mTiny.table==1){
            if(mapT1.containsKey(mTiny.purpose)){
                textView.setText(mapT1.get(mTiny.purpose));
            }else {
                textView.setVisibility(View.GONE);
            }
        }
        TextView titul= (TextView) mView.findViewById(R.id.text_titul);

        if(mTiny.name!=null){
            titul.setText(mTiny.name);
        }

        return builder.create();
    }


}

