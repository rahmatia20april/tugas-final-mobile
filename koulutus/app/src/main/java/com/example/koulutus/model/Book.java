package com.example.koulutus.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;

public class Book implements Parcelable {

    private String id;

    @SerializedName("bookTitle")
    private String bookTitle;

    @SerializedName("bookImage")
    private String bookImage;

    @SerializedName("bookDescription")
    private String bookDescription;

    @SerializedName("bookAuthor")
    private String bookAuthor;

    @SerializedName("bookPublisher")
    private String bookPublisher;

    @SerializedName("bookRank")
    private String bookRank;

    @SerializedName("bookIsbn")
    private String bookIsbn;

    @SerializedName("amazonBookUrl")
    private String amazonBookUrl;

    public Book(String id, String bookTitle, String bookImage, String bookAuthor, String bookPublisher, String bookRank) {
        this.id = id;
        this.bookTitle = bookTitle;
        this.bookImage = bookImage;
        this.bookAuthor = bookAuthor;
        this.bookPublisher = bookPublisher;
        this.bookRank = bookRank;
    }

    protected Book(Parcel in) {
        id = in.readString();
        bookTitle = in.readString();
        bookImage = in.readString();
        bookDescription = in.readString();
        bookAuthor = in.readString();
        bookPublisher = in.readString();
        bookRank = in.readString();
        bookIsbn = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookPublisher() {
        return bookPublisher;
    }

    public void setBookPublisher(String bookPublisher) {
        this.bookPublisher = bookPublisher;
    }

    public String getBookRank() {
        return bookRank;
    }

    public void setBookRank(String bookRank) {
        this.bookRank = bookRank;
    }

    public String getBookIsbn() {
        return bookIsbn;
    }

    public void setBookIsbn(String bookIsbn) {
        this.bookIsbn = bookIsbn;
    }

    public String getAmazonBookUrl() {
        return amazonBookUrl;
    }

    public void setAmazonBookUrl(String amazonBookUrl) {
        this.amazonBookUrl = amazonBookUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(bookTitle);
        dest.writeString(bookImage);
        dest.writeString(bookDescription);
        dest.writeString(bookPublisher);
        dest.writeString(bookAuthor);
        dest.writeString(bookRank);
        dest.writeString(bookIsbn);
    }
}
