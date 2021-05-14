package com.openclassrooms.realestatemanager.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.utils.Utils;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class DialogCalculatorMortgage extends AlertDialog {

    // UI
    private TextView txtResult;
    private TextInputLayout inputPris;
    private TextInputLayout inputBring;
    private TextInputLayout inputTime;
    private TextInputLayout inputRate;
    private Button btnCalculate;

    private final Context context;
    private float price;

    public DialogCalculatorMortgage(Context context, float pris) {
        super(context);
        this.context = context;
        this.price = pris;
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

        Objects.requireNonNull(inputPris.getEditText()).setText(String.valueOf(price));

        btnCalculate.setOnClickListener(v -> Calculate());
    }

    private void Calculate() {
        if(!inputPris.getEditText().getText().toString().equals("")
        && !inputBring.getEditText().getText().toString().equals("")
        && !inputTime.getEditText().getText().toString().equals("")
        && !inputRate.getEditText().getText().toString().equals("")){
            price = Float.parseFloat(Objects.requireNonNull(inputPris.getEditText()).getText().toString());
            int bring = Integer.parseInt(Objects.requireNonNull(inputBring.getEditText()).getText().toString());
            int time = Integer.parseInt(Objects.requireNonNull(inputTime.getEditText()).getText().toString());
            float rate = Float.parseFloat(Objects.requireNonNull(inputRate.getEditText()).getText().toString());

            float monthlyPris = Utils.calculateMonthlyPayment(price, bring, time, rate);
            float totalPris = Utils.calculateTotalPriceOfProperty(monthlyPris, time);
            float profits = Utils.calculateTotalProfitsOfProperty(monthlyPris, time, (price - bring));

            txtResult.setVisibility(View.VISIBLE);
            txtResult.setText(context.getResources().getString(R.string.msg_part1_mortgage) + monthlyPris + "$ , the benefits is " + profits + "$" +context.getResources().getString(R.string.msg_part2_mortgage) + time + context.getResources().getString(R.string.msg_part3_mortgage) + totalPris + "$");

        }else{
            Toast.makeText(context, "Enter all fields !", Toast.LENGTH_LONG).show();
        }

    }
}
