package wit.bobajob.baj_maxshannon;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Random;

import wit.bobajob.baj_maxshannon.Objects.TradeProfile;

public class MyTrade extends AppCompatActivity {

    EditText tradeName;
    EditText tradeEmail;
    EditText tradeNumber;
    EditText location;
    EditText tradeExperience;
    Spinner countySpinnder;
    CheckBox availDays, availNights, unskilledWorker, skilledWoker;
    ImageButton companyIcon;
    Button finishButton;
    Uri iconImage;
    Uri iconImageSelected;
    String imageIdentifier;
    boolean imagepicked;

    boolean cb1, cb2, cb3, cb4;

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
        setContentView(R.layout.activity_my_trade2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null){
                    MyTrade.this.loggedIn = false;
                }else{
                    MyTrade.this.loggedIn = true;
                    MyTrade.this.currentUserId = user.getUid();

                }
                invalidateOptionsMenu();
            }
        };

        getTies();
        imageButtonFunctionality();
        getUser();

        imageIdentifier = getSaltString();

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    StorageReference advertImages = mStorRef.child("companyIcons/" + imageIdentifier);
                    advertImages.putFile(iconImageSelected).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(MyTrade.this, "IMAGE UPLOADED", Toast.LENGTH_SHORT).show();
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {


                                }
                            });
                } catch (IllegalArgumentException e) {

                }


                //lets create the object
                long phNum = 0;
                String temp = tradeNumber.getText().toString();
                try {
                    phNum = Long.parseLong(temp);
                } catch (NumberFormatException e) {
                }


                if (!tradeName.getText().toString().equals("") && !tradeNumber.getText().toString().equals("") && !tradeEmail.getText().toString().equals("") && !tradeNumber.getText().toString().equals("") && imagepicked) {
                    checkCheckBoxs();

                    TradeProfile newProfile = new TradeProfile(tradeName.getText().toString(), tradeEmail.getText().toString(),
                            phNum, location.getText().toString(), tradeExperience.getText().toString()
                            , countySpinnder.getSelectedItem().toString(), cb1, cb2, cb3, cb4, currentUserId, imageIdentifier);

                    mDatabase.child("TradeProfile").push().setValue(newProfile);
                    Toast.makeText(MyTrade.this, "TradeProfile Complete", Toast.LENGTH_SHORT).show();
                    finish();

                } else
                    Toast.makeText(MyTrade.this, "Incomplete - Please check details", Toast.LENGTH_SHORT).show();
            }


        });

    }

    public void getTies() {
        tradeName = (EditText) findViewById(R.id.tradeName);
        tradeEmail = (EditText) findViewById(R.id.tradeEmail);
        tradeNumber = (EditText) findViewById(R.id.tradeNumber);
        location = (EditText) findViewById(R.id.location);
        tradeExperience = (EditText) findViewById(R.id.tradeExperience);
        countySpinnder = (Spinner) findViewById(R.id.countySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.countys, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countySpinnder.setAdapter(adapter);
        availDays = (CheckBox) findViewById(R.id.availDays);
        availNights = (CheckBox) findViewById(R.id.availNights);
        unskilledWorker = (CheckBox) findViewById(R.id.unskilledWorker);
        skilledWoker = (CheckBox) findViewById(R.id.skilledWorker);
        companyIcon = (ImageButton) findViewById(R.id.companyIcon);
        finishButton = (Button) findViewById(R.id.finishButton);
    }

    public void imageButtonFunctionality() {

        companyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickAddPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickAddPhoto, 1);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    iconImageSelected = imageReturnedIntent.getData();
                    companyIcon.setImageURI(iconImageSelected);
                    imagepicked = true;
                } else
                    imagepicked = false;
                break;
        }

    }

    public void checkCheckBoxs() {
        if (availDays.isChecked())
            cb1 = true;
        else
            cb1 = false;
        if (availNights.isChecked())
            cb2 = true;
        else
            cb2 = false;
        if (unskilledWorker.isChecked())
            cb3 = true;
        else
            cb3 = false;
        if (skilledWoker.isChecked())
            cb4 = true;
        else
            cb4 = false;
    }

    //Taken from stack overflow - http://stackoverflow.com/questions/20536566/creating-a-random-string-with-a-z-and-0-9-in-java
    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    public void getUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            currentUserId = user.getUid();
        }

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
                Intent openCreateAd = new Intent(MyTrade.this, CreateAdAct.class);
                startActivity(openCreateAd);
            } else {
                Intent openLogin = new Intent(MyTrade.this, Login.class);
                startActivity(openLogin);
                Toast.makeText(this, "Please Login/Register to create an Advert.", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        if (id == R.id.login) {

            Intent logIn = new Intent(MyTrade.this, Login.class);
            startActivity(logIn);

            return true;
        }

        if (id == R.id.logout) {
            mAuth.signOut();
        }

        if(id == R.id.browseTradesmen)
        {
            Intent openTrades = new Intent(MyTrade.this, BrowseTradesMen.class);
            startActivity(openTrades);
        }

        if(id == R.id.dashboard)
        {
            Intent goToDash = new Intent(MyTrade.this, Dashboard.class);
            startActivity(goToDash);
        }

        if(id == R.id.browseAds)
        {
            Intent openBrowse = new Intent(MyTrade.this, CreateAdAct.class);
            startActivity(openBrowse);
        }


        return super.onOptionsItemSelected(item);
    }


}
