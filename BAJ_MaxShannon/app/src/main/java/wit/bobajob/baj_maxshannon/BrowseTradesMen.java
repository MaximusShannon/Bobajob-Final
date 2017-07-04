package wit.bobajob.baj_maxshannon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import wit.bobajob.baj_maxshannon.Objects.TradeProfile;

public class BrowseTradesMen extends AppCompatActivity {


    ListView tradesmanList;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabase = database.getReference();
    StorageReference mStorRef = FirebaseStorage.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener mAuthListener;
    boolean loggedIn;

    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_trades_men);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tradesmanList = (ListView) findViewById(R.id.tradesmanList);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    BrowseTradesMen.this.loggedIn = false;
                } else {
                    BrowseTradesMen.this.loggedIn = true;
                    BrowseTradesMen.this.currentUserId = user.getUid();
                }

                invalidateOptionsMenu();

            }
        };


    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        final FirebaseListAdapter<TradeProfile> adapter = new FirebaseListAdapter<TradeProfile>(
                this,
                TradeProfile.class,
                R.layout.tradesmanlistitem,
                mDatabase.child("TradeProfile")
        ) {
            @Override
            protected void populateView(View v, TradeProfile model, int position) {
                //set image - need to pull the image down and display it properly - will be done in the project.
                //Set the text of the custom list items


                StorageReference advertImages = mStorRef.child("companyIcons/" + model.getIconTie());
                ImageView adImage = (ImageView) v.findViewById(R.id.companyIcon);
                adImage.setImageResource(R.drawable.workinprogress);

                Glide.with(BrowseTradesMen.this).using(new FirebaseImageLoader())
                        .load(advertImages)
                        .into(adImage);

                TextView companyName = (TextView) v.findViewById(R.id.companyName);
                companyName.setText(model.getTradeName());
                TextView companyEmail = (TextView) v.findViewById(R.id.tradeEmail);
                companyEmail.setText(model.getTradeEmail());
                TextView companyNumber = (TextView) v.findViewById(R.id.companyNumber);
                companyNumber.setText(Long.toString(model.getTradeNumber()));
                TextView tradeCounty = (TextView) v.findViewById(R.id.tradeCounty);
                tradeCounty.setText(model.getTradeCounty());
            }
        };


        tradesmanList.setAdapter(adapter);
        tradesmanList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TradeProfile profile = (TradeProfile) parent.getItemAtPosition(position);
                Intent openTradeProfile = new Intent(BrowseTradesMen.this, TradeProfileDisplay.class);
                openTradeProfile.putExtra("tradeprofile", profile);
                startActivity(openTradeProfile);
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
                Intent openCreateAd = new Intent(BrowseTradesMen.this, CreateAdAct.class);
                startActivity(openCreateAd);
            } else {
                Intent openLogin = new Intent(BrowseTradesMen.this, Login.class);
                startActivity(openLogin);
                Toast.makeText(this, "Please Login/Register to create an Advert.", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        if (id == R.id.login) {

            Intent logIn = new Intent(BrowseTradesMen.this, Login.class);
            startActivity(logIn);

            return true;
        }

        if (id == R.id.logout) {
            mAuth.signOut();
        }


        if (id == R.id.myTrade) {
            Intent openTradeTab = new Intent(BrowseTradesMen.this, MyTrade.class);
            startActivity(openTradeTab);
        }

        if(id == R.id.dashboard)
        {
            Intent goToDash = new Intent(BrowseTradesMen.this, Dashboard.class);
            startActivity(goToDash);
        }

        if(id == R.id.browseAds)
        {
            Intent openBrowse = new Intent(BrowseTradesMen.this, CreateAdAct.class);
            startActivity(openBrowse);
        }


        return super.onOptionsItemSelected(item);
    }

}
