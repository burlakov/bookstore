package com.example.vaadinjpa.bookstore;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;




//import com.example.vaadinjpa.vaadinjpa.ui.BasicCrudView;
import com.vaadin.Application;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Window;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class MyVaadinApplication extends Application {

	public static final String PERSISTENCE_HIBERNATE = "hibernate-book-persistence";
	
	@Override
	public void init() {
		System.out.println("START");
		fillpersons();
		filldata();
		setMainWindow(new AutoCrudViews());
		System.out.println("STOP");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	class AutoCrudViews extends Window {
		
		public AutoCrudViews() {
			final HorizontalSplitPanel horizontalSplitPanel = new HorizontalSplitPanel();

			Metamodel metamodel = JPAContainerFactory.createEntityManagerForPersistenceUnit(PERSISTENCE_HIBERNATE).getEntityManagerFactory().getMetamodel();
			
			EntityType<?> entityType = metamodel.entity(Book.class);
			Class<?> javaType = entityType.getJavaType();
			ViewBook viewBook = new ViewBook(PERSISTENCE_HIBERNATE, this);
			viewBook.setVisibleTableProperties("title","genre");
			
			entityType = metamodel.entity(Author.class);
			javaType = entityType.getJavaType();
			ViewAuthor viewAuthor = new ViewAuthor(PERSISTENCE_HIBERNATE, viewBook, this);
			viewAuthor.setVisibleTableProperties("name","country");
			
			horizontalSplitPanel.setSplitPosition(500,HorizontalSplitPanel.UNITS_PIXELS);
			horizontalSplitPanel.setFirstComponent(viewAuthor);
			horizontalSplitPanel.setSecondComponent(viewBook);
			setContent(horizontalSplitPanel);

		}
	}
	
	static void filldata(){
		EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit(PERSISTENCE_HIBERNATE);
		
		Country Russia = new Country("Russian Federation"); 
        Country USA = new Country("USA"); 
        Country Germany = new Country("Germany"); 
		Country China = new Country("China"); 
        Country Japan = new Country("Japan"); 
        Country India = new Country("India");
        
        /* для отладки
        Author Burlakov = new Author("Burlakov Alexey", Russia); 
        Author Hemingway = new Author("Hemingway Ernest", USA); 
        Author Schiller = new Author("Johann Christoph Friedrich von Schiller", Germany); 
        
        Book warandpeace = new Book(Burlakov, "The War and Peace",  "Classic");
        Book article = new Book(Burlakov, "The Discription of the CPU Commands",  "IT");
        Book novel = new Book(Hemingway, "The Oldman and the Sea",  "Journal"); 
        Book drama = new Book(Schiller, "Die Reauber",  "Classic"); 
        */
        
		long size = (Long) em.createQuery("SELECT COUNT(c) FROM Country c").getSingleResult();
		if (size == 0) {

			em.getTransaction().begin();
			
			em.persist(Russia);
			em.persist(USA);
			em.persist(Germany);
			em.persist(China);
			em.persist(Japan);			
			em.persist(India);

			em.getTransaction().commit();
		}

		/* Для отладки
		size = (Long) em.createQuery("SELECT COUNT(a) FROM Author a").getSingleResult();
		if (size == 0) {

			em.getTransaction().begin();
			
			em.persist(Burlakov);
			em.persist(Hemingway);			
			em.persist(Schiller);

			em.getTransaction().commit();
		}
		
		size = (Long) em.createQuery("SELECT COUNT(b) FROM Book b").getSingleResult();
		if (size == 0) {

			em.getTransaction().begin();
			
			em.persist(article);
			em.persist(warandpeace);
			em.persist(novel);			
			em.persist(drama);

			em.getTransaction().commit();
		}
		*/
		em.close();
		
	}
	
	static void fillpersons(){
		EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit(PERSISTENCE_HIBERNATE);

		long size = (Long) em.createQuery("SELECT COUNT(p) FROM Person p").getSingleResult();
		if (size == 0) {
			// create three Person objects as test data

			em.getTransaction().begin();
			Person boss = new Person();
			boss.setFirstName("John");
			boss.setLastName("Bigboss");
			boss.setCity("Turku");
			boss.setPhoneNumber("+358 02 555 221");
			boss.setZipCode("20200");
			boss.setStreet("Ruukinkatu 2-4");
			em.persist(boss);

			Person p = new Person();
			p.setFirstName("Marc");
			p.setLastName("Hardworker");
			p.setCity("Turku");
			p.setPhoneNumber("+358 02 555 222");
			p.setZipCode("20200");
			p.setStreet("Ruukinkatu 2-4");
			p.setBoss(boss);
			em.persist(p);

			Person I = new Person();
			I.setFirstName("Alex");
			I.setLastName("Burlakov");
			I.setCity("Irkutsk");
			I.setPhoneNumber("+79025788853");
			I.setZipCode("664075");
			I.setStreet("Baikalskaja 215");
			em.persist(I);
			
			em.getTransaction().commit();
		}

		em.close();
	}
}
