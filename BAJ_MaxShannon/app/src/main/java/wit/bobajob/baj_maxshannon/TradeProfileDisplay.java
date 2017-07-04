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

import wit.bobajob.baj_maxshannon.Objects.TradeProfile;

public class TradeProfileDisplay extends AppCompatActivity {

    String currentUser;
    TextView companyName, companyEmail, companyNumber, companyLocation, companyCounty, companyExperience, availDays, availNights, unskilled, skilled;
    Button edit, delete;
    ImageView companyIcon;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseAuth.AuthStateListener mAuthListener;
    StorageReference mStorRef = FirebaseStorage.getInstance().getReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabase = database.getReference();
    boolean loggedIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_profile_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getTies();
        getUser();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    TradeProfileDisplay.this.loggedIn = false;
                } else {
                    TradeProfileDisplay.this.loggedIn = true;
                    TradeProfileDisplay.this.currentUser = user.getUid();
                }

                invalidateOptionsMenu();

            }
        };

        Intent intent = getIntent();
        final TradeProfile profile = (TradeProfile) intent.getSerializableExtra("tradeprofile");
        StorageReference profileImage = mStorRef.child("companyIcons/" + profile.getIconTie());

        Glide.with(TradeProfileDisplay.this).using(new FirebaseImageLoader())
                .load(profileImage)
                .into(companyIcon);

        companyName.setText(profile.getTradeName());
        companyEmail.setText(profile.getTradeEmail());
        companyNumber.setText(Long.toString(profile.getTradeNumber()));
        companyLocation.setText(profile.getTradeLocation());
        companyCounty.setText(profile.getTradeCounty());
        companyExperience.setText(profile.getTradeExperience());

        if (profile.isAvailDays())
            availDays.setText("Yes.");
        else
            availDays.setText("No.");
        if (profile.isAvailNights())
            availNights.setText("Yes");
        else
            availNights.setText("No.");
        if (profile.isUnskilledWorker())
            unskilled.setText("Yes.");
        else
            unskilled.setText("No.");
        if (profile.isSkilledWorker())
            skilled.setText("Yes.");
        else
            skilled.setText("No.");

        if (currentUser.equals(profile.getUserId())) {
            edit.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    removeTradesman();
                    finish();
                }
            });

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder mBuilder = new AlertDialog.Builder(TradeProfileDisplay.this);
                    View mView = getLayoutInflater().inflate(R.layout.edittradesman, null);
                    final EditText changeTradeName = (EditText) mView.findViewById(R.id.companyName);
                    final EditText changeTradeNumber = (EditText) mView.findViewById(R.id.companyNumber);
                    final EditText changeTradeEmail = (EditText) mView.findViewById(R.id.companyEmail);
                    Button finishEdit = (Button) mView.findViewById(R.id.finish);

                    finishEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            long phNum = 0;
                            String temp = changeTradeNumber.getText().toString();
                            try {
                                phNum = Long.parseLong(temp);
                            } catch (NumberFormatException e) {
                            }


                            if (!changeTradeName.getText().toString().equals("") || !changeTradeEmail.getText().toString().equals("")) {
                                updateTradesman(profile, changeTradeName.getText().toString(), phNum, changeTradeEmail.getText().toString());
                                finish();
                                Toast.makeText(TradeProfileDisplay.this, "Updated!", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(TradeProfileDisplay.this, "Check Details - Failed", Toast.LENGTH_SHORT).show();


                        }

                    });
                    mBuilder.setView(mView);
                    AlertDialog dialog = mBuilder.create();
                    dialog.show();
                }

            });


            //finish
        }

    }

    public void removeTradesman() {

        Query firebaseDeleteQuery = mDatabase.child("TradeProfile").orderByChild("tradeName").equalTo(companyName.getText().toString());
        firebaseDeleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot adSnapshot : dataSnapshot.getChildren()) {
                    adSnapshot.getRef().removeValue();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void updateTradesman(final TradeProfile profile, final String newName, final long newNumber, final String newEmail) {
        Query firebaseDeleteQuery = mDatabase.child("TradeProfile").orderByChild("tradeName").equalTo(companyName.getText().toString());
        firebaseDeleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot adSnapshot : dataSnapshot.getChildren()) {
                    adSnapshot.getRef().removeValue();

                    TradeProfile updatedTradeProfile = new TradeProfile(newName, newEmail, newNumber, profile.getTradeLocation(), profile.getTradeExperience(), profile.getTradeCounty(), profile.isAvailDays(), profile.isAvailNights(), profile.isUnskilledWorker(), profile.isSkilledWorker(), profile.getUserId(), profile.getIconTie());
                    mDatabase.child("TradeProfile").push().setValue(updatedTradeProfile);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public void getUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            currentUser = user.getUid();
        }

    }

    public void getTies() {

        companyIcon = (ImageView) findViewById(R.id.companyIcon);
        companyName = (TextView) findViewById(R.id.companyName);
        companyEmail = (TextView) findViewById(R.id.companyEmail);
        companyNumber = (TextView) findViewById(R.id.companyNumber);
        companyCounty = (TextView) findViewById(R.id.companyCounty);
        companyLocation = (TextView) findViewById(R.id.companyLocation);
        companyExperience = (TextView) findViewById(R.id.companyExp);
        availDays = (TextView) findViewById(R.id.availDays);
        availNights = (TextView) findViewById(R.id.availNights);
        unskilled = (TextView) findViewById(R.id.unskilled);
        skilled = (TextView) findViewById(R.id.skilled);
        edit = (Button) findViewById(R.id.editProfile);
        delete = (Button) findViewById(R.id.delete);
        edit.setVisibility(View.INVISIBLE);
        delete.setVisibility(View.INVISIBLE);
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
                Intent openCreateAd = new Intent(TradeProfileDisplay.this, CreateAdAct.class);
                startActivity(openCreateAd);
            } else {
                Intent openLogin = new Intent(TradeProfileDisplay.this, Login.class);
                startActivity(openLogin);
                Toast.makeText(this, "Please Login/Register to create an Advert.", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        if (id == R.id.login) {

            Intent logIn = new Intent(TradeProfileDisplay.this, Login.class);
            startActivity(logIn);

            return true;
        }

        if (id == R.id.logout) {
            mAuth.signOut();
        }


        if (id == R.id.myTrade) {
            Intent openTradeTab = new Intent(TradeProfileDisplay.this, MyTrade.class);
            startActivity(openTradeTab);
        }

        if(id == R.id.browseAds)
        {
            Intent openBrowse = new Intent(TradeProfileDisplay.this, CreateAdAct.class);
            startActivity(openBrowse);
        }


        return super.onOptionsItemSelected(item);
    }

}
