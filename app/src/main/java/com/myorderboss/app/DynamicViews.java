package com.myorderboss.app;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

public class DynamicViews {
    private Context context;


    public DynamicViews(Context context) {
        this.context = context;
    }

    public TextView descriptionTextView(Context context, String text){
        final ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView textView = new TextView(context);
        textView.setLayoutParams(lparams);
        textView.setTextSize(16);
        textView.setMinEms(5);
        textView.setTextColor(Color.BLACK);
        textView.setText(" " +text+" ");
        return textView;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public EditText receivedRoomLenghtEditText(Context context){
        final ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText editTextOne = new EditText(context);
        int id = 0;
        editTextOne.setTextSize(16);
        editTextOne.setMinEms(5);
        editTextOne.setTextColor(Color.BLACK);
        editTextOne.setId(id);
        editTextOne.setHintTextColor(Color.GRAY);
        editTextOne.setHint("meters");
        editTextOne.setGravity(Gravity.CENTER);
        editTextOne.setBackgroundResource(R.drawable.et_background);
        return editTextOne;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public EditText receivedRoomWidthEditText(Context context){
        final ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText editText = new EditText(context);
        int id = 1;
        editText.setTextSize(16);
        editText.setMinEms(5);
        editText.setTextColor(Color.BLACK);
        editText.setId(id);
        editText.setGravity(Gravity.CENTER);
        editText.setHintTextColor(Color.GRAY);
        editText.setHint("meters");
        editText.setBackgroundResource(R.drawable.et_background);
        return editText;
    }
}
