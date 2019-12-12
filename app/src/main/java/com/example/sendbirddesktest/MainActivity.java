package com.example.sendbirddesktest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.sendbird.android.BaseChannel;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.sendbird.android.UserMessage;
import com.sendbird.desk.android.SendBirdDesk;
import com.sendbird.desk.android.Ticket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SendBird.init("192F73AB-EED6-465B-9B77-1F34406B5658", this);
        SendBirdDesk.init();

        // Connect to SendBird Chat
        SendBird.connect("test_user_1","ac029769b3f1091d7c292c192ce4b1246be17ffe", new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                if (e != null) {
                    Log.d("test", "Ran into error calling SendBird.connect() ");
                    return;
                }

                // Connect to SendBird Desk
                SendBirdDesk.authenticate("test_user_1", "ac029769b3f1091d7c292c192ce4b1246be17ffe", new SendBirdDesk.AuthenticateHandler() {
                    @Override
                    public void onResult(SendBirdException e) {

                        // Create Ticket
                        Ticket.create("Ticket7", "test_user_1", new Ticket.CreateHandler() {
                            @Override
                            public void onResult(Ticket ticket, SendBirdException e) {
                                if (e != null) {
                                    // Error handling.
                                    Log.d("test", "Ran into error calling Ticket.create() " + e.getMessage());
                                    return;
                                }

                                // Send Message to Ticket (Without a message from a customer, a ticket will not be placed in the queue and
                                // show on the Dashboard.
                                ticket.getChannel().sendUserMessage("test message", new BaseChannel.SendUserMessageHandler() {
                                    @Override
                                    public void onSent(UserMessage userMessage, SendBirdException e) {
                                        if (e != null) {
                                            // Error handling.
                                            Log.d("test", "Ran into error calling sendUserMessage() " + e.getMessage());

                                            return;
                                        }
                                    }
                                });
                            }
                        });

                        // Retrieve previously closed tickets
                        Ticket.getClosedList(0, new Ticket.GetClosedListHandler() {
                            @Override
                            public void onResult(List<Ticket> list, boolean b, SendBirdException e) {
                                Log.d("test", "Closed list size: " + String.valueOf(list.size()));

                                for (Ticket ticket  : list) {
                                    Log.d("test", "Ticket Title: " + ticket.getTitle());
                                }
                            }
                        });
                    }
                });
            }
        });

        setContentView(R.layout.activity_main);
    }
}
