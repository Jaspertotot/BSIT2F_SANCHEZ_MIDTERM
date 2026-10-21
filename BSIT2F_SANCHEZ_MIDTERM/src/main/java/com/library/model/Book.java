package com.library.model;

/** One cataloged library book. */
public class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private String status;

    public Book(String title, String author, String isbn, String status) {
        this(0, title, author, isbn, status);
    }

    public Book(int id, String title, String author, String isbn, String status) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
