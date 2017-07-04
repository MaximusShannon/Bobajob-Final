package wit.bobajob.baj_maxshannon;

import wit.bobajob.baj_maxshannon.Objects.User;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Register extends AppCompatActivity {

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private Button registerButton;
    private EditText userName;
    private EditText email;
    private EditText passwordOne;
    private EditText confirmPassword;
    private CheckBox acceptTerms;
    private CheckBox acceptNewsLetterBox;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "Register.class";
    Boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null){
                    Register.this.loggedIn = false;
                }else{
                    Register.this.loggedIn = true;
                }

                invalidateOptionsMenu();

            }
        };


        //FIREBASE LOGIN STATE CHANGE
        //TODO: must make it display who's logged in somehow - project
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null)
                {
                    //User is signed in
                    Log.d(TAG, "onAuthChanged:signed_in:" + user.getUid());


                }else
                {
                    //User is signed out
                    Log.d(TAG, "onAuthChanged:signed_out");

                }
            }
        };

        registerButton = (Button) findViewById(R.id.register);
        userName = (EditText) findViewById(R.id.userNameIn);
        email = (EditText) findViewById(R.id.email);
        passwordOne = (EditText) findViewById(R.id.passwordOne);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        acceptTerms = (CheckBox) findViewById(R.id.acceptTermsBox);
        acceptNewsLetterBox = (CheckBox) findViewById(R.id.acceptedNewsLetterBox);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean userNameverf;
                boolean passwordverf = false;
                boolean termsverf = false;



                if (userName.getText().toString().matches("") || email.getText().toString().matches(""))
                    userNameverf = false;
                  else
                    userNameverf = true;

                //validate the password input
                if (confrimPassword(passwordOne.getText().toString(), confirmPassword.getText().toString())) {
                    passwordverf = true;
                } else {
                    passwordOne.setText("");
                    confirmPassword.setText("");
                }


                //validate that the terms and conditions are checked.
                if (acceptTerms.isChecked()) {
                    termsverf = true;
                } else {

                }


                boolean acceptedNewsLetter;
                //check if the news letter is checked.
                if (acceptNewsLetterBox.isChecked())
                    acceptedNewsLetter = true;
                else
                    acceptedNewsLetter = false;

                if(userNameverf && passwordverf && termsverf) {
                    createAccount(email.getText().toString(), passwordOne.getText().toString());

                    String authUserUUID = "";
                    User newUser = new User(userName.getText().toString(), email.getText().toString(), passwordOne.getText().toString(), authUserUUID, acceptedNewsLetter);
                    mDatabase.child("User").push().setValue(newUser);


                    Intent thankYou = new Intent(Register.this, ThankYou_Register.class);
                    thankYou.putExtra("userName", userName.getText().toString());
                    startActivity(thankYou);

                }else{
                    Toast.makeText(Register.this, "Registration failed! Please check the information provided", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void createAccount(String email, String password){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Toast.makeText(Register.this, "Authentication Complete", Toast.LENGTH_SHORT).show();
                        Log.v(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if(!task.isSuccessful()){
                            Toast.makeText(Register.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    protected void onStop(){
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public boolean confrimPassword(String p1, String p2) {

        boolean confirmedPass = false;
        if (p1.equals(p2) && p2.equals(p1) && !p1.equals("") && !p2.equals(""))
            confirmedPass = true;
        return confirmedPass;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(loggedIn)
            getMenuInflater().inflate(R.menu.menu_browse_loggedin, menu);
        else
            getMenuInflater().inflate(R.menu.menu_browse, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_register) {

            Intent openRegisterAct = new Intent(this, Register.class);
            startActivity(openRegisterAct);

            return true;
        }

        if(id == R.id.createAd)
        {

            if(loggedIn) {
                Intent openCreateAd = new Intent(Register.this, CreateAdAct.class);
                startActivity(openCreateAd);
            }else {
                Intent openLogin = new Intent(Register.this, Login.class);
                startActivity(openLogin);
                Toast.makeText(this, "Please Login/Register to create an Advert.", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        if(id == R.id.login){
            Intent logIn = new Intent(Register.this, Login.class);
            startActivity(logIn);
            return true;
        }

        if(id == R.id.browseAds)
        {
            Intent browse = new Intent(Register.this, Browse.class);
            startActivity(browse);
        }

        if(id == R.id.logout){
            Toast.makeText(this, "LOGOUTPRESSED", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
        }
        if(id == R.id.browseAds)
        {
            Intent openBrowse = new Intent(Register.this, CreateAdAct.class);
            startActivity(openBrowse);
        }

        return super.onOptionsItemSelected(item);
    }


    //needs work atm
    public int generatedUserId() {
        double userId1 = Math.random() * (200000 - 0) + 0;
        int userId = (int) userId1;

        return userId;
    }

}
