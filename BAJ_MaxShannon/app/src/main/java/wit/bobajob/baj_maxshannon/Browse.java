package wit.bobajob.baj_maxshannon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
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

import java.util.ArrayList;

import wit.bobajob.baj_maxshannon.Objects.Ad;

public class Browse extends AppCompatActivity {


    ListView adListings;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabase = database.getReference();
    StorageReference mStorRef = FirebaseStorage.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener mAuthListener;

    boolean loggedIn = false;
    String userUid;
    ArrayList<Ad> adList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        adListings = (ListView) findViewById(R.id.adListView);

        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null){
                    Browse.this.loggedIn = false;
                }else{
                    Browse.this.loggedIn = true;
                    Browse.this.userUid = user.getUid();
                }

                invalidateOptionsMenu();

            }
        };

    }

    @Override
   public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        final FirebaseListAdapter<Ad> adapter = new FirebaseListAdapter<Ad>(
                this,
                Ad.class,
                R.layout.listiem,
                mDatabase.child("Ad")
        ) {
            @Override
            protected void populateView(View v, Ad model, int position) {
                //set image - need to pull the image down and display it properly - will be done in the project.
                //Set the text of the custom list items

                if(model.getCustomerIdRef().equals(userUid)){
                    adList.add(model);
                }

                StorageReference advertImages = mStorRef.child("adverts/"+model.getPictureTie());
                ImageView adImage = (ImageView) v.findViewById(R.id.adImage_list);
                adImage.setImageResource(R.drawable.workinprogress);

                Glide.with(Browse.this).using(new FirebaseImageLoader())
                        .load(advertImages)
                        .into(adImage);

                TextView adTitle = (TextView) v.findViewById(R.id.adTitle_list);
                adTitle.setText(model.getAdName());
                TextView adDesc = (TextView) v.findViewById(R.id.adDesc_list);
                adDesc.setText(model.getAdText());
            }
        };


        adListings.setAdapter(adapter);
        adListings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Ad advert = (Ad) parent.getItemAtPosition(position);
                Intent openAdvert = new Intent(Browse.this, Advert.class);
                openAdvert.putExtra("advert", advert);
                startActivity(openAdvert);
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
                Intent openCreateAd = new Intent(Browse.this, CreateAdAct.class);
                startActivity(openCreateAd);
            }else {
                Intent openLogin = new Intent(Browse.this, Login.class);
                startActivity(openLogin);
                Toast.makeText(this, "Please Login/Register to create an Advert.", Toast.LENGTH_SHORT).show();
            }
            return true;
        }



        if(id == R.id.login){

            Intent logIn = new Intent(Browse.this, Login.class);
            startActivity(logIn);

            return true;
        }

        if(id == R.id.logout){
            mAuth.signOut();
        }



        if(id == R.id.myTrade)
        {
            Intent openTradeTab = new Intent(Browse.this, MyTrade.class);
            startActivity(openTradeTab);
        }

        if(id == R.id.browseTradesmen)
        {
            Intent openTradesMen = new Intent(Browse.this, BrowseTradesMen.class);
            startActivity(openTradesMen);
        }

        if(id == R.id.dashboard)
        {
            Intent openDashboard = new Intent(Browse.this, Dashboard.class);
            startActivity(openDashboard);
        }




        return super.onOptionsItemSelected(item);
    }
}
