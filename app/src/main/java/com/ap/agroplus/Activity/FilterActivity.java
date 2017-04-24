package com.ap.agroplus.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ap.agroplus.R;
import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapLabel;
import com.beardedhen.androidbootstrap.api.attributes.BootstrapBrand;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FilterActivity extends AppCompatActivity {

    BootstrapLabel[] labels;
    BootstrapLabel ls, fd, cp, fz, my, gn;
    String category = "";
    int price = 1000;
    SeekBar seekBar;
    EditText tv_price;
    BootstrapButton filter;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ls = (BootstrapLabel) findViewById(R.id.ls);
        fd = (BootstrapLabel) findViewById(R.id.fd);
        cp = (BootstrapLabel) findViewById(R.id.cp);
        fz = (BootstrapLabel) findViewById(R.id.fz);
        my = (BootstrapLabel) findViewById(R.id.my);
        gn = (BootstrapLabel) findViewById(R.id.gn);

        seekBar = (SeekBar) findViewById(R.id.price_seek);
        tv_price = (EditText) findViewById(R.id.editPrice);
        filter = (BootstrapButton) findViewById(R.id.btnFilter);

        labels = new BootstrapLabel[]{ls, fd, cp, fz, my, gn};
        seekBar.setProgress(1000);
        if (Build.VERSION.SDK_INT > 23) {
            seekBar.setProgress(1000, true);
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_price.setText(progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                price = seekBar.getProgress();
            }
        });
        tv_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    int value = Integer.parseInt(String.valueOf(s));
                    int getMax = seekBar.getMax();
                    if (value <= getMax) {
                        seekBar.setProgress(value);
                    } else {
                        Toast.makeText(FilterActivity.this, "Maximum price reached", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void Submit(View view) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("category", category);
        bundle.putString("price", price + "");
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void CategoryClick(View view) {
        SetSelected(view);
    }

    private void setBootstrapLabelColor(int index) {
        for (int i = 0; i < labels.length; i++) {
            int id = labels[i].getId();
            if (id == index) {
                labels[i].setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
                filter.setVisibility(View.VISIBLE);
            } else {
                labels[i].setBootstrapBrand(DefaultBootstrapBrand.PRIMARY);
            }
        }
    }

    private void SetSelected(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.ls:
                setBootstrapLabelColor(R.id.ls);
                category = "Livestock";
                break;
            case R.id.fd:
                setBootstrapLabelColor(R.id.fd);
                category = "Feeds";
                break;
            case R.id.cp:
                setBootstrapLabelColor(R.id.cp);
                category = "Crops";
                break;
            case R.id.fz:
                setBootstrapLabelColor(R.id.fz);
                category = "Fertilizer";
                break;
            case R.id.my:
                setBootstrapLabelColor(R.id.my);
                category = "Machinery";
                break;
            case R.id.gn:
                setBootstrapLabelColor(R.id.gn);
                category = "Grains";
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
