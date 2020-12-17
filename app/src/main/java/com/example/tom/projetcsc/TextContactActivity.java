package com.example.tom.projetcsc;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ThreadLocalRandom;

public class TextContactActivity extends AppCompatActivity {

    public TextView tvTextContactName;
    public TextView tvTextContactNumber;
    public String contactName;
    public String contactNumber;

    private Toolbar toolbar;
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.pause:
                Intent pause = new Intent(getApplicationContext(), PauseActivity.class);
                startActivity(pause);
                break;
            case R.id.scores:
                Intent launchScores = new Intent(getApplicationContext(), ScoresActivity.class);
                startActivity(launchScores);
                break;
            case R.id.options:
                Intent goToPrefs = new Intent(getApplicationContext(), GamePreferences.class);
                startActivity(goToPrefs);
                break;
            case R.id.home:
                AlertDialog.Builder builder = new AlertDialog.Builder(TextContactActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Are you sure you want to quit?");
                builder.setMessage("That will erase every score");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent backHome = new Intent(getApplicationContext(), MainActivity.class);
                                backHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(backHome);
                                Toast.makeText(getApplicationContext(), "Let's start another game!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_contact);

        toolbar=findViewById(R.id.myToolBar);
        toolbar.setTitle(PlayerList.currentNarrator.name);
        setSupportActionBar(toolbar);

        LinearLayout lyTextContact = findViewById(R.id.lyTextContact);
        Button btnTextContactNext = (Button)findViewById(R.id.btnTextContactNext);
        btnTextContactNext.setOnClickListener(new GetActivityOnClickListener(this) {
            @Override
            public void onClick(View v) {
                this.activity.finish();
            }
        });

        tvTextContactName = (TextView)findViewById(R.id.tvTextContactName);
        tvTextContactNumber = (TextView)findViewById(R.id.tvTextContactNumber);

        Player narrator = PlayerList.currentNarrator;

        setAnotherContact();

        Button btnContactTexted = (Button)findViewById(R.id.btnContactTexted);
        Button btnContactNotTexted = (Button)findViewById(R.id.btnContactNotTexted);

        btnContactTexted.setOnClickListener(new GetActivityOnClickListener(this) {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Well done!!", Toast.LENGTH_SHORT).show();
                Toast.makeText(activity, narrator + " you win 2 points", Toast.LENGTH_LONG).show();
                narrator.score+=2;
                lyTextContact.setVisibility(View.GONE);
            }
        });

        btnContactNotTexted.setOnClickListener(new GetActivityOnClickListener(this) {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Oh no!!", Toast.LENGTH_SHORT).show();
                Toast.makeText(activity, narrator + " you lose 2 points", Toast.LENGTH_LONG).show();
                narrator.score-=2;
                lyTextContact.setVisibility(View.GONE);
            }
        });

        Button btnGetAnotherContact = (Button)findViewById(R.id.btnGetAnotherTextContact);
        btnGetAnotherContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAnotherContact();
                btnGetAnotherContact.setVisibility(View.INVISIBLE);
            }
        });




    }

    private void getcontact() {
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        int index = ThreadLocalRandom.current().nextInt(0, cursor.getCount());
        cursor.moveToPosition(index);

        contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
        contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

        cursor.close();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if (requestCode ==1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getcontact();
            }
        }
    }

    public void verifyBuildVersion(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},1);
        }else{
            getcontact();
        }
    }

    public void setAnotherContact(){
        verifyBuildVersion();
        tvTextContactName.setText(contactName);
        tvTextContactNumber.setText(contactNumber);
    }
}