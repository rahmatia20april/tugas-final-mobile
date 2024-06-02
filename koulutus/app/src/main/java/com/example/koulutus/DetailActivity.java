package com.example.koulutus;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koulutus.adapter.BookAdapter;
import com.example.koulutus.model.Book;
import com.example.koulutus.sqlite.DbConfig;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private DbConfig dbConfig;
    private BookAdapter bookAdapter;
    private ImageView btn_back;
    private Button btn_roadMore;
    private boolean isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        dbConfig = new DbConfig(this);

        String link = getIntent().getStringExtra("link");

        btn_back = findViewById(R.id.btn_back);
//        btn_roadMore = findViewById(R.id.btn_readMore);

        btn_back.setOnClickListener(v -> {
            finish();
        });

        Book bookModel = getIntent().getParcelableExtra("bookModel");
        if (bookModel != null) {
            displayBookDetails(bookModel);
        }
    }

    private void displayBookDetails(Book bookModel) {
        TextView tvTitle = findViewById(R.id.tv_titleBook);
//        TextView tvDescription = findViewById(R.id.tv_detailSummary);
        TextView tvPublisher = findViewById(R.id.tv_bookPublisher);
        TextView tvAuthor = findViewById(R.id.tv_bookAuthor);
        TextView tvRank = findViewById(R.id.tv_bookRank);
        ImageView ivImage = findViewById(R.id.img_poster);

        tvTitle.setText(bookModel.getBookTitle());
//        tvDescription.setText(bookModel.getBookDescription());
        tvPublisher.setText(bookModel.getBookPublisher());
        tvAuthor.setText(bookModel.getBookAuthor());
        tvRank.setText(bookModel.getBookRank());
        Picasso.get().load(bookModel.getBookImage()).into(ivImage);
    }
}