package com.kevinjanvier.booksgoogle.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.kevinjanvier.booksgoogle.R;
import com.kevinjanvier.booksgoogle.data.entities.Book;
import com.kevinjanvier.booksgoogle.databinding.ActivityBookDetailsBinding;

public class BookDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_book_details);
        ActivityBookDetailsBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_book_details);
        Book book = getIntent().getParcelableExtra("Book");
        binding.setBook(book);
    }
}
