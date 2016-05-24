package org.vaadin.presentation.views;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.vaadin.backend.BookService;
import org.vaadin.backend.domain.Book;
import org.vaadin.cdiviewmenu.ViewMenuItem;
import org.vaadin.presentation.AppUI;
import org.vaadin.presentation.ScreenSize;
import org.vaadin.presentation.views.BookEvent.Type;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.fields.MValueChangeEvent;
import org.vaadin.viritin.fields.MValueChangeListener;
import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.cdi.CDIView;
import com.vaadin.event.FieldEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

/**
 * A view that lists Books in a Table and lets user to choose one for
 * editing. There is also RIA features like on the fly filtering.
 */
@CDIView("books")
//@RolesAllowed({"user"})
@ViewMenuItem(icon = FontAwesome.BOOK, title = "書籍データ編集" , order = ViewMenuItem.BEGINNING)
public class BookListView extends MVerticalLayout implements View {

    @Inject
    private BookService service;

    @Inject
    BookForm bookEditor;

    // Introduce and configure some UI components used on this view
    MTable<Book> bookTable = new MTable(Book.class).withFullWidth().
            withFullHeight();

    MHorizontalLayout mainContent = new MHorizontalLayout(bookTable).
            withFullWidth().withMargin(false);

    TextField filter = new TextField();

    Header header = new Header("Books").setHeaderLevel(2);

    Button addButton = new MButton(FontAwesome.EDIT,
            new Button.ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    addBook();
                }
            });

    @PostConstruct
    public void init() {

        /*
         * Add value change listener to table that opens the selected book into
         * an editor.
         */
        bookTable.addMValueChangeListener(new MValueChangeListener<Book>() {

            @Override
            public void valueChange(MValueChangeEvent<Book> event) {
                editBook(event.getValue());
            }
        });

        /*
         * Configure the filter input and hook to text change events to
         * repopulate the table based on given filter. Text change
         * events are sent to the server when e.g. user holds a tiny pause
         * while typing or hits enter.
         * */
        filter.setInputPrompt("Filter books...");
        filter.addTextChangeListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(FieldEvents.TextChangeEvent textChangeEvent) {
                listBooks(textChangeEvent.getText());
            }
        });


        /* "Responsive Web Design" can be done with plain Java as well. Here we
         * e.g. do selective layouting and configure visible columns in
         * table based on available width */
        layout();
        adjustTableColumns();
        /* If you wish the UI to adapt on window resize/page orientation
         * change, hook to BrowserWindowResizeEvent */
        UI.getCurrent().setResizeLazy(true);
        Page.getCurrent().addBrowserWindowResizeListener(
                new Page.BrowserWindowResizeListener() {
                    @Override
                    public void browserWindowResized(
                            Page.BrowserWindowResizeEvent browserWindowResizeEvent) {
                                adjustTableColumns();
                                layout();
                            }
                });

        listBooks();
    }

    /**
     * Do the application layout that is optimized for the screen size.
     * <p>
     * Like in Java world in general, Vaadin developers can modularize their
     * helpers easily and re-use existing code. E.g. In this method we are using
     * extended versions of Vaadins basic layout that has "fluent API" and this
     * way we get bit more readable code. Check out vaadin.com/directory for a
     * huge amount of helper libraries and custom components. They might be
     * valuable for your productivity.
     * </p>
     */
    private void layout() {
        removeAllComponents();
        if (ScreenSize.getScreenSize() == ScreenSize.LARGE) {
            addComponents(
                    new MHorizontalLayout(header, filter, addButton)
                    .expand(header)
                    .alignAll(Alignment.MIDDLE_LEFT),
                    mainContent
            );

            filter.setSizeUndefined();
        } else {
            addComponents(
                    header,
                    new MHorizontalLayout(filter, addButton)
                    .expand(filter)
                    .alignAll(Alignment.MIDDLE_LEFT),
                    mainContent
            );
        }
        setMargin(new MarginInfo(false, true, true, true));
        expand(mainContent);
    }

    /**
     * Similarly to layouts, we can adapt component configurations based on the
     * client details. Here we configure visible columns so that a sane amount
     * of data is displayed for various devices.
     */
    private void adjustTableColumns() {
        if (ScreenSize.getScreenSize() == ScreenSize.LARGE) {
            bookTable.setVisibleColumns("title", "heading", "body",
                    "link");
            bookTable.setColumnHeaders("タイトル", "見出し", "本文",
                    "リンク");
        } else {
            // Only show one (generated) column with combined first + last name
            if (bookTable.getColumnGenerator("body") == null) {
                bookTable.addGeneratedColumn("body",
                        new Table.ColumnGenerator() {
                            @Override
                            public Object generateCell(Table table, Object o,
                                    Object o2) {
                                Book c = (Book) o;
                                return c.getTitle() + " " + c.getHeading();
                            }
                        });
            }
            if (ScreenSize.getScreenSize() == ScreenSize.MEDIUM) {
                bookTable.setVisibleColumns("title", "heading");
                bookTable.setColumnHeaders("タイトル", "見出し");
            } else {
                bookTable.setVisibleColumns("heading");
                bookTable.setColumnHeaders("見出し");
            }
        }
    }

    /* ******* */
    // Controller methods.
    //
    // In a big project, consider using separate controller/presenter
    // for improved testability. MVP is a popular pattern for large
    // Vaadin applications.
    private void listBooks() {
        // Here we just fetch data straight from the EJB.
        //
        // If you expect a huge amount of data, do proper paging,
        // or use lazy loading Vaadin Container like LazyQueryContainer
        // See: https://vaadin.com/directory#addon/lazy-query-container:vaadin
        bookTable.setBeans(new ArrayList<>(service.findAll()));
    }

    private void listBooks(String filterString) {
        bookTable.setBeans(new ArrayList<>(service.findByName(filterString)));
    }

    void editBook(Book book) {
        if (book != null) {
            openEditor(book);
        } else {
            closeEditor();
        }
    }

    void addBook() {
        openEditor(new Book());
    }

    private void openEditor(Book book) {
        bookEditor.setEntity(book);
        // display next to table on desktop class screens
        if (ScreenSize.getScreenSize() == ScreenSize.LARGE) {
            mainContent.addComponent(bookEditor);
            bookEditor.focusFirst();
        } else {
            // Replace this view with the editor in smaller devices
            AppUI.get().getContentLayout().
                    replaceComponent(this, bookEditor);
        }
    }

    private void closeEditor() {
        // As we display the editor differently in different devices,
        // close properly in each modes
        if (bookEditor.getParent() == mainContent) {
            mainContent.removeComponent(bookEditor);
        } else {
            AppUI.get().getContentLayout().
                    replaceComponent(bookEditor, this);
        }
    }

    /* These methods gets called by the CDI event system, which is also
     * available for Vaadin UIs when using Vaadin CDI add-on. In this
     * example events are arised from BookForm. The CDI event system
     * is a great mechanism to decouple components.
     */
    void saveBook(@Observes @BookEvent(Type.SAVE) Book book) {
        listBooks();
        closeEditor();
    }

    void resetBook(@Observes @BookEvent(Type.REFRESH) Book book) {
        listBooks();
        closeEditor();
    }

    void deleteBook(@Observes @BookEvent(Type.DELETE) Book book) {
        closeEditor();
        listBooks();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

}
