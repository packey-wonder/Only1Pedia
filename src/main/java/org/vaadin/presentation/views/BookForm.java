package org.vaadin.presentation.views;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.inject.Inject;

import org.vaadin.backend.BookService;
import org.vaadin.backend.domain.Book;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

/**
 * A UI component built to modify Book entities. The used superclass
 * provides binding to the entity object and e.g. Save/Cancel buttons by
 * default. In larger apps, you'll most likely have your own customized super
 * class for your forms.
 * <p>
 * Note, that the advanced bean binding technology in Vaadin is able to take
 * advantage also from Bean Validation annotations that are used also by e.g.
 * JPA implementation. Check out annotations in Book objects email field and
 * how they automatically reflect to the configuration of related fields in UI.
 * </p>
 */
public class BookForm extends AbstractForm<Book> {

    @Inject
    BookService service;

    // Prepare some basic field components that our bound to entity property
    // by naming convetion, you can also use PropertyId annotation
    TextField title = new MTextField("書名").withFullWidth();
    TextField heading = new MTextField("見出し").withFullWidth();
    TextArea body = new TextArea("本文");
    // body.setMaxLength(140);
    TextField link = new MTextField("リンク").withFullWidth();

    @Override
    protected Component createContent() {

        setStyleName(ValoTheme.LAYOUT_CARD);

        return new MVerticalLayout(
                new Header("書籍データ編集").setHeaderLevel(3),
                new MFormLayout(
                        title,
                        heading,
                        body,
                        link
                ).withFullWidth(),
                getToolbar()
        ).withStyleName(ValoTheme.LAYOUT_CARD);
    }

    @PostConstruct
    void init() {
        setEagerValidation(true);
        setSavedHandler(new SavedHandler<Book>() {

            @Override
            public void onSave(Book entity) {
                try {
                    // make EJB call to save the entity
                    service.saveOrPersist(entity);
                    // fire save event to let other UI components know about
                    // the change
                    saveEvent.fire(entity);
                } catch (EJBException e) {
                    /*
                     * The Book object uses optimitic locking with the
                     * version field. Notify user the editing didn't succeed.
                     */
                    Notification.show("The book was concurrently edited "
                            + "by someone else. Your changes were discarded.",
                            Notification.Type.ERROR_MESSAGE);
                    refrehsEvent.fire(entity);
                }
            }
        });
        setResetHandler(new ResetHandler<Book>() {

            @Override
            public void onReset(Book entity) {
                refrehsEvent.fire(entity);
            }
        });
        setDeleteHandler(new DeleteHandler<Book>() {
            @Override
            public void onDelete(Book entity) {
                service.deleteEntity(getEntity());
                deleteEvent.fire(getEntity());
            }
        });
    }

    @Override
    protected void adjustResetButtonState() {
        // always enabled in this form
        getResetButton().setEnabled(true);
        getDeleteButton().setEnabled(getEntity() != null && getEntity().isPersisted());
    }

    /* "CDI interface" to notify decoupled components. Using traditional API to
     * other componets would probably be easier in small apps, but just
     * demonstrating here how all CDI stuff is available for Vaadin apps.
     */
    @Inject
    @BookEvent(BookEvent.Type.SAVE)
    javax.enterprise.event.Event<Book> saveEvent;

    @Inject
    @BookEvent(BookEvent.Type.REFRESH)
    javax.enterprise.event.Event<Book> refrehsEvent;

    @Inject
    @BookEvent(BookEvent.Type.DELETE)
    javax.enterprise.event.Event<Book> deleteEvent;
}
