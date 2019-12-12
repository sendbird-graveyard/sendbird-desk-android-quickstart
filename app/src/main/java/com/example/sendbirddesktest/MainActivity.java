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
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SendBird.init("192F73AB-EED6-465B-9B77-1F34406B5658", this);
        SendBirdDesk.init();


        SendBird.connect("test_user_1","ac029769b3f1091d7c292c192ce4b1246be17ffe", new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                if (e != null) {
                    Log.d("test", "mulgar ran into error connecting");

                    return;
                }

                SendBirdDesk.authenticate("test_user_1", "ac029769b3f1091d7c292c192ce4b1246be17ffe", new SendBirdDesk.AuthenticateHandler() {
                    @Override
                    public void onResult(SendBirdException e) {
                        Map<String,String> Ticket1CustomFields = new HashMap<String, String>()
                        {
                            {
                                put("1", "value1");
                                put("2", "value2");
                            }
                        };


                        Ticket.create("Ticket7", "test", null, Ticket1CustomFields, new Ticket.CreateHandler() {
                            @Override
                            public void onResult(Ticket ticket, SendBirdException e) {
                                if (e != null) {
                                    // Error handling.
                                    Log.d("test", "mulgar ran into error creating ticket" + e.getMessage());
                                    return;
                                }

                                // Now you can send messages to the ticket by ticket.getChannel().sendUserMessage() or sendFileMessage().
                                Log.d("test", "mulgar did not run into error creating ticket");
                                ticket.getChannel().sendUserMessusage("testdsfdsa", new BaseChannel.SendUserMessageHandler() {
                                    @Override
                                    public void onSent(UserMessage userMessage, SendBirdException e) {
                                        if (e != null) {
                                            // Error handling.
                                            Log.d("test", "mulgar ran into error sending message");

                                            return;
                                        }

                                        Log.d("test", "mulgar did not run into error sending message");

                                    }
                                });



                            }
                        });
                        /*
                        Map<String,String> customFieldFilter = new HashMap<String, String>()
                        {
                            {
                                put("Field1", "value1");
                                put("Field2", "value2");
                            }
                        };

                        Ticket.getClosedList(0, customFieldFilter, new Ticket.GetClosedListHandler() {
                            @Override
                            public void onResult(List<Ticket> list, boolean b, SendBirdException e) {
                                Log.d("test", "mulgar closed list" + String.valueOf(list.size()));
                            }
                        });

                        Ticket.getOpenedList(0, customFieldFilter, new Ticket.GetOpenedListHandler() {
                            @Override
                            public void onResult(List<Ticket> list, boolean b, SendBirdException e) {
                                Log.d("test", "mulgar opened list" + String.valueOf(list.size()));
                            }
                        });
                        */
                    }
                });
            }
        });

        setContentView(R.layout.activity_main);
    }
}
