package wit.bobajob.baj_maxshannon;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyProfile extends AppCompatActivity {

    TextView userName;
    TextView userEmail;
    TextView userUUID;
    TextView aboutUser;
    TextView location;
    CheckBox skilledJobsCheck;
    CheckBox unskilledJobsCheck;
    Button editUserEmail;
    Button editUserBio;
    Button editUserLocation;
    Button backToBrowse;
    Button saveChanges;

    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    boolean loggedIn = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        widgetFinds();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
        {
            userEmail.setText(user.getEmail());
            userUUID.setText(user.getUid());
        }


        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null){
                    MyProfile.this.loggedIn = false;
                }else{
                    MyProfile.this.loggedIn = true;
                }

                invalidateOptionsMenu();

            }
        };

        editUserBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MyProfile.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_bio, null);

                final EditText bioText = (EditText) mView.findViewById(R.id.bioText);
                Button cancel = (Button) mView.findViewById(R.id.cancelBtn);
                Button save = (Button) mView.findViewById(R.id.saveBtn);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        aboutUser.setText(bioText.getText());
                        Toast.makeText(MyProfile.this, "Bio changed - Please save changes!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });


                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // Aler

                        dialog.dismiss();

                    }
                });



            }


        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop(){
        super.onStop();

        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    public void widgetFinds()
    {
        userName = (TextView) findViewById(R.id.userName);
        userEmail = (TextView) findViewById(R.id.userEmail);
        userUUID = (TextView) findViewById(R.id.userUUID);
        aboutUser = (TextView) findViewById(R.id.aboutUser);
        location = (TextView) findViewById(R.id.location);
        skilledJobsCheck = (CheckBox) findViewById(R.id.skilledJobsCheck);
        unskilledJobsCheck = (CheckBox) findViewById(R.id.unskilledCheck);

        editUserEmail = (Button) findViewById(R.id.editUserEmail);
        editUserBio = (Button) findViewById(R.id.editUserBio);
        editUserLocation = (Button) findViewById(R.id.editUserLocation);
        backToBrowse = (Button) findViewById(R.id.backToBrowse);
        saveChanges = (Button) findViewById(R.id.backToBrowse);

    }

}
