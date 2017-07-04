package wit.bobajob.baj_maxshannon;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Dashboard extends AppCompatActivity {
    // this is to be completed in the project - I haven't worked out how I want it to look yet.

    Button createAdvert;
    Button createTradeProfile;
    Button browseAdverts;
    Button browseTradesmen;
    Button logout;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener mAuthListener;

    boolean loggedIn;
    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        createAdvert = (Button) findViewById(R.id.createAd);
        createTradeProfile = (Button) findViewById(R.id.createTradeProfile);
        browseAdverts = (Button) findViewById(R.id.browseAds);
        browseTradesmen = (Button) findViewById(R.id.browseTradesmen);
        //logout = (Button) findViewById(R.id.logout);

        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null){
                    Dashboard.this.loggedIn = false;
                }else{
                    Dashboard.this.loggedIn = true;
                    Dashboard.this.currentUserId = user.getUid();

                }
                invalidateOptionsMenu();
            }
        };


        createOnClicks();
    }


    public void createOnClicks(){

        createAdvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent openCreate = new Intent(Dashboard.this, CreateAdAct.class);
                startActivity(openCreate);

            }
        });

        createTradeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent openCreateTrade = new Intent(Dashboard.this, MyTrade.class);
                startActivity(openCreateTrade);

            }
        });

        browseAdverts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent openBrowse = new Intent(Dashboard.this, Browse.class);
                startActivity(openBrowse);

            }
        });

        browseTradesmen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent openTradesmenBrowse = new Intent(Dashboard.this, BrowseTradesMen.class);
                startActivity(openTradesmenBrowse);
            }
        });

    }

    @Override
    public void onStop(){
        super.onStop();

        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(loggedIn){
            getMenuInflater().inflate(R.menu.menu_browse_loggedin, menu);
        }else
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
                Intent openCreateAd = new Intent(Dashboard.this, CreateAdAct.class);
                startActivity(openCreateAd);
            }else {
                Intent openLogin = new Intent(Dashboard.this, Login.class);
                startActivity(openLogin);
                Toast.makeText(this, "Please Login/Register to create an Advert.", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        if(id == R.id.login){

            Intent logIn = new Intent(Dashboard.this, Login.class);
            startActivity(logIn);

            return true;
        }

        if(id == R.id.logout){
            mAuth.signOut();
        }



        if(id == R.id.myTrade)
        {
            Intent openTradeTab = new Intent(Dashboard.this, MyTrade.class);
            startActivity(openTradeTab);
        }

        if(id == R.id.browseTradesmen)
        {
            Intent openTradesMen = new Intent(Dashboard.this, BrowseTradesMen.class);
            startActivity(openTradesMen);
        }

        if(id == R.id.browseAds)
        {
            Intent openBrowse = new Intent(Dashboard.this, CreateAdAct.class);
            startActivity(openBrowse);
        }


        return super.onOptionsItemSelected(item);
    }
}
