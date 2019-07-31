package com.kevinjanvier.booksgoogle.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kevinjanvier.booksgoogle.R;
import com.kevinjanvier.booksgoogle.utils.ApiUtil;
import com.kevinjanvier.booksgoogle.utils.SharedPref;

import java.net.URL;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final EditText etTitle = findViewById(R.id.etTitile);
        final EditText etAuthor = findViewById(R.id.etAuthor);
        final EditText etPublisher = findViewById(R.id.etPublisher);
        final EditText etIsbn = findViewById(R.id.etIsbn);
        final Button button = findViewById(R.id.btnSearch);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = etTitle.getText().toString().trim();
                String author = etAuthor.getText().toString().trim();
                String publisher = etPublisher.getText().toString().trim();
                String isbn = etIsbn.getText().toString().trim();

                if (title.isEmpty() && author.isEmpty() && publisher.isEmpty() && isbn.isEmpty()){
                    String message = getString(R.string.no_search_found);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }else {
                    URL queryURL = ApiUtil.buildURL(title, author, publisher, isbn);

                    Context context = getApplicationContext();
                    int position = SharedPref.getPreferenceInt(context, SharedPref.POSITION);
                    if (position ==0 || position ==5){
                        position =1;

                    }else {
                        position++;
                    }
                    String key  = SharedPref.QUERY + String.valueOf(position);
                    String value = title + "," + author + "," + publisher + "," + isbn;
                    SharedPref.setPreferenceString(context, key, value);
                    SharedPref.setPreferenceInt(context, SharedPref.POSITION, position);


                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("Query", queryURL);
                    startActivity(intent);
                }
            }
        });
    }
}
