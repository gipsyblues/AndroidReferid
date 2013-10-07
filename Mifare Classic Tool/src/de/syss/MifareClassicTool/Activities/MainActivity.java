/*
 * Copyright 2013 Gerhard Klostermeier
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package de.syss.MifareClassicTool.Activities;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.nfc.NfcAdapter;
import android.nfc.tech.MifareClassic;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import de.syss.MifareClassicTool.Common;
import de.syss.MifareClassicTool.R;

/**
 * Main App entry point showing the main menu.
 * Some stuff about the App:
 * <ul>
 * <li>Error/Debug messages (Log.e()/Log.d()) are hard coded</li>
 * <li>This is my first App, so please by decent with me ;)</li>
 * </ul>
 * @author Gerhard Klostermeier
 */
public class MainActivity extends Activity {

    private static final String LOG_TAG =
            MainActivity.class.getSimpleName();

    private final static int FILE_CHOOSER_DUMP_FILE = 1;
    private final static int FILE_CHOOSER_KEY_FILE = 2;
    private AlertDialog mEnableNfc;
    private Button mReadTag;
    private boolean mResume = true;
    private Intent mOldIntent = null;

    /**
     * Check for NFC hardware, Mifare Classic support and for external storage.
     * If the directory structure and the std. keys file is not already there
     * it will be created. Also, at the first run of this App, a warning
     * notice will be displayed.
     * @see #hasStdKeysFile()
     * @see #createStdKeysFile()
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Check if there is an NFC hardware component.
        Common.setNfcAdapter(NfcAdapter.getDefaultAdapter(this));
        if (Common.getNfcAdapter() == null) {
            new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_no_nfc_title)
                .setMessage(R.string.dialog_no_nfc)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.action_exit_app,
                        new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                 })
                 .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                 })
                 .show();
            mResume = false;
            return;
        }

        // Find Read button and bind them to member vars.
        mReadTag = (Button) findViewById(R.id.buttonMainReadTag);

        Button settingsButton = (Button) findViewById(R.id.buttonSettings);
        settingsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                // myIntent.putExtra("key", value); //Optional parameters
                startActivity(myIntent);
            }
        });

        Button historyButton = (Button) findViewById(R.id.buttonHistory);
        historyButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), HistoryActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                startActivity(myIntent);
            }
        });



        // Create a dialog that send user to NFC settings if NFC is off.
        // (Or let the user use the App in editor only mode / exit the App.)
        mEnableNfc = new AlertDialog.Builder(this)
            .setTitle(R.string.dialog_nfc_not_enabled_title)
            .setMessage(R.string.dialog_nfc_not_enabled)
            .setIcon(android.R.drawable.ic_dialog_info)
            .setPositiveButton(R.string.action_nfc,
                    new DialogInterface.OnClickListener() {
                @SuppressLint("InlinedApi")
                public void onClick(DialogInterface dialog, int which) {
                    // Goto NFC Settings.
                    if (Build.VERSION.SDK_INT >= 16) {
                        startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
                    } else {
                        startActivity(new Intent(
                                Settings.ACTION_WIRELESS_SETTINGS));
                    }
                }
             })
             .setNeutralButton(R.string.action_editor_only,
                    new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Only use Editor. Do nothing.
                }
             })
             .setNegativeButton(R.string.action_exit_app,
                     new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Exit the App.
                    finish();
                }
             }).create();

        // Show first usage notice.
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        boolean isFirstRun = sharedPref.getBoolean("is_first_run", true);
        if (isFirstRun) {
            Editor e = sharedPref.edit();
            e.putBoolean("is_first_run", false);
            e.commit();
            new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_first_run_title)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(R.string.dialog_first_run)
                .setPositiveButton(R.string.action_ok,
                        new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        mResume = true;
                        checkNfc();
                    }
                 })
                .show();
            mResume = false;
        }
    }

    /**
     * Add the menu with the tools.
     * It will be shown if the user clicks on "Tools".
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        menu.setHeaderTitle(R.string.dialog_tools_menu_title);
        menu.setHeaderIcon(android.R.drawable.ic_menu_preferences);
        inflater.inflate(R.menu.tools, menu);
        // Enable/Disable tag info tool depending on NFC availability.
        menu.findItem(R.id.menuMainTagInfo).setEnabled(
                Common.getNfcAdapter() != null
                && Common.getNfcAdapter().isEnabled());
    }

    /**
     * If resuming is allowed because all dependencies from
     * {@link #onCreate(Bundle)} are satisfied, call
     * {@link #checkNfc()}
     * @see #onCreate(Bundle)
     * @see #checkNfc()
     */
    @Override
    public void onResume() {
        super.onResume();

        if (mResume) {
            checkNfc();
        }
    }

    /**
     * Check if NFC adapter is enabled. If not, show the user a dialog and let
     * him choose between "Goto NFC Setting", "Use Editor Only" and "Exit App".
     * Also enable NFC foreground dispatch system.
     * @see Common#enableNfcForegroundDispatch(Activity)
     */
    private void checkNfc() {
        // Check if the NFC hardware is enabled.
        if (Common.getNfcAdapter() != null
                && !Common.getNfcAdapter().isEnabled()) {
            // NFC is disabled. Show dialog.
            mEnableNfc.show();
            // Disable read/write tag options.
            mReadTag.setEnabled(false);
           // mWriteTag.setEnabled(false);
            return;
        } else {
            // NFC is enabled. Hide dialog and enable NFC
            // foreground dispatch.
            if (mOldIntent != getIntent()) {
                if (Common.treatAsNewTag(getIntent(), this) == 0) {
                    // Device or tag does not support Mifare Classic.
                    // Run the only thing that is possible: The tag info tool.
                    Intent i = new Intent(this, TagInfoToolActivity.class);
                    startActivity(i);
                }
                mOldIntent = getIntent();
            }
            Common.enableNfcForegroundDispatch(this);
            mEnableNfc.hide();
            mReadTag.setEnabled(true);
           // mWriteTag.setEnabled(true);
        }
    }

    /**
     * Disable NFC foreground dispatch system.
     * @see Common#disableNfcForegroundDispatch(Activity)
     */
    @Override
    public void onPause() {
        super.onPause();
        Common.disableNfcForegroundDispatch(this);
    }

    /**
     * Handle new Intent as a new tag Intent and if the tag/device does not
     * support Mifare Classic, then run {@link TagInfoToolActivity}.
     * @see Common#treatAsNewTag(Intent, android.content.Context)
     * @see TagInfoToolActivity
     */
    @Override
    public void onNewIntent(Intent intent) {
        if (Common.treatAsNewTag(intent, this) == 0) {
            // Device or tag does not support Mifare Classic.
            // Run the only thing that is possible: The tag info tool.
            Intent i = new Intent(this, TagInfoToolActivity.class);
            startActivity(i);
        }
    }

    /**
     * Show the {@link ReadTagActivity}.
     * @param view The View object that triggered the method
     * (in this case the read tag button).
     * @see ReadTagActivity
     */
    public void onShowReadTag(View view) {
        Intent intent = new Intent(this, ReadTagActivity.class);
        startActivity(intent);
    }

    /**
     * Show the tools menu (as context menu).
     * @param view The View object that triggered the method
     * (in this case the tools button).
     */
    public void onShowTools(View view) {
        openContextMenu(view);
    }




    /**
     * Handle (start) the selected tool from the tools menu.
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
        case R.id.menuMainTagInfo:
            intent = new Intent(this, TagInfoToolActivity.class);
            startActivity(intent);
            return true;
        case R.id.menuMainValueBlockCoder:
            intent = new Intent(this, ValueBlockToolActivity.class);
            startActivity(intent);
            return true;
        default:
            return super.onContextItemSelected(item);
        }
    }

    private void createStdKeysFile() {
        // Create std. keys file.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Common.HOME_DIR) + Common.KEYS_DIR, Common.STD_KEYS);
        String[] lines = new String[Common.SOME_CLASSICAL_KNOWN_KEYS.length+4];
        lines[0] = "# " + getString(R.string.text_std_keys_comment);
        lines[1] = Common.byte2HexString(MifareClassic.KEY_DEFAULT);
        lines[2] = Common.byte2HexString(
                MifareClassic.KEY_MIFARE_APPLICATION_DIRECTORY);
        lines[3] = Common.byte2HexString(MifareClassic.KEY_NFC_FORUM);
        System.arraycopy(Common.SOME_CLASSICAL_KNOWN_KEYS, 0,
                lines, 4, Common.SOME_CLASSICAL_KNOWN_KEYS.length);
        Common.saveFile(file, lines);
    }

    /**
     * Check if there is a {@link Common#STD_KEYS} file
     * in {@link Common#HOME_DIR}/{@link Common#KEYS_DIR}.
     * @return True if there is such a file, False otherwise.
     */
    private boolean hasStdKeysFile() {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Common.HOME_DIR) + Common.KEYS_DIR, Common.STD_KEYS);
        return file.exists();
    }

}
