package com.webbrowser.webbrowser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private String address;
    private Button navigateOrRefreshButton;
    private Button buttonBack;
    private EditText editTextAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (WebView) findViewById(R.id.webView);
        address = "http://www.google.fi";
        webView.loadUrl(address);
        navigateOrRefreshButton = (Button) findViewById(R.id.buttonNavigateOrRefresh);
        navigateOrRefreshButton.setText("Refresh");
        buttonBack = (Button) findViewById(R.id.buttonBack);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);

        editTextAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //if "Go" pressed on keyboard, navigate to page
                if(actionId == EditorInfo.IME_ACTION_GO) webView.loadUrl(editTextAddress.getText().toString());
                return false;
            }
        });

        editTextAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //take address to a variable for later use
                address = editTextAddress.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //if the new address differs from previous, set button text to "Go"
                if(editTextAddress.getText().toString() != address) navigateOrRefreshButton.setText("Go");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void buttonNavigateOrRefreshButtonClicked(View view) {
        webView.loadUrl(editTextAddress.getText().toString());
        navigateOrRefreshButton.setText("Refresh");
    }
}
