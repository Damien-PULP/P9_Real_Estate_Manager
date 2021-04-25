package com.openclassrooms.realestatemanager.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.bumptech.glide.util.Util;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.utils.Utils;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DialogCalculatorMortgage extends AlertDialog {

    // UI
    private TextView txtResult;
    private TextInputLayout inputPris;
    private TextInputLayout inputBring;
    private TextInputLayout inputTime;
    private TextInputLayout inputRate;
    private Button btnCalculate;

    private Context context;
    private float pris;

    public DialogCalculatorMortgage(Context context, float pris) {
        super(context);
        this.context = context;
        this.pris = pris;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        View content = LayoutInflater.from(context).inflate(R.layout.dialog_calculator_mortgage, null);
        setView(content);
        onConfigureDialog(content);
        super.onCreate(savedInstanceState);
    }

    private void onConfigureDialog(View content) {
        inputPris = content.findViewById(R.id.dialog_calculator_mortgage_pris);
        inputBring = content.findViewById(R.id.dialog_calculator_mortgage_bring);
        inputTime = content.findViewById(R.id.dialog_calculator_mortgage_time);
        inputRate = content.findViewById(R.id.dialog_calculator_mortgage_rate);
        btnCalculate = content.findViewById(R.id.dialog_calculator_mortgage_btn);
        txtResult = content.findViewById(R.id.dialog_calculator_mortgage_result_txt);

        inputPris.getEditText().setText(String.valueOf(pris));

        btnCalculate.setOnClickListener(v -> Calculate());
    }

    private void Calculate() {
        pris = Float.parseFloat(inputPris.getEditText().getText().toString());
        int bring = Integer.parseInt(inputBring.getEditText().getText().toString());
        int time = Integer.parseInt(inputTime.getEditText().getText().toString());
        float rate = Float.parseFloat(inputRate.getEditText().getText().toString());

        float monthlyPris = Utils.calculateMonthlyPayment(pris, bring, time, rate);
        float totalPris = Utils.calculateTotalPrisOfProperty(monthlyPris, time);

        txtResult.setVisibility(View.VISIBLE);
        txtResult.setText("Your monthly payment is " + monthlyPris + "$ \n and the total pris on " + time + " years is " + totalPris + "$");

    }
}
