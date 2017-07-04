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
import android.widget.ImageView;
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

import wit.bobajob.baj_maxshannon.Objects.Ad;

public class CreateAdAct extends AppCompatActivity {


    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener mAuthListener;

    Spinner categorySpinner;
    Button addPictureBtn;
    Button createAd;
    ImageView adSelected;
    Uri adImageSelected;
    Uri imageUrl_top;


    //Edittext for inputs
    EditText adTitle;
    EditText adDesc;
    EditText phoneNum;

    //Checkboxs
    CheckBox acceptEmails;
    CheckBox acceptTerms;


    Boolean numberCorrect;
    Boolean pictureAdded;
    boolean acceptedTerms = false;
    boolean loggedIn;
    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ad);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    CreateAdAct.this.loggedIn = false;
                } else {
                    CreateAdAct.this.loggedIn = true;
                    CreateAdAct.this.currentUserId = user.getUid();
                }

                invalidateOptionsMenu();

            }
        };


        //Populate the Spinner with data predefined in the resources
        /*
        * I could give the user the option to populate this spinner - In other words I could make the user create the category, But this would have to be adminned as no idea how much
        * or what someone would create - and personally filtering this at this time is not viable
        *
        *
        * */


        categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.job_catergorys, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);


        //Open the Gallery
        addPictureBtn = (Button) findViewById(R.id.pictureBtn);
        adSelected = (ImageView) findViewById(R.id.adImage);
        addPictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickAddPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickAddPhoto, 1);
            }
        });


        adTitle = (EditText) findViewById(R.id.adTitleText);
        adDesc = (EditText) findViewById(R.id.adDesc);
        phoneNum = (EditText) findViewById(R.id.phoneNumber);
        acceptEmails = (CheckBox) findViewById(R.id.acceptEmail);
        acceptTerms = (CheckBox) findViewById(R.id.acceptTerms);


        createAd = (Button) findViewById(R.id.createAdBtn);
        createAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean acceptingEmails = false;

                String userUid = "";

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    userUid = user.getUid();
                }


                if (acceptEmails.isChecked())
                    acceptingEmails = true;
                if (acceptTerms.isChecked())
                    acceptedTerms = true;


                long phNum = 0;
                String temp = phoneNum.getText().toString();
                try {
                    phNum = Long.parseLong(temp);
                } catch (NumberFormatException e) {

                }


                String imageIdentifier = getSaltString();

                try {

                    StorageReference advertImages = mStorageRef.child("adverts/" + imageIdentifier);
                    advertImages.putFile(adImageSelected).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                } catch (IllegalArgumentException e) {

                }


                if (!adTitle.getText().toString().equals("") && !categorySpinner.getSelectedItem().toString().equals("Please Select...") && acceptedTerms && !phoneNum.getText().toString().equals("")) {
                    Ad newAd = new Ad(adTitle.getText().toString(), adDesc.getText().toString(), phNum, userUid, acceptingEmails, acceptedTerms, categorySpinner.getSelectedItem().toString(), imageIdentifier);


                    mDatabase.child("Ad").push().setValue(newAd);

                    Intent openAfterCreated = new Intent(CreateAdAct.this, ThankYou_CreateAd.class);
                    startActivity(openAfterCreated);

                } else {
                    Toast.makeText(CreateAdAct.this, "Please fill in the details!", Toast.LENGTH_SHORT).show();
                }


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
                Intent openCreateAd = new Intent(CreateAdAct.this, CreateAdAct.class);
                startActivity(openCreateAd);
            } else {
                Intent openLogin = new Intent(CreateAdAct.this, Login.class);
                startActivity(openLogin);
                Toast.makeText(this, "Please Login/Register to create an Advert.", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        if (id == R.id.login) {

            Intent logIn = new Intent(CreateAdAct.this, Login.class);
            startActivity(logIn);

            return true;
        }

        if (id == R.id.logout) {
            mAuth.signOut();
        }


        if (id == R.id.myTrade) {
            Intent openTradeTab = new Intent(CreateAdAct.this, MyTrade.class);
            startActivity(openTradeTab);
        }

        if(id == R.id.browseTradesmen)
        {
            Intent openTrades = new Intent(CreateAdAct.this, BrowseTradesMen.class);
            startActivity(openTrades);
        }

        if(id == R.id.dashboard)
        {
            Intent goToDash = new Intent(CreateAdAct.this, Dashboard.class);
            startActivity(goToDash);
        }


        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    adImageSelected = imageReturnedIntent.getData();
                    adSelected.setImageURI(adImageSelected);
                }
                break;
        }

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

}
