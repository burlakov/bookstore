package com.example.vaadinjpa.bookstore;
 
import java.io.Serializable;

import javax.persistence.*;
 
@Entity
@Table(name="Book")
public class Book implements Serializable {
    private static final long serialVersionUID = 1L;
 
    // nullable = true, затем, чтобы можно было сперва создать объект,
    // а затем передать его как Item в Form.setItemDataSource
    
    @Id 
    @GeneratedValue
    @Column(name = "ID")
    private Long id;
 
    @ManyToOne
    @JoinColumn(name = "AuthorID", nullable=true)
    private Author author;
    
    @Column(name = "Title", nullable=true)
    private String title;
    
    @Column(name = "Genre", nullable=true)
    private String genre;
 
    public Book() {
    }
 
    public Book(Author aauthor, String atitle, String agenre) {
    	author = aauthor;
        title = atitle;
        genre = agenre;
    }
  
    @Override
    public String toString() {
        return String.format("%s", title);
    }
    
    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }
    
    public Long getId(){
    	return id;
    }
}