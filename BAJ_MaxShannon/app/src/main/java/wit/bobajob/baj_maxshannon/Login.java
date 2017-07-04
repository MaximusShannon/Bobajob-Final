package wit.bobajob.baj_maxshannon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    Button register;
    Button login;
    EditText email, password;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    FirebaseAuth.AuthStateListener mAuthListener;

    boolean loggedIn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null){
                    Login.this.loggedIn = false;
                }else{
                    Login.this.loggedIn = true;
                }

                invalidateOptionsMenu();

            }
        };

        register = (Button)findViewById(R.id.register);
        login = (Button) findViewById(R.id.login);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email.setText("");
            }
        });
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password.setText("");
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent openReg = new Intent(Login.this, Register.class);
                startActivity(openReg);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(!task.isSuccessful()){
                            Toast.makeText(Login.this, "Login Failed - Check your details", Toast.LENGTH_SHORT).show();
                        }else{
                            Intent openBrowse = new Intent(Login.this, Browse.class);
                            startActivity(openBrowse);
                            Toast.makeText(Login.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
                });
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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

        if(id == R.id.browseAds)
        {
            Intent browse = new Intent(Login.this, Browse.class);
            startActivity(browse);
        }

        if(id == R.id.createAd)
        {
            if(loggedIn) {
                Intent openCreateAd = new Intent(Login.this, CreateAdAct.class);
                startActivity(openCreateAd);
            }else {
                Toast.makeText(this, "Please Login/Register to create an Advert.", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

}
