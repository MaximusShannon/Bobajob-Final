package wit.bobajob.baj_maxshannon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import wit.bobajob.baj_maxshannon.Objects.Ad;

public class Advert extends AppCompatActivity {

    TextView adTitle;
    TextView adText;
    TextView phoneNumber;
    TextView yourAd;
    Button editButton;
    Button deleteAdvert;
    ImageView adImage;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabase = database.getReference();
    StorageReference mStorRef = FirebaseStorage.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener mAuthListener;

    String userUid;
    boolean loggedIn;
    String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null){
                    Advert.this.loggedIn = false;
                }else{
                    Advert.this.loggedIn = true;
                    Advert.this.userUid = user.getUid();
                }

                invalidateOptionsMenu();

            }
        };

        getUser();
        tieWidgets();


        Intent intent = getIntent();
        final Ad advert = (Ad) intent.getSerializableExtra("advert");
        StorageReference advertImages = mStorRef.child("adverts/"+advert.getPictureTie());
        adTitle.setText(advert.getAdName());
        adText.setText(advert.getAdText());
        phoneNumber.setText("0"+Long.toString(advert.getPhoneNumber()));



        Glide.with(Advert.this).using(new FirebaseImageLoader())
                .load(advertImages)
                .into(adImage);

        if(advert.getCustomerIdRef().equals(currentUser))
        {
            editButton.setVisibility(View.VISIBLE);
            deleteAdvert.setVisibility(View.VISIBLE);


            deleteAdvert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    removeAdvert();

                }
            });
        }

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(Advert.this);
                View mView = getLayoutInflater().inflate(R.layout.editdialog, null);
                final EditText changeAdName = (EditText) mView.findViewById(R.id.editAdName);
                final EditText changeAdText = (EditText) mView.findViewById(R.id.editAdText);
                final EditText changeAdNumber = (EditText) mView.findViewById(R.id.editNumber);
                Button finishEdit = (Button) mView.findViewById(R.id.finishButton);


                finishEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        long phNum = 0;
                        String temp = changeAdNumber.getText().toString();
                        try{
                            phNum = Long.parseLong(temp);
                        }catch (NumberFormatException e){
                        }


                        if(!changeAdName.getText().toString().equals("") || !changeAdNumber.getText().toString().equals("")) {
                            updateAdvert(advert, changeAdName.getText().toString(), changeAdText.getText().toString(), phNum);
                            finish();
                        }
                        else
                            Toast.makeText(Advert.this, "Please Fill in the details", Toast.LENGTH_SHORT).show();

                    }

                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();

                //updateAdvert(advert);

            }
        });


    }

    public void tieWidgets() {
        adTitle = (TextView) findViewById(R.id.advertTitle);
        adText = (TextView) findViewById(R.id.advertText);
       // yourAd = (TextView) findViewById(R.id.yourAd);
       // yourAd.setVisibility(View.INVISIBLE);
        editButton = (Button) findViewById(R.id.adEdit);
        deleteAdvert = (Button) findViewById(R.id.deleteAdvert);
        editButton.setVisibility(View.INVISIBLE);
        deleteAdvert.setVisibility(View.INVISIBLE);
        adImage = (ImageView) findViewById(R.id.advertImage);
        phoneNumber = (TextView) findViewById(R.id.phoneNumber);
    }

    public void getUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
        {
            currentUser = user.getUid();
        }

    }


    public void updateAdvert(final Ad advert, final String newName, final String newText, final long newNumber){
        Query firebaseDeleteQuery = mDatabase.child("Ad").orderByChild("adName").equalTo(adTitle.getText().toString());
        firebaseDeleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot adSnapshot: dataSnapshot.getChildren()){
                   adSnapshot.getRef().removeValue();
                    Ad updatedNewAd = new Ad(newName, newText, newNumber, currentUser, advert.getAcceptEmails(), advert.getAcceptTerms(), advert.getCategory(), advert.getPictureTie() );
                    mDatabase.child("Ad").push().setValue(updatedNewAd);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public void removeAdvert(){

        Query firebaseDeleteQuery = mDatabase.child("Ad").orderByChild("adName").equalTo(adTitle.getText().toString());
        firebaseDeleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot adSnapshot: dataSnapshot.getChildren()){
                    adSnapshot.getRef().removeValue();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
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
                Intent openCreateAd = new Intent(Advert.this, CreateAdAct.class);
                startActivity(openCreateAd);
            }else {
                Intent openLogin = new Intent(Advert.this, Login.class);
                startActivity(openLogin);
                Toast.makeText(this, "Please Login/Register to create an Advert.", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        if(id == R.id.login){

            Intent logIn = new Intent(Advert.this, Login.class);
            startActivity(logIn);

            return true;
        }

        if(id == R.id.logout){

            Toast.makeText(this, "LOGOUTPRESSED", Toast.LENGTH_SHORT).show();
            mAuth.signOut();


        }

        if(id == R.id.browseAds)
        {
            Intent openBrowse = new Intent(Advert.this, CreateAdAct.class);
            startActivity(openBrowse);
        }









        return super.onOptionsItemSelected(item);
    }






}
