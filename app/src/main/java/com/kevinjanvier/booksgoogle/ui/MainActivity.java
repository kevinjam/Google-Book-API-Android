package com.kevinjanvier.booksgoogle.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.kevinjanvier.booksgoogle.R;
import com.kevinjanvier.booksgoogle.data.entities.Book;
import com.kevinjanvier.booksgoogle.ui.adapter.BooksAdapter;
import com.kevinjanvier.booksgoogle.utils.ApiUtil;
import com.kevinjanvier.booksgoogle.utils.SharedPref;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private ProgressBar mLoadingProgress;
    private TextView tvError;
    private RecyclerView rvBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Intent intent = getIntent();
        String query = intent.getStringExtra("Query");

        mLoadingProgress = findViewById(R.id.pb_loading);
        tvError = findViewById(R.id.tv_error);
         rvBooks = findViewById(R.id.rv_book);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        rvBooks.setLayoutManager(linearLayout);

        URL bookURL;

        try {
            if (query ==null || query.isEmpty()){
                bookURL = ApiUtil.buildUrl("cooking");
            }else {
                bookURL = ApiUtil.buildUrl("cooking");

            }

           new BooksQueryTask().execute(bookURL);
       }catch (Exception e){
           Log.d("error", e.getMessage());
       }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_list_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        ArrayList<String> recentList = SharedPref.getQueryList(getApplicationContext());
        int itemNum = recentList.size();
        MenuItem recentMenu;
        for (int i =0; i<itemNum;i++){
            recentMenu = menu.add(Menu.NONE,i, Menu.NONE, recentList.get(i));

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_advance_search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                return true;

            default:
                int postion = item.getItemId() + 1;
                String preferenceName = SharedPref.QUERY + String.valueOf(postion);
                String query = SharedPref.getPreferenceString(getApplicationContext(), preferenceName);
                String[] prefParams = query.split("\\,");
                String[] queryParams = new String[4];
                for (int i = 0; i<preferenceName.length();i++){
                    queryParams[i] = prefParams[i];
                }
                URL bookUrl = ApiUtil.buildURL(
                        queryParams[0] == null?"":queryParams[0],
                        queryParams[1] == null?"":queryParams[1],
                        queryParams[2] == null?"":queryParams[2],
                        queryParams[3] == null?"":queryParams[3]


                );
                new BooksQueryTask().execute(bookUrl);
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        try {
            URL bookName = ApiUtil.buildUrl(query);
            new BooksQueryTask().execute(bookName);

        }catch (Exception e){
            Log.d("Error " , e.getMessage());
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public class BooksQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL searchURL = urls[0];
            String result = null;
            try {
                result = ApiUtil.getJson(searchURL);
            }catch (IOException e){
                Log.d("Error " , e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            mLoadingProgress.setVisibility(View.INVISIBLE);

            if (result== null){
                mLoadingProgress.setVisibility(View.INVISIBLE);
                rvBooks.setVisibility(View.INVISIBLE);
                tvError.setVisibility(View.VISIBLE);

            }else {
                rvBooks.setVisibility(View.VISIBLE);
                tvError.setVisibility(View.INVISIBLE);
                ArrayList<Book> books = ApiUtil.getBooksFromJson(result);
                BooksAdapter adapter = new BooksAdapter(books);
                rvBooks.setAdapter(adapter);

            }


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingProgress.setVisibility(View.VISIBLE);
        }
    }
}
