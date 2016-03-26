package com.webbrowser.webbrowser;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebBackForwardList;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private String address;
    private Button navigateOrRefreshButton;
    private EditText editTextAddress;
    private ProgressBar progressBar;
    private RelativeLayout relativeLayout;
    private Button buttonSettings;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (WebView) findViewById(R.id.webView);
        address = "http://www.google.fi";
        webView.loadUrl(address);
        navigateOrRefreshButton = (Button) findViewById(R.id.buttonNavigateOrRefresh);
        navigateOrRefreshButton.setText("Refresh");
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout);
        buttonSettings = (Button) findViewById(R.id.buttonSettings);
        searchView = (SearchView) findViewById(R.id.searchView);

        editTextAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //if "Go" pressed on keyboard, navigate to page
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    webView.stopLoading();
                    String navigateToThisAddress;
                    if ((editTextAddress.getText().toString().startsWith("https://www.")) || (editTextAddress.getText().toString().startsWith("http://www."))) {
                        navigateToThisAddress = editTextAddress.getText().toString();
                    } else {
                        navigateToThisAddress = "http://www." + editTextAddress.getText().toString();
                    }
                    progressBar.setVisibility(View.VISIBLE);
                    webView.loadUrl(navigateToThisAddress);
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String searchText = searchView.getQuery().toString();
                if (searchText != null && !searchText.equals("")) {
                    int resultsCount = webView.findAll(searchText);
                    try {
                        Method m = WebView.class.getMethod("setFindIsUp", Boolean.TYPE);
                        m.invoke(webView, true);
                    } catch (Throwable ignored) {

                    }
                    searchView.setIconified(true);
                    searchView.setIconified(true);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.onActionViewExpanded();
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
                if (editTextAddress.getText().toString() != address)
                    navigateOrRefreshButton.setText("Go");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                editTextAddress.setText(url);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.INVISIBLE);
                navigateOrRefreshButton.setText("Refresh");
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                editTextAddress.setText(url);
                progressBar.setVisibility(View.VISIBLE);
                return super.shouldOverrideUrlLoading(view,url);
            }
        });

    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(relativeLayout.getWindowToken(), 0);
    }

    public void buttonNavigateOrRefreshButtonClicked(View view) {
        webView.stopLoading();
        String navigateToThisAddress;
        progressBar.setVisibility(View.VISIBLE);
        if((editTextAddress.getText().toString().startsWith("https://www.")) || (editTextAddress.getText().toString().startsWith("http://www."))) {
            navigateToThisAddress = editTextAddress.getText().toString();
        }
        else {
            navigateToThisAddress = "http://www."+editTextAddress.getText().toString();
        }
        webView.loadUrl(navigateToThisAddress);
        progressBar.setVisibility(View.VISIBLE);
        hideKeyboard();
    }
    public void buttonBackButtonClicked(View view) {
        webView.stopLoading();
        if(webView.canGoBack()) {
            progressBar.setVisibility(View.VISIBLE);
            webView.goBack();
        }
    }
    public void buttonForwardButtonClicked(View view) {
        webView.stopLoading();
        if(webView.canGoForward()) {
            progressBar.setVisibility(View.VISIBLE);
            webView.goForward();
        }
    }
    public void buttonExitButtonClicked(View view) {
        webView.stopLoading();
        this.finish();
    }
}
