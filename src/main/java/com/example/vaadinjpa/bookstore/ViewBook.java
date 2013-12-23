package com.example.vaadinjpa.bookstore;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.JPAContainerItem;
import com.vaadin.addon.jpacontainer.fieldfactory.FieldFactory;
import com.vaadin.addon.jpacontainer.util.DefaultQueryModifierDelegate;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.Reindeer;

/**
 * This is a rudimentary general purpose CRUD view to list and edit JPA entities
 * with JPAContainer. Lists all entities in a table and puts the selected row
 * into a buffered form below it. Form uses {@link FieldFactory} to support most
 * common relation types.
 */
public class ViewBook extends AbsoluteLayout implements
		Property.ValueChangeListener, Handler, ClickListener {

	private JPAContainer<Book> container;
	private Table table;
	private Button addButton;
	private Button deleteButton;
	private Panel panel;
	private String persistenceUnit;
	private Window window;
	private Object[] tablePropertyIds;
	private Long authorId;
	public static final Long SELECT_ALL = -1L;
	public static final Long SELECT_NONE = -2L;
	
	public void hidePanel(){
		panel.setVisible(false);
	}
	
	public void refreshTable(){
		//showBooksFor(SELECT_ALL);
		showBooksFor(authorId);
	}

	public ViewBook(String persistenceUnit, Window awindow) {
		this.persistenceUnit = persistenceUnit;
		this.window = awindow;
		buildView();
	}

	public void setVisibleTableProperties(Object... tablePropertyIds) {
		this.tablePropertyIds = tablePropertyIds;
		table.setVisibleColumns(tablePropertyIds);
	}

	private void buildView() {
		setSizeFull();
		authorId = SELECT_ALL;
		panel = new Panel("Books");
		panel.setSizeFull();
		addComponent(panel);
		
		container = JPAContainerFactory.make(Book.class, persistenceUnit);
		table = new Table(null, container);
		
		table.setSizeFull();
		table.setSelectable(true);
		table.addListener(this);
		table.setImmediate(true);
		table.addActionHandler(this);
		panel.addComponent(table);

		addButton = new Button("+", this);
		addButton.setDescription("Add new book");
		addButton.setStyleName(Reindeer.BUTTON_SMALL);
		addComponent(addButton, "top:0;right:40px;");

		deleteButton = new Button("-", this);
		deleteButton.setDescription("Delete selected book");
		deleteButton.setStyleName(Reindeer.BUTTON_SMALL);
		deleteButton.setEnabled(false);
		addComponent(deleteButton, "top:0;right:5px;");

		// register action handler (enter and ctrl-n)
		panel.addActionHandler(this);
	}
	
	public void showBooksAll(){
		container.removeAllContainerFilters();
	}
	
	public void showBooksFor(Long authorID) {
		showBooksAll();
		this.authorId = authorID;
		if(SELECT_ALL == authorID)
			return;
		Filter filter = new Compare.Equal("author", authorID);
		container.addContainerFilter(filter);
	}

	@Override
	public void valueChange(ValueChangeEvent event) {
		Object itemId = event.getProperty().getValue();
		Item item = table.getItem(itemId);
		boolean entitySelected = item != null;
		deleteButton.setEnabled(entitySelected);
	}

	@Override
	public String getCaption() {
		return "Books";
	}

	private static final ShortcutAction SAVE = new ShortcutAction("Save", KeyCode.ENTER, null);
	private static final ShortcutAction SAVE2 = new ShortcutAction("^Save");
	private static final ShortcutAction NEW = new ShortcutAction("^New");
	private static final Action DELETE = new Action("Delete");

	private static final Action[] ACTIONS = new Action[] { NEW, DELETE };
	private static final Action[] SHORTCUT_ACTIONS = new Action[] { SAVE,SAVE2, NEW };

	@Override
	public Action[] getActions(Object target, Object sender) {
		if (sender == table) {
			return ACTIONS;
		} else {
			return SHORTCUT_ACTIONS;
		}
	}

	@Override
	public void handleAction(Action action, Object sender, Object target) {
		if (action == NEW) {
			addItem();
		} else if (action == DELETE) {
			deleteItem(target);
		} 
	}

	private void deleteItem(Object itemId) {
		JPAContainerItem item = (JPAContainerItem<Book>)container.getItem(itemId);
		Book author = (Book)item.getEntity();
		container.removeItem(itemId);
	}

	protected void addItem() {
		Book book = new Book();
		Object itemId = container.addEntity(book);
		table.setValue(itemId);
		Item item = container.getItem(itemId);
		WindowAddRecord<Book> wndAddBook = new WindowAddRecord<Book>(this, "Add new book", item, itemId, container, "title","genre","author");
		if(SELECT_ALL != authorId)
			wndAddBook.setAuthor(authorId);
        window.addWindow(wndAddBook);
        //refreshTable();
        //container.refreshItem(itemId);
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == addButton) {
			addItem();
		} else if (event.getButton() == deleteButton) {
			deleteItem(table.getValue());
		}
	}

	@Override
	public void attach() {
		super.attach();
		panel.focus();
	}
}