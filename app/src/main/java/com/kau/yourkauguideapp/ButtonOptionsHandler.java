package com.kau.yourkauguideapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.kau.yourkauguideapp.activities.MainActivity;

import java.util.ArrayList;

public class ButtonOptionsHandler {
    private final ArrayList<String> buttonOptions;
    Context context;
    public int optionIndex = 0;

    private final MainActivity mainActivity;

    public ButtonOptionsHandler(ArrayList<String> buttonOptions, Context context, MainActivity mainActivity) {
        this.buttonOptions = buttonOptions;
        this.context = context;
        this.mainActivity = mainActivity;
    }

    public void showButtonOptions(ViewGroup linearLayout, MessageRVAdapter.BotViewHolder holder) {

        linearLayout.setVisibility(View.VISIBLE);
        linearLayout.removeAllViews();

        for (int i = optionIndex; i < Math.min(optionIndex + 2, buttonOptions.size()); i++) {
            Button button = createButton(buttonOptions.get(i), holder);
            linearLayout.addView(button);
        }

        if (optionIndex + 2 < buttonOptions.size()) {
            Button button = createButton("خيارات أخرى", holder);
            linearLayout.addView(button);
        }
    }

    private Button createButton(String option, MessageRVAdapter.BotViewHolder holder) {
        Button button = new Button(holder.itemView.getContext());
        button.setText(option);
        button.setPadding(10, 5, 10, 5);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 10, 0, 0);
        button.setBackgroundResource(R.drawable.button_background_lightgreen);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buttonText = button.getText().toString();

                if (buttonText.equals("خيارات أخرى")) {
                    optionIndex += 2;
                    showButtonOptions(holder.linearLayout, holder);
                    return;
                }
                if (buttonText.equals("العودة الى المحادثة")) {
                    optionIndex = 0;
                    LinearLayout linearLayout = holder.linearLayout;
                    linearLayout.setVisibility(View.GONE);
                    linearLayout.removeAllViews();
                    return;

                }


                String botProcessBut = buttonText;


                // Remove the buttons after clicking
                LinearLayout linearLayout = holder.linearLayout;
                linearLayout.setVisibility(View.GONE);
                linearLayout.removeAllViews();
                optionIndex = 0;
                mainActivity.ProsesMsg(botProcessBut);


                //remove the buttons after clicking
                holder.linearLayout.removeAllViews();

            }
        });

        button.setLayoutParams(layoutParams);
        return button;
    }


}
