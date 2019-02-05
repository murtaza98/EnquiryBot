package com.example.murtaza.enquirybot;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatWindow extends AppCompatActivity {

    LinearLayout chatScroll;
    LayoutInflater inflater;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        final TextView enterMessage = findViewById(R.id.enterMessage);
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url ="http://13.233.158.98:8888/chat/";
        scrollView = findViewById(R.id.scrollView);


        chatScroll = findViewById(R.id.chat_scroll);
        inflater =  (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        ImageView sendMessage = findViewById(R.id.sendMessage);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!enterMessage.getText().toString().equals("")) {
                    updateUI("message", enterMessage.getText().toString());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            updateUI("response", response.trim());
                            scrollToBottom();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            updateUI("error", error.getMessage());
                        }
                    }){
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String>  params = new HashMap<>();
                        params.put("question", "What is hostel fees?");
                        return params;
                    }};
                    queue.add(stringRequest);
                }
                scrollToBottom();
            }
        });

    }

    private void scrollToBottom(){
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    private void updateUI(String type, String message) {
        View view = inflater.inflate(R.layout.chat, null);
        if (type.equals("message")) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.START;
            params.bottomMargin = 12;
            params.rightMargin = 40;
            view.setLayoutParams(params);
            TextView enterMessage = findViewById(R.id.enterMessage);
            enterMessage.setText("");
            TextView text = view.findViewById(R.id.message);
            text.setText(message);
            text.setPadding(0,0,10, 0);
            view.setBackgroundColor(0xffffffff);
            chatScroll.addView(view);
        } else if (type.equals("response")) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.END;
            params.bottomMargin = 12;
            params.leftMargin = 40;
            view.setLayoutParams(params);
            TextView text = view.findViewById(R.id.message);
            text.setText(message);
            text.setPadding(10, 0, 0, 0);
            view.setBackgroundColor(0xfaedb000);
            chatScroll.addView(view);
        } else if (type.equals("error")) {
            TextView text = view.findViewById(R.id.message);
            message = "error"+message;
            text.setText(message);
            view.setBackgroundColor(0x22222222);
            chatScroll.addView(view);
        }
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat df = new SimpleDateFormat("h:mm a");
        String date = df.format(currentTime);
        TextView text = view.findViewById(R.id.time);
        text.setText(date);
    }
}
