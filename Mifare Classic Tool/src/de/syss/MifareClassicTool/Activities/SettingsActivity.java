package de.syss.MifareClassicTool.Activities;

import android.widget.EditText;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View.OnClickListener;

import java.util.HashSet;
import java.util.Set;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import de.syss.MifareClassicTool.R;
import android.widget.Button;
import java.lang.String;
import android.content.Context;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

public class SettingsActivity extends Activity {
    public static final String SETTINGS_NAME = "MySettingsFile";
    EditText inputName;
    EditText inputPass;
    EditText inputPassConfirmed;
    String name;
    String pass;
    HashSet<String> empty;
    Boolean userContained;

    @Override
    protected void onCreate(Bundle state){
       super.onCreate(state);

        //Check if saved user/pass
        final Context context = this;
        final SharedPreferences loginPref = getApplicationContext().getSharedPreferences("SETTINGS_NAME", 0);

        setContentView(R.layout.activity_settings);

        // Edit Text
        inputName = (EditText) findViewById(R.id.usernameSettings);
        inputPass = (EditText) findViewById(R.id.passwordSettings);
        inputPassConfirmed = (EditText) findViewById(R.id.passwordSettingsConfirmed);

        // Create button
        Button submitButton1 = (Button) findViewById(R.id.submitButton);
        submitButton1.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view) {

                final String confirmPassString = inputPassConfirmed.getText().toString();
                final String passString = inputPass.getText().toString();
                final String userString = inputName.getText().toString();

               /* userContained = false;
                empty = new HashSet<String>();
                empty.add("nobody");
                for(String user : loginPref.getStringSet("username", empty))
                {
                    if(user.equals(userString))
                    {
                        userContained = true;
                    }
                }
*/
                if(!passString.equals(confirmPassString))
                {
                    showToast("Woops! Your passwords don't match.");
                }
                else if(passString.equals(confirmPassString))
                {
                    //convert parameters to strings
                    if(userString.equals("trev"))
                    {
                        loginPref.edit().putString("userid", "00000001").commit();
                    }
                    if(userString.equals("martin"))
                    {
                        loginPref.edit().putString("userid", "00000002").commit();
                    }
                    if(userString.equals("chris"))
                    {
                        loginPref.edit().putString("userid", "00000003").commit();
                    }


                    loginPref.edit().putString("username", userString).commit();
                    loginPref.edit().putString("password", passString).commit();
                    showToast("Successfully added you as a user!");
                }
                else
                {
                    showToast("You're username is taken!");

                }
            }
        });
    }
    public void showToast(final String toast)
    {
        Toast.makeText(SettingsActivity.this, toast, Toast.LENGTH_SHORT).show();
    }
}