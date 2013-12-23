package com.example.vaadinjpa.bookstore;


import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.JPAContainerItem;
import com.vaadin.addon.jpacontainer.fieldfactory.FieldFactory;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;


/**
 * This is a rudimentary general purpose CRUD view to list and edit JPA entities
 * with JPAContainer. Lists all entities in a table and puts the selected row
 * into a buffered form below it. Form uses {@link FieldFactory} to support most
 * common relation types.
 */
public class ViewAuthor extends AbsoluteLayout implements
		Property.ValueChangeListener, Handler, ClickListener {

	private JPAContainer<Author> container;
	private Table table;
	private Button addButton;
	private Button deleteButton;
	private Panel panel;
	private String persistenceUnit;
	private ViewBook viewBook; 
	private Window window;
	
	public void hidePanel(){
		panel.setVisible(false);
	}

	public ViewAuthor(String persistenceUnit, ViewBook viewBook, Window awindow) {
		this.viewBook = viewBook;
		this.persistenceUnit = persistenceUnit;
		this.window = awindow;
		setSizeFull();
		initContainer();
		buildView();
	}

	protected Table getTable() {
		return table;
	}

	public void setVisibleTableProperties(Object... tablePropertyIds) {
		table.setVisibleColumns(tablePropertyIds);
	}

	protected void buildView() {
		setSizeFull();
		panel = new Panel(getCaption());
		panel.setSizeFull();
		addComponent(panel);

		table.setSizeFull();
		table.setSelectable(true);
		table.addListener(this);
		table.setImmediate(true);
		table.addActionHandler(this);
		table.setSizeFull();
		panel.addComponent(table);
		
		
		addButton = new Button("+", this);
		addButton.setDescription("Add new author");
		addButton.setStyleName(Reindeer.BUTTON_SMALL);
		addComponent(addButton, "top:0;right:40px;");

		deleteButton = new Button("-", this);
		deleteButton.setDescription("Delete selected author");
		deleteButton.setStyleName(Reindeer.BUTTON_SMALL);
		deleteButton.setEnabled(false);
		addComponent(deleteButton, "top:0;right:5px;");

		// register action handler (enter and ctrl-n)
		panel.addActionHandler(this);
	}

	protected void initContainer() {
		container = JPAContainerFactory.make(Author.class, persistenceUnit);
		table = new Table(null, container);
	}

	@Override
	public void valueChange(ValueChangeEvent event) {
		Object itemId = event.getProperty().getValue();
		JPAContainerItem item = (JPAContainerItem<Author>)table.getItem(itemId);
		Author author = (Author)item.getEntity();
		viewBook.showBooksFor(author.getId());
		boolean entitySelected = item != null;
		deleteButton.setEnabled(entitySelected);
	}

	@Override
	public String getCaption() {
		return "Authors";
	}

	//private static final ShortcutAction SAVE = new ShortcutAction("Save", KeyCode.ENTER, null);
	//private static final ShortcutAction SAVE2 = new ShortcutAction("^Save");
	private static final ShortcutAction NEW = new ShortcutAction("^New");
	private static final Action DELETE = new Action("Delete");

	private static final Action[] ACTIONS = new Action[] { NEW, DELETE };
	//private static final Action[] SHORTCUT_ACTIONS = new Action[] { SAVE, SAVE2, NEW };

	@Override
	public Action[] getActions(Object target, Object sender) {
		return ACTIONS;
		/*if (sender == table) {
			return ACTIONS;
		} else {
			return SHORTCUT_ACTIONS;
		}*/
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
		
		JPAContainerItem item = (JPAContainerItem<Author>)table.getItem(itemId);
		Author author = (Author)item.getEntity();
		container.removeItem(itemId);
		viewBook.showBooksFor(ViewBook.SELECT_NONE);//очищаем правую таблицу
	}

	protected void addItem() {
		Author author = new Author();
		Object itemId = container.addEntity(author);	
		table.setValue(itemId);
		Item item = table.getItem(itemId);
        WindowAddRecord<Author> wndAddAuthor = new WindowAddRecord<Author>(null, "Add new Author", item, itemId, container, "name", "country");
        window.addWindow(wndAddAuthor);
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