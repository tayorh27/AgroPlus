package com.ap.agroplus.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ap.agroplus.R;
import com.hardsoftstudio.real.textview.views.RealTextView;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AboutActivity extends AppCompatActivity {

    RealTextView tv;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        tv = (RealTextView) findViewById(R.id.tvAbout);
        tv.setHtmlFromString("<h2 style=\"text-align: center;\"><strong><u>About AgroPlus</u></strong></h2>\n" +
                "<strong>AgroPlus</strong> connects People to buy, sell or exchange Agricultural products and services by making it fast and easy for anyone to post a listing through their <strong>AgroPlus</strong> Mobile App.\n" +
                "\n" +
                "We’ve provided a platform to sell and buy almost anything.\n" +
                "\n" +
                "As a Seller you can:\n" +
                "<ul>\n" +
                " <li>Post free Ads with images;</li>\n" +
                " <li>Update, move your ad to Top position to get maximum efficiency from selling;</li>\n" +
                " <li>Get calls and messages only from real people, because we require every user to register.</li>\n" +
                "</ul>\n" +
                "As a Buyer you can:\n" +
                "<ul>\n" +
                " <li>Buy anything, simply call or send message to the Seller and agree purchase with Sellers directly;</li>\n" +
                " <li>Write a review after a deal is closed.</li>\n" +
                "</ul>\n" +
                "&nbsp;\n" +
                "<h3><strong><u>Acceptance</u></strong></h3>\n" +
                "The App and the Service are provided to you subject to these Terms of Use (For the purpose of the Terms and wherever the context so requires, the terms 'you' and “your” shall mean any person who uses the App or the Service in any manner whatsoever including persons surfing the App and its content, posting comments or any content or responding to any advertisements or content on the App. By using the Service, you agree to comply with these Terms. Additionally, when using a portion of the Service, you agree to conform to any applicable posted guidelines for such Service, which may change or be updated from time to time at <strong>AgroPlus</strong> sole discretion. You understand and agree that you are solely responsible for reviewing these Terms from time to time. Should you object to any term or condition of these Terms, any guideline, or any subsequent changes thereto or become unhappy with <strong>AgroPlus</strong> or the Service in any way, your only choice is to immediately discontinue use of <strong>AgroPlus</strong>. These Terms may be updated by <strong>AgroPlus</strong> at any time at its sole discretion. <strong>AgroPlus</strong> may send you notices of changes to the App or the Terms.", false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
