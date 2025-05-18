package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView resultTv, solutionTv;
    private String dataToCalculate = "";


    private MaterialButton buttonAC, buttonC,
            buttonBrackOpen, buttonBrackClose,
            buttonDivide, buttonMultiply, buttonPlus, buttonMinus, buttonEquals,
            button0, button1, button2, button3, button4, button5, button6, button7, button8, button9,
            buttonDot;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        resultTv   = findViewById(R.id.result_tv);
        solutionTv = findViewById(R.id.solution_tv);


        buttonAC         = init(R.id.button_ac);
        buttonC          = init(R.id.button_c);
        buttonBrackOpen  = init(R.id.button_open_bracket);
        buttonBrackClose = init(R.id.button_close_bracket);
        buttonDivide     = init(R.id.button_divide);
        buttonMultiply   = init(R.id.button_multiply);
        buttonPlus       = init(R.id.button_add);
        buttonMinus      = init(R.id.button_subtract);
        buttonEquals     = init(R.id.button_equals);
        button0 = init(R.id.button_0);  button1 = init(R.id.button_1);
        button2 = init(R.id.button_2);  button3 = init(R.id.button_3);
        button4 = init(R.id.button_4);  button5 = init(R.id.button_5);
        button6 = init(R.id.button_6);  button7 = init(R.id.button_7);
        button8 = init(R.id.button_8);  button9 = init(R.id.button_9);
        buttonDot = init(R.id.button_dot);
    }

    private MaterialButton init(int id) {
        MaterialButton b = findViewById(id);
        b.setOnClickListener(this);
        return b;
    }


    @Override public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.button_ac) {
            clearAll();
            return;
        }

        if (id == R.id.button_c) {
            deleteLast();
        } else if (id == R.id.button_equals) {
            evaluateExpression();
            return;
        } else {
            appendToExpression(((MaterialButton) v).getText().toString());
        }

        updateResult();
    }


    private void clearAll() {
        dataToCalculate = "";
        solutionTv.setText("");
        resultTv.setText("0");
    }

    private void deleteLast() {
        if (!dataToCalculate.isEmpty()) {
            dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
            solutionTv.setText(dataToCalculate);
        }
    }

    private void evaluateExpression() {
        String res = resultTv.getText().toString();
        if (!res.equals("Err")) {
            dataToCalculate = res;
            solutionTv.setText(res);
        }
    }

    private void appendToExpression(String val) {
        dataToCalculate += val;
        solutionTv.setText(dataToCalculate);
    }

    private void updateResult() {

        if (dataToCalculate.isEmpty()) {
            resultTv.setText("0");
            return;
        }
        char last = dataToCalculate.charAt(dataToCalculate.length() - 1);
        boolean isSafeToEval =
                Character.isDigit(last) || last == ')';

        if (!isSafeToEval) {
            return;
        }

        String res = getResult(dataToCalculate);
        if (!res.equals("Err")) {
            resultTv.setText(res);
        }
    }

    private String getResult(String expr) {
        if (expr.isEmpty()) return "0";
        try {
            Context ctx = Context.enter();
            ctx.setOptimizationLevel(-1);
            Scriptable scope = ctx.initStandardObjects();
            Object raw = ctx.evaluateString(scope, expr, "calc", 1, null);
            String r = raw.toString();
            return r.endsWith(".0") ? r.replace(".0", "") : r;
        } catch (Exception e) {
            return "Err";
        } finally {
            Context.exit();
        }
    }
}
