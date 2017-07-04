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
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ThankYou_CreateAd extends AppCompatActivity {

    boolean loggedIn;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener mAuthListener;
    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you__create_ad);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView thumbs = (ImageView) findViewById(R.id.thumbsUp);
        thumbs.setImageResource(R.drawable.thumbsuo);

        Button createAnotherAdd = (Button) findViewById(R.id.browseAds);
        Button goToDash = (Button) findViewById(R.id.browseTradesmen);

        createAnotherAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent openCreatedAdAct = new Intent(ThankYou_CreateAd.this, CreateAdAct.class);
                startActivity(openCreatedAdAct);
                finish();
            }
        });

        goToDash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent openDashboard = new Intent(ThankYou_CreateAd.this, Dashboard.class);
                startActivity(openDashboard);
                finish();
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    ThankYou_CreateAd.this.loggedIn = false;
                } else {
                    ThankYou_CreateAd.this.loggedIn = true;
                    ThankYou_CreateAd.this.currentUserId = user.getUid();

                }

                invalidateOptionsMenu();

            }
        };


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
        if (loggedIn) {
            getMenuInflater().inflate(R.menu.menu_browse_loggedin, menu);
        } else
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

        if (id == R.id.createAd) {

            if (loggedIn) {
                Intent openCreateAd = new Intent(ThankYou_CreateAd.this, CreateAdAct.class);
                startActivity(openCreateAd);
                finish();
            } else {
                Intent openLogin = new Intent(ThankYou_CreateAd.this, Login.class);
                startActivity(openLogin);
                Toast.makeText(this, "Please Login/Register to create an Advert.", Toast.LENGTH_SHORT).show();
                finish();
            }
            return true;
        }

        if (id == R.id.login) {

            Intent logIn = new Intent(ThankYou_CreateAd.this, Login.class);
            startActivity(logIn);
            finish();

            return true;
        }

        if (id == R.id.logout) {
            mAuth.signOut();
            finish();
        }


        if (id == R.id.myTrade) {
            Intent openTradeTab = new Intent(ThankYou_CreateAd.this, MyTrade.class);
            startActivity(openTradeTab);
            finish();
        }

        if (id == R.id.browseTradesmen) {
            Intent openTradesMen = new Intent(ThankYou_CreateAd.this, BrowseTradesMen.class);
            startActivity(openTradesMen);
            finish();
        }

        if (id == R.id.dashboard) {
            Intent openDashboard = new Intent(ThankYou_CreateAd.this, Dashboard.class);
            startActivity(openDashboard);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
