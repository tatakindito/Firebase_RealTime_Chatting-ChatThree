package com.example.firebasechat_typec;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Message> mArrayList;
    private CustomAdapter mAdapter;
    private int count = -1;

    private HashMap<String, String> dict1;
    private HashMap<String, String> dict2;
    private HashMap<String, String> dict3;
    private HashMap<String, String> dict4;
    private HashMap<String, String> dict5;
    private HashMap<String, String> dict6;

    private ArrayList<HashMap<String, String>> arrMap;

    private Button startBtn;
    private Button disBtn;

    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        disBtn = findViewById(R.id.disbutton);
        disBtn.setOnClickListener(this);


        startBtn = findViewById(R.id.startbutton);
        startBtn.setOnClickListener(this);
        initializeMapData();
        mAuth = FireHelper.getInstance().AuthInit();

    }

    public void initializeMapData() {

        arrMap =  new ArrayList<HashMap<String, String>>();

        dict1 = new HashMap<>();
        dict1.put("name", "bill");
        dict1.put("email", "bill@fbase.com");
        dict1.put("uid", "Pb1RuwnOHUS48TwEce0oRl3eccw2");

        arrMap.add(0, dict1);

        dict2 = new HashMap<>();
        dict2.put("name", "john");
        dict2.put("email", "john@fbase.com");
        dict2.put("uid", "AL1u83276Aappb8BQEdhlHgVnUb2");

        arrMap.add(1, dict2);

        dict3 = new HashMap<>();
        dict3.put("name", "heri");
        dict3.put("email", "babarian@fbase.com");
        dict3.put("uid", "FrFUq07lJuOFY9WC3kssawAJZPf1");

        arrMap.add(2, dict3);

        dict4 = new HashMap<>();
        dict4.put("name", "tyas");
        dict4.put("email", "tyasdarma19@gmail.com");
        dict4.put("uid", "jzI5je5a3QfY3dfxhIl3c9NergW2");

        arrMap.add(3, dict4);

        dict5 = new HashMap<>();
        dict5.put("name", "nilson");
        dict5.put("email", "nilson@fbase.com");
        dict5.put("uid", "59Bb43YpEZWhrv2quu52LZKIMIm2");

        arrMap.add(4, dict5);

        dict6 = new HashMap<>();
        dict6.put("name", "tatak");
        dict6.put("email", "tatakindito80@gmail.com");
        dict6.put("uid", "o9uVUjIBMZRloE5BO0nYcsDdXBp2");

        arrMap.add(5, dict6);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.disbutton:

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(StartActivity.this);
//                builderSingle.setIcon(R.drawable.ic_send_black_24dp);
                builderSingle.setTitle("Select!!..");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(StartActivity.this, android.R.layout.select_dialog_singlechoice);
                arrayAdapter.add("bill");
                arrayAdapter.add("john");
                arrayAdapter.add("heri");
                arrayAdapter.add("tyas");
                arrayAdapter.add("nilson");
                arrayAdapter.add("tatak");

                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);

                        HashMap<String, String> mapSelect = arrMap.get(which);

                        String str = mapSelect.get("email").trim();
                        String email = mapSelect.get("email").trim();

                        disBtn.setText(str);

                        signIn(email, "17121004");

                    }
                });
                builderSingle.show();

                break;

            case R.id.startbutton:


                break;

            default:

                break;
        }

    }

    private String subStringName (String str) {

        String[] subString = str.split("@");

        if(subString == null) {
            return "";
        }

        return subString[0];
    }


    private void signIn(final String email, String password) {
        Log.d("", "signIn:" + email);

        // [START sign_in_with_email]
        Task<AuthResult> authResultTask = mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("", "signInWithEmail:success");


                            // Write a message to the database
                            FirebaseDatabase database = FirebaseDatabase.getInstance("https://chatthree-2683a-default-rtdb.firebaseio.com/");
                            DatabaseReference myRef = database.getReference("chat");

                            FChat friendlyMessage = new FChat(email, subStringName(email), mAuth.getCurrentUser().getUid());
                            myRef.push().setValue(friendlyMessage);


                            startActivity(new Intent(StartActivity.this, MainActivity.class));
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("", "signInWithEmail:failure", task.getException());
                            Toast.makeText(StartActivity.this, "   " + task.getException() +"   Authentication failed.", Toast.LENGTH_SHORT).show();

                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {

                        }

                        // [END_EXCLUDE]
                    }
                });


        // [END sign_in_with_email]
    }

}