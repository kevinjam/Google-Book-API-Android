package com.kevinjanvier.booksgoogle.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kevinjanvier.booksgoogle.R;
import com.kevinjanvier.booksgoogle.data.entities.Book;
import com.kevinjanvier.booksgoogle.ui.BookDetails;

import java.util.ArrayList;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookViewHolder> {

    ArrayList<Book> books;
    public  BooksAdapter(ArrayList<Book> books){
        this.books = books;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.items_book,parent, false);
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.bind(book);

    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class  BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvTitle;
        TextView tvAuthors;
        TextView tvDate;
        TextView tvPublisher;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvAuthors = itemView.findViewById(R.id.tv_author);
            tvDate = itemView.findViewById(R.id.tv_publisherDate);
            tvPublisher = itemView.findViewById(R.id.tv_publisher);

            itemView.setOnClickListener(this);
        }

        public void bind(Book book){
            tvTitle.setText(book.title);
            String authours ="";
            tvAuthors.setText(book.authors);
            tvDate.setText(book.publishedDate);
            tvPublisher.setText(book.publisher);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Book selectedBook = books.get(position);
            Intent intent = new Intent(view.getContext(), BookDetails.class);
            intent.putExtra("Book", selectedBook);
            view.getContext().startActivity(intent);

        }
    }
}
