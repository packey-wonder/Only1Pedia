package org.vaadin.presentation.views;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;

import org.tepi.filtertable.FilterDecorator;
import org.tepi.filtertable.FilterTable;
import org.tepi.filtertable.demo.DemoFilterDecorator;
import org.tepi.filtertable.demo.DemoFilterDecorator.State;
import org.tepi.filtertable.demo.DemoFilterGenerator;
import org.vaadin.backend.CustomerService;
import org.vaadin.cdiviewmenu.ViewMenuItem;
import org.vaadin.cdiviewmenu.ViewMenuUI;
import org.vaadin.viritin.label.RichText;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.cdi.CDIView;
import com.vaadin.data.Container;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

/*
 * A very simple view that just displays an "about text". The view also has 
 * a button to reset the demo date in the database.
 */
@CDIView("search")
@RolesAllowed({"user"})
@ViewMenuItem(icon = FontAwesome.SUPPORT)
public class SearchView extends MVerticalLayout implements View {

    @Inject
    CustomerService service;

    private FilterTable filterTable;
    
    
    @PostConstruct
    void init() {
    	

        /* Create FilterTable */
        filterTable = buildFilterTable();

        //mainLayout.setSizeFull();
       // mainLayout.setSpacing(true);
       // mainLayout.setMargin(true);
        add(filterTable);
        setExpandRatio(filterTable, 1);
        add(buildButtons());
           	

        setMargin(new MarginInfo(false, true, true, true));
        setStyleName(ValoTheme.LAYOUT_CARD);
    }
    

    private FilterTable buildFilterTable() {
        FilterTable filterTable = new FilterTable();
        filterTable.setSizeFull();
        filterTable.setFilterDecorator(new DemoFilterDecorator());
        filterTable.setFilterGenerator(new DemoFilterGenerator());
        filterTable.setContainerDataSource(buildContainer());
        filterTable.setFilterBarVisible(true);
        return filterTable;
    }

    private Component buildButtons() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSizeUndefined();
        buttonLayout.setSpacing(true);
        Button showFilters = new Button("Show filter bar");
 
        showFilters.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				 filterTable.setFilterBarVisible(true);
				
			}
        });
        buttonLayout.addComponent(showFilters);
        Button hideFilters = new Button("Hide filter bar");
        hideFilters.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                filterTable.setFilterBarVisible(false);   
            }
        });
        buttonLayout.addComponent(hideFilters);
        return buttonLayout;
    }

    private Container buildContainer() {
        IndexedContainer cont = new IndexedContainer();
        Calendar c = Calendar.getInstance();

        cont.addContainerProperty("name", String.class, null);
        cont.addContainerProperty("id", Integer.class, null);
        cont.addContainerProperty("state", State.class, null);
        cont.addContainerProperty("date", Date.class, null);
        cont.addContainerProperty("validated", Boolean.class, null);

        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            cont.addItem(i);
            /* Set name and id properties */
            cont.getContainerProperty(i, "name").setValue("Order " + i);
            cont.getContainerProperty(i, "id").setValue(i);
            /* Set state property */
            int rndInt = random.nextInt(4);
            State stateToSet = State.CREATED;
            if (rndInt == 0) {
                stateToSet = State.PROCESSING;
            } else if (rndInt == 1) {
                stateToSet = State.PROCESSED;
            } else if (rndInt == 2) {
                stateToSet = State.FINISHED;
            }
            cont.getContainerProperty(i, "state").setValue(stateToSet);
            /* Set date property */
            cont.getContainerProperty(i, "date").setValue(c.getTime());
            c.add(Calendar.DAY_OF_MONTH, 1);
            /* Set validated property */
            cont.getContainerProperty(i, "validated").setValue(
                    random.nextBoolean());
        }
        return cont;
    }
    	
    	


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }
}
