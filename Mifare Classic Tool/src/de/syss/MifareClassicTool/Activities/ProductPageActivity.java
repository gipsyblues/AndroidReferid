package de.syss.MifareClassicTool.Activities;
import android.app.Activity;
import android.webkit.WebView;
import android.os.Bundle;
import de.syss.MifareClassicTool.R;

public class ProductPageActivity extends Activity {

    private WebView webView;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);

        webView = (WebView) findViewById(R.id.webview);

        webView.getSettings().setJavaScriptEnabled(true);
        String base = getIntent().getStringExtra("base");
        String product = getIntent().getStringExtra("product");
        String company = getIntent().getStringExtra("company");
        String user = getIntent().getStringExtra("user");
        String userid = getIntent().getStringExtra("userid");
        String URL = base+product+company+user+userid;
        webView.loadUrl(URL);
    }   

}