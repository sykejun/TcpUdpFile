package com.kejun.trans;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.kejun.trans.core.transmission.Transmission;
import com.kejun.trans.dialog.TransConfirmDialogFragment;
import com.kejun.trans.model.Equipment;


public class ConfirmDialogActivity extends AppCompatActivity {

    public static TransConfirmDialogFragment.OnTransmissionConfirmResultListener onTransmissionConfirmResultListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Transmission transmission = (Transmission) intent.getSerializableExtra("transmission");
        Equipment equipment = (Equipment) intent.getSerializableExtra("equipment");
        TransConfirmDialogFragment transConfirmDialogFragment = new TransConfirmDialogFragment(transmission, equipment);
        transConfirmDialogFragment.show(getSupportFragmentManager(), null);
        transConfirmDialogFragment.setOnTransmissionConfirmResultListener(onTransmissionConfirmResultListener);
    }
}
