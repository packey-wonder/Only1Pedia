package org.vaadin.presentation.views;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.tepi.filtertable.FilterTable;
import org.vaadin.backend.CustomerService;
import org.vaadin.cdiviewmenu.ViewMenuItem;
import org.vaadin.pagingcomponent.PagingComponent;
import org.vaadin.pagingcomponent.RangeDisplayer;
import org.vaadin.pagingcomponent.listener.impl.SimplePagingComponentListener;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;


/*
 * A very simple view that just displays an "about text". The view also has
 * a button to reset the demo date in the database.
 */
@CDIView("search")
//@RolesAllowed({"user"})
@ViewMenuItem(icon = FontAwesome.SUPPORT)
public class SearchView extends MVerticalLayout implements View {

    @Inject
    CustomerService service;

    private FilterTable filterTable;


    @PostConstruct
    void init() {


		VerticalLayout verticalContent = new VerticalLayout();
		verticalContent.addComponent(rangeDisplayerExemple());

		addComponents(verticalContent);

        setMargin(new MarginInfo(false, true, true, true));
        setStyleName(ValoTheme.LAYOUT_CARD);
    }

	/**
	 * Example with {@link RangeDisplayer}, i.e. display "Product 20-40 currently displayed".
	 */
	private VerticalLayout rangeDisplayerExemple() {
		// Test data
		List<Integer> items = createItems();

		final VerticalLayout mainLayout = new VerticalLayout();

		// Layout where we will display items (changing when we click next page).
		final VerticalLayout itemsArea = new VerticalLayout();

		// Visual controls (First, Previous,1 2 ..., Next, Last)
		final PagingComponent<Integer> pagingComponent = PagingComponent.paginate(items).addListener(new SimplePagingComponentListener<Integer>(itemsArea) {

			@Override
			protected Component displayItem(int index, Integer item) {
				return new Label(String.valueOf(item));
			}

		}).build();

		// We declare a RangeDisplayer which takes PagingComponent as parameter
		RangeDisplayer<Integer> rd = new RangeDisplayer<Integer>(pagingComponent);
		// We set the value to be displayed before range ex : results : 10-20
		// It's optional
		rd.setValue("results : ");

		mainLayout.addComponent(new Label("<h1>検索結果<h1>", ContentMode.HTML));
		mainLayout.addComponent(itemsArea);
		mainLayout.addComponent(pagingComponent);
		mainLayout.addComponent(rd);
		return mainLayout;
	}

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }


	/**
	 * @return a list of Integers
	 */
	private List<Integer> createItems() {
		List<Integer> items = new ArrayList<Integer>();
		for (Integer i = 1; i <= 300; i++) {
			items.add(i);
		}

		return items;
	}
}
