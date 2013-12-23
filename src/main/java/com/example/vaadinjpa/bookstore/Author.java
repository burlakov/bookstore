package com.example.vaadinjpa.bookstore;
 
import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
 
@Entity
@Table(name="Author")
public class Author implements Serializable {
    private static final long serialVersionUID = 1L;
 
    @Id 
    @GeneratedValue
    @Column(name = "ID")
    private Long id;
 
    //@Column(name = "CountryID")
    //private long countryId;
    
    @ManyToOne
    @JoinColumn(name="CountryID", nullable=true)
    private Country country;
        
    @Column(name = "Name", nullable=true)
    private String name;
 
    @OneToMany(mappedBy="author")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Book> books;
    
    public void SetCountry(Country acountry){
    	country = acountry;
    }
    
    public Author() {
    }
 
    public Author(String aname, Country acountry) {
    	country = acountry;
        name = aname;
    }
    
    public Author(String aname, Long acountryId){
    	
    }
  
    public Long getId(){
    	return id;
    }
    
    public Object[] getBooks(){
    	books.toArray(); 
    	Book book1 = new Book(this, "qwe","asd");
    	Book book2 = new Book(this, "rty","fgh");
    	Book books[] = {book1, book2};
    	return books;
    }
    
    @Override
    public String toString() {
        return String.format("%s", name);
    }
    
    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }
}