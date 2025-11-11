package org.example.demo.model;

import java.io.Serializable;

public class Document implements Serializable {
    private Integer id;
    private String ISBN;
    private String name;
    private String author;
    private String publisher;
    private Integer yearOfPublication;
    private String genre;
    private String description;

    public Document() {}

    public Document(Integer id, String ISBN, String name, String author, String publisher, Integer yearOfPublication, String genre, String description) {
        this.id = id;
        this.ISBN = ISBN;
        this.name = name;
        this.author = author;
        this.publisher = publisher;
        this.yearOfPublication = yearOfPublication;
        this.genre = genre;
        this.description = description;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getISBN() { return ISBN; }
    public void setISBN(String ISBN) { this.ISBN = ISBN; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public Integer getYearOfPublication() { return yearOfPublication; }
    public void setYearOfPublication(Integer yearOfPublication) { this.yearOfPublication = yearOfPublication; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
