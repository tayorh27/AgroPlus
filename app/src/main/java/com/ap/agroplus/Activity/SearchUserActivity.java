package com.ap.agroplus.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ap.agroplus.Adapter.SearchAdapter;
import com.ap.agroplus.Callbacks.ClickCallback;
import com.ap.agroplus.Callbacks.UserCallback;
import com.ap.agroplus.R;
import com.ap.agroplus.database.AppData;
import com.ap.agroplus.information.Search;
import com.ap.agroplus.network.GetUsers;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SearchUserActivity extends AppCompatActivity implements ClickCallback, UserCallback {

    RecyclerView recyclerView;
    SearchAdapter adapter;
    AppData data;
    EditText editText_search;
    AVLoadingIndicatorView loading;
    ArrayList<Search> searchArray = new ArrayList<>();
    boolean isAvailFromDb = true;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data = new AppData(SearchUserActivity.this);
        editText_search = (EditText) findViewById(R.id.edit_search);
        loading = (AVLoadingIndicatorView) findViewById(R.id.loading);
        recyclerView = (RecyclerView) findViewById(R.id.myRecyclerView);
        loading.smoothToHide();
        adapter = new SearchAdapter(SearchUserActivity.this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchUserActivity.this));
        recyclerView.setAdapter(adapter);

        CheckDatabaseForExistingUser();

        editText_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loading.smoothToShow();
                String getUsername = String.valueOf(s);
                if (getUsername.length() > 0) {
                    CheckUsersOnline(getUsername);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void CheckUsersOnline(String username) {
        GetUsers getUsers = new GetUsers(SearchUserActivity.this, this);
        getUsers.CheckUser(username, loading);
    }

    private void CheckDatabaseForExistingUser() {
        Set<String> getData = data.getSearchResults();
        Log.e("search", getData.toString());
        ArrayList<Search> customData = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(getData.toString());
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String username = jsonObject.getString("username");
                    String email = jsonObject.getString("email");
                    String mob = jsonObject.getString("mobile");
                    String dp = jsonObject.getString("dp");
                    Search search = new Search(username, email, mob, dp);
                    customData.add(search);
                }
                searchArray = customData;
                adapter.LoadView(customData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void SaveResultClick(Search search) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", search.username);
            jsonObject.put("email", search.email);
            jsonObject.put("mobile", search.mobile);
            jsonObject.put("dp", search.image_path);
        } catch (JSONException e) {
            Log.e("SaveResultClick", e.toString());
        }
        Set<String> currentSet = data.getSearchResults();
        currentSet.add(jsonObject.toString());
        data.setSearchResults(currentSet);
    }

    @Override
    public void onHomeClick(View view, int position) {
        Search search = searchArray.get(position);
        SaveResultClick(search);
        Intent intent = new Intent(SearchUserActivity.this, UserAccountActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("username", search.username);
        bundle.putString("mobile", search.mobile);
        bundle.putString("email", search.email);
        bundle.putString("dp", search.image_path);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onUserSearch(ArrayList<Search> searchArrayList) {
        loading.smoothToHide();
        if (searchArrayList.isEmpty()) {
            Toast.makeText(SearchUserActivity.this, "No user found", Toast.LENGTH_SHORT).show();
        } else {
            searchArray = searchArrayList;
            adapter.LoadView(searchArrayList);
        }
    }
}
