package com.example.koulutus.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koulutus.DetailActivity;
import com.example.koulutus.R;
import com.example.koulutus.model.Book;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private List<Book> bookModels;
    private Context context;

    public BookAdapter(List<Book> bookModels, Context context) {
        this.bookModels = bookModels;
        this.context = context;
    }

    @NonNull
    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.ViewHolder holder, int position) {
        Book bookModel = bookModels.get(position);
        holder.bind(bookModel);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("bookModel", bookModel);
            intent.putExtra("link", bookModel.getAmazonBookUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return bookModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_bookImage;
        TextView tv_title, tv_publisher, tv_author, tv_bookRank;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_bookImage = itemView.findViewById(R.id.img_cover);
            tv_title = itemView.findViewById(R.id.tv_titleBookitle);
            tv_publisher = itemView.findViewById(R.id.tv_publisher);
            tv_author = itemView.findViewById(R.id.tv_author);
            tv_bookRank = itemView.findViewById(R.id.tv_bookRank1);
        }

        public void bind(Book bookModel) {
            Picasso.get().load(bookModel.getBookImage()).into(iv_bookImage);
            tv_title.setText(bookModel.getBookTitle());
            tv_publisher.setText(bookModel.getBookPublisher());
            tv_author.setText(bookModel.getBookAuthor());
            tv_bookRank.setText(bookModel.getBookRank());
        }
    }
}
