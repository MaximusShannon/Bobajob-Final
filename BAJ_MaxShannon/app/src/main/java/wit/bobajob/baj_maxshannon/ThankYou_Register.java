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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ThankYou_Register extends AppCompatActivity {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener mAuthListener;
    boolean loggedIn;
    String currentUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you__register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    ThankYou_Register.this.loggedIn = false;
                } else {
                    ThankYou_Register.this.loggedIn = true;
                    ThankYou_Register.this.currentUserId = user.getUid();

                }

                invalidateOptionsMenu();

            }
        };

        Bundle extras = getIntent().getExtras();
        String temp = extras.getString("userName");

        TextView welcome = (TextView) findViewById(R.id.welcomeText);
        welcome.setText("Welcome " + temp + "!");

        ImageView thumbsUp = (ImageView) findViewById(R.id.thumbsUp);
        thumbsUp.setImageResource(R.drawable.thumbsuo);

        //Buttons nav
        final Button browse = (Button) findViewById(R.id.browseAds);
        Button goToDash = (Button) findViewById(R.id.goToDash);

        Button browseTradesmen = (Button) findViewById(R.id.browseTradesmen);
        browseTradesmen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browseTrades = new Intent(ThankYou_Register.this, BrowseTradesMen.class);
                startActivity(browseTrades);
                finish();
            }
        });

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToBrowse = new Intent(ThankYou_Register.this, Browse.class);
                startActivity(goToBrowse);
                finish();

            }
        });

        goToDash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToDash = new Intent(ThankYou_Register.this, Dashboard.class);
                startActivity(goToDash);
                finish();
            }
        });


    }

    @Override
    public void onStop() {
        super.onStop();

        if (mAuthListener != null) {
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
                Intent openCreateAd = new Intent(ThankYou_Register.this, CreateAdAct.class);
                startActivity(openCreateAd);
            } else {
                Intent openLogin = new Intent(ThankYou_Register.this, Login.class);
                startActivity(openLogin);
                Toast.makeText(this, "Please Login/Register to create an Advert.", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        if (id == R.id.login) {

            Intent logIn = new Intent(ThankYou_Register.this, Login.class);
            startActivity(logIn);

            return true;
        }

        if (id == R.id.logout) {
            mAuth.signOut();
        }


        if (id == R.id.myTrade) {
            Intent openTradeTab = new Intent(ThankYou_Register.this, MyTrade.class);
            startActivity(openTradeTab);
        }

        if (id == R.id.browseTradesmen) {
            Intent openTradesMen = new Intent(ThankYou_Register.this, BrowseTradesMen.class);
            startActivity(openTradesMen);
        }

        if (id == R.id.dashboard) {
            Intent openDashboard = new Intent(ThankYou_Register.this, Dashboard.class);
            startActivity(openDashboard);
        }


        return super.onOptionsItemSelected(item);
    }

}
