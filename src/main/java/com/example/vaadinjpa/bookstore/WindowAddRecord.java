package com.example.vaadinjpa.bookstore;
import java.util.Arrays;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.fieldfactory.FieldFactory;
import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;


public class WindowAddRecord<T> extends Window {
	
	private JPAContainer<T> container;
	private Form form; 	
	private WindowAddRecord<T> thiswindow;	// чтобы закрывать по кнопке
	private Object itemId;
	private ViewBook viewBook;
	
	@SuppressWarnings("deprecation")
	public WindowAddRecord(ViewBook aviewBook, String caption, Item item, Object aitemId, JPAContainer<T> acontainer, Object... tablePropertyIds) {
		super(caption);
		this.thiswindow = this;
		this.container = acontainer;
		this.itemId = aitemId;
		this.viewBook = aviewBook;
		setWidth(500, UNITS_PIXELS);
		setModal(true);
		VerticalLayout vl = new VerticalLayout();
		setContent(vl);
		vl.setMargin(true);		
		
		form = new Form();
		form.setVisible(true);
		form.setWriteThrough(false);
		form.setCaption("Fill out prerequisites");
		form.setFormFieldFactory(new FieldFactory());
        
        Button commit = new Button("Save", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				form.commit();
				if(null != viewBook)
					viewBook.refreshTable();
				thiswindow.getParent().removeWindow(thiswindow);
			}
		});
		commit.setStyleName(Reindeer.BUTTON_DEFAULT);
		
		Button discard = new Button("Cancel", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				container.removeItem(itemId);
				form.discard();
				thiswindow.getParent().removeWindow(thiswindow);
			}
		});
		form.getFooter().addComponent(commit);
		form.getFooter().addComponent(discard);
		form.getLayout().setMargin(true);
		form.getFooter().setMargin(false, true, false, true);
		((HorizontalLayout) form.getFooter()).setSpacing(true);
		form.setItemDataSource(item, Arrays.asList(tablePropertyIds));
		vl.addComponent(form);

		//form.focus();
        center();
	}
	
	// вызывать только при создании книги
	public void setAuthor(Long authorId){
		form.getField("author").setValue(authorId);
	}
}
