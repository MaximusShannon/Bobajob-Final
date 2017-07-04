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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterInformation extends AppCompatActivity {


    Button login;
    Button register;
    Button continueBrowsing;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        login.findViewById(R.id.login);
        register.findViewById(R.id.register);
        continueBrowsing.findViewById(R.id.continueBrowsing);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent openRegisterAct = new Intent(RegisterInformation.this, Register.class);
                startActivity(openRegisterAct);
                finish();
            }
        });

        continueBrowsing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent openBrowse = new Intent(RegisterInformation.this, Browse.class);
                startActivity(openBrowse);
                finish();
            }
        });




    }
    @Override
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
            Boolean loggedIn;

            mAuthListener = new FirebaseAuth.AuthStateListener(){
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if(user == null){

                        //open activity to make user register.


                    }else{
                        //open create add
                        Intent openCreateAdAct = new Intent(RegisterInformation.this, CreateAdAct.class);
                        startActivity(openCreateAdAct);
                    }

                }
            };

            Intent openCreateAdAct = new Intent(this, CreateAdAct.class);
            startActivity(openCreateAdAct);
            return true;
        }




        return super.onOptionsItemSelected(item);
    }


}
