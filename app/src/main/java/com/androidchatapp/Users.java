package com.androidchatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidchatapp.Adapters.RecyclerAdapter;
import com.androidchatapp.Model.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class Users extends AppCompatActivity {
   // ListView usersList;
    TextView noUsersText;
    ArrayList<UserModel> userlist;
    int totalUsers = 0;
    ProgressDialog pd;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private DatabaseReference mDatabase;
    UserModel item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        pd = new ProgressDialog(Users.this);
        userlist = new ArrayList<>();

        //usersList = (ListView)findViewById(R.id.usersList);
        noUsersText = (TextView)findViewById(R.id.noUsersText);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        fetchImage();

        // String url = "https://android-chat-app-e711d.firebaseio.com/users.json";
        String url = "https://androidchatapp-85f77.firebaseio.com/user.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(Users.this);
        rQueue.add(request);

      /*  recyclerView.addOnItemTouchListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.chatWith = userlist.get(position);
                startActivity(new Intent(Users.this, Chat.class));
            }
        });*/

        recyclerView.addOnItemTouchListener(
                new MyRecyclerItemClickListener(getApplication(), new MyRecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        item = new UserModel();
                        item = userlist.get(position);
                        UserDetails.chatWith = item.getName();
                        startActivity(new Intent(Users.this, Chat.class));
                    }
                })
        );
    }

    public void fetchImage(){
        //displaying progress dialog while fetching images
        pd.setMessage("Please wait...");
        pd.show();
        mDatabase = FirebaseDatabase.getInstance().getReference("profile_image");

        //adding an event listener to fetch values
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //dismissing the progress dialog
                pd.dismiss();

                //iterating through all the values in database
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    item = new UserModel();
                    item = postSnapshot.getValue(UserModel.class);
                    String value = item.getName();
                    String userprofile = item.getPro_thumbnail();
                    item.setPro_thumbnail(userprofile);
                    if(!value.equals(UserDetails.username)) {
                        userlist.add(item);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pd.dismiss();
            }
        });
    }

    public void doOnSuccess(String s){

        pd.setMessage("Loading...");
        pd.show();

        try {
            JSONObject obj = new JSONObject(s);
            Iterator i = obj.keys();
            String key = "";

            while(i.hasNext()){
                key = i.next().toString();

                if(!key.equals(UserDetails.username)) {
                    item = new UserModel();
                    item.setName(key);
                    //userlist.add(item);
                }
                totalUsers++;

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(totalUsers <=1){
            noUsersText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else{
            noUsersText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            //usersList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al));
            adapter = new RecyclerAdapter(getApplication(),userlist);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        pd.dismiss();
    }
}