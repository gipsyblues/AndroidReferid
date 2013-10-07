package de.syss.MifareClassicTool.Activities;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.webkit.WebView;
import android.os.Bundle;
import de.syss.MifareClassicTool.R;

public class HistoryActivity extends Activity {
    private WebView webview;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        webview = (WebView) findViewById(R.id.webview);

        final String emptyString = "";
        final SharedPreferences loginPref = this.getSharedPreferences("SETTINGS_NAME", 0);
        String userValRough = loginPref.getString("username", emptyString);
        String userid = loginPref.getString("userid", "00000000");

        String base = "http://referid.co/history.php?";
        String user = "&user=" + userValRough;
        userid = "&userid=" + userid;

        webview.getSettings().setJavaScriptEnabled(true);
        String URL = base+userid;
        webview.loadUrl(URL);
    }   

}