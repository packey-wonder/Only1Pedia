/**
 *
 */
package org.vaadin.presentation.views;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.backend.BookService;
import org.vaadin.backend.domain.Book;
import org.vaadin.cdiviewmenu.ViewMenuItem;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;

/**
 * @author ace-wonder
 *
 */
@CDIView("encyclopedia")
@ViewMenuItem(icon = FontAwesome.BOOK,title = "仏語検索" )
public class Encyclopedia extends MVerticalLayout implements View {

    @Inject
    private BookService service;


    MHorizontalLayout mainContent = new MHorizontalLayout().
            withFullWidth().withMargin(false);


    TextField filter = new TextField();

    Header header = new Header("仏語検索").setHeaderLevel(2);

    Button searchButton = new MButton(FontAwesome.SEARCH,
            new Button.ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {
                	addSearch(filter.getValue());
                }
            });

    @PostConstruct
    public void init() {
        /*
         * Configure the filter input and hook to text change events to
         * repopulate the table based on given filter. Text change
         * events are sent to the server when e.g. user holds a tiny pause
         * while typing or hits enter.
         * */
    	filter.setWidth("300px");
        filter.setInputPrompt("調べたい語句を入れて下さい");

        layout();

    }

    private void layout() {
    	addComponents(new MHorizontalLayout(header, filter, searchButton)
                .expand(header)
                .alignAll(Alignment.MIDDLE_LEFT),
                mainContent
        );

    }

    private void addSearch() {
        removeAllComponents();
        layout();

        ArrayList<Book>books=new ArrayList<>(service.findAll());

    	for(Book b:books){

    		Panel panel = new Panel(b.getTitle()+ " " +b.getHeading());
    		panel.setWidth("100%");
    		panel.setContent(new Label (b.getBody().substring(0,150)
    				+ "..."
    				+ "<a href=''>全文を見る</a>",ContentMode.HTML));

    		add(panel);
    	}
    }

    private void addSearch(String filterString) {
        removeAllComponents();

        layout();

        ArrayList<Book>books=new ArrayList<>(service.findByName(filterString));


//        MHorizontalLayout MH = new MHorizontalLayout();
//        MH.removeAllComponents();

        System.err.println("filterString= "+filterString);

    	for(Book b:books){

        		Panel panel = new Panel(b.getTitle()+ " " +b.getHeading());
        		panel.setWidth("100%");
        		panel.setContent(new Label (b.getBody().substring(0,150)
        				+ "..."
        				+ "<a href=''>全文を見る</a>",ContentMode.HTML));

        		add(panel);
    	}

//    	addComponents(MH);
    }

	/* (非 Javadoc)
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
