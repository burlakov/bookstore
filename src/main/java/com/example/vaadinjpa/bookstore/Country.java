package com.example.vaadinjpa.bookstore;
 
import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
 
@Entity
@Table(name="Country")
public class Country implements Serializable {
    private static final long serialVersionUID = 1L;
 
    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;
 
    @Column(name = "Name", nullable=false)
    private String name;

    @OneToMany(mappedBy="country")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Author> authors;

    public Country() {
    }
    
    public Country(String aname) {
        name = aname;
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