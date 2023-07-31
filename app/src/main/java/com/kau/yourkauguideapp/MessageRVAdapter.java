package com.kau.yourkauguideapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kau.yourkauguideapp.activities.MainActivity;

import java.util.ArrayList;

import android.os.Handler;

public class MessageRVAdapter extends RecyclerView.Adapter {

    // variable for our array list and context.
    public static ArrayList<ChatsModel> chatsModelArrayList;
    private UserViewHolder lastUserViewHolder;
    private BotViewHolder lastBotViewHolder;
    private final Context context;
    ArrayList<String> buttonOptions = new ArrayList<>();
    ArrayList<String> EmailsOptions = new ArrayList<>();
    ArrayList<String> PlanssOptions = new ArrayList<>();
    ArrayList<String> TraksOptions = new ArrayList<>();

    private final ButtonOptionsHandler buttonOptionsHandler;
    private final EmailsButtonOptionsHandler emailsOptionsHandler;
    private final PlansButtonOptionsHandler plansOptionsHandler;
    private final TraksButtonOptionsHandler traksOptionsHandler;
    private final MainActivity mainActivity;


    public MessageRVAdapter(ArrayList<ChatsModel> chatsModelArrayList, Context context, MainActivity mainActivity) {
        MessageRVAdapter.chatsModelArrayList = chatsModelArrayList;
        this.context = context;
        this.mainActivity = mainActivity;
        this.buttonOptionsHandler = new ButtonOptionsHandler(buttonOptions, context, mainActivity);
        this.emailsOptionsHandler = new EmailsButtonOptionsHandler(EmailsOptions, context, mainActivity);
        this.plansOptionsHandler = new PlansButtonOptionsHandler(PlanssOptions, context, mainActivity);
        this.traksOptionsHandler = new TraksButtonOptionsHandler(TraksOptions, context, mainActivity);


        buttonOptions.add("جدول الاختبارات");
        buttonOptions.add("مواقع تسكين القاعات");
        buttonOptions.add("تغيير التخصص");
        buttonOptions.add("التحويل الى كلية الحاسبات");
        buttonOptions.add("شروط معادلة المواد");
        buttonOptions.add("الخطط الدراسية والمواد");
        buttonOptions.add("المسارات");
        buttonOptions.add("الدورات");
        buttonOptions.add("متطلبات التخرج");
        buttonOptions.add("العودة الى المحادثة");

        EmailsOptions.add("أيميل دكتور حسام لحظه");
        EmailsOptions.add("أيميل دكتور وجدي الغامدي");
        EmailsOptions.add("أيميل دكتور معصتم جراح");
        EmailsOptions.add("أيميل دكتور مرشد الدربالي");
        EmailsOptions.add("أيميل دكتور محمد باحداد");
        EmailsOptions.add("العودة الى المحادثة");


        PlanssOptions.add("خطة تقنية المعلومات");
        PlanssOptions.add("خطة نظم المعلومات");
        PlanssOptions.add("خطة علوم الحاسبات");

        TraksOptions.add("مسار تقنية المعلومات");
        TraksOptions.add("مسار نظم المعلومات");
        TraksOptions.add("مسار علوم الحاسب");


    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        // below code is to switch our
        // layout type along with view holder.
        switch (viewType) {
            case 0:
                // below line we are inflating user message layout.
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_msg, parent, false);
                return new UserViewHolder(view);
            case 1:
                // below line we are inflating bot message layout.
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bot_msg, parent, false);
                return new BotViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // this method is use to set data to our layout file.
        ChatsModel modal = chatsModelArrayList.get(position);
        switch (modal.getSender()) {
            case "user":
                // below line is to set the text to our text view of user layout
                ((UserViewHolder) holder).userTV.setText(modal.getMessage());
                lastUserViewHolder = (UserViewHolder) holder;

                break;
            case "bot":
                // below line is to set the text to our text view of bot layout
                ((BotViewHolder) holder).botTV.setText(modal.getMessage());
                lastBotViewHolder = (BotViewHolder) holder;
                System.out.println(modal.getMessage());

                if (modal.getMessage().equals("اسف لم افهم ماذا تعني\n هذه بعض الأقتراحات لك")) {
                    buttonOptionsHandler.showButtonOptions(((BotViewHolder) holder).linearLayout, (BotViewHolder) holder);
                    modal.setMessage("تمام بحاول اساعدك ...");
                    return;
                }
                if (modal.getMessage().equals("خطة اي قسم؟")) {
                    plansOptionsHandler.showButtonOptions(((BotViewHolder) holder).linearLayout, (BotViewHolder) holder);
                    modal.setMessage("تمام بحاول احصل الخطة ...");
                    return;
                }

                if (modal.getMessage().equals("ايميل دكتور مين؟")) {
                    emailsOptionsHandler.showButtonOptions(((BotViewHolder) holder).linearLayout, (BotViewHolder) holder);
                    modal.setMessage("تمام بحاول احصل الايميل ...");
                    return;
                }

                if (modal.getMessage().equals("مسارات اي قسم؟")) {
                    traksOptionsHandler.showButtonOptions(((BotViewHolder) holder).linearLayout, (BotViewHolder) holder);
                    modal.setMessage("جاري البحث عن المسارات ...");
                    return;
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        // return the size of array list
        return chatsModelArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        // below line of code is to set position.
        switch (chatsModelArrayList.get(position).getSender()) {
            case "user":
                return 0;
            case "bot":
                return 1;
            default:
                return -1;
        }
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        // creating a variable
        // for our text view.
        TextView userTV;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing with id.
            userTV = itemView.findViewById(R.id.user_textMessage);
        }
    }

    public static class BotViewHolder extends RecyclerView.ViewHolder {

        // creating a variable
        // for our text view.
        TextView botTV;
        Button button;
        LinearLayout linearLayout;

        public BotViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing with id.
            botTV = itemView.findViewById(R.id.bot_textMessage);
            button = itemView.findViewById(R.id.bot_button);
            linearLayout = itemView.findViewById(R.id.linearButtons);

        }

    }

}

