/**
 *
 */
package org.vaadin.presentation.views;

import javax.inject.Inject;

import org.vaadin.backend.BookService;
import org.vaadin.backend.domain.Book;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.ui.Component;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author ace-wonder
 *
 */
public class BookReference extends AbstractForm<Book> {

    @Inject
    BookService service;

    TextField title = new MTextField("書名").withFullWidth();
    TextField heading = new MTextField("見出し").withFullWidth();
    TextArea body = new TextArea("本文");


	@Override
	protected Component createContent() {

        return new MVerticalLayout(
                new Header("書籍データ編集").setHeaderLevel(3),
                new MFormLayout(
                        title,
                        heading,
                        body
                ).withFullWidth(),
                getToolbar()
        ).withStyleName(ValoTheme.LAYOUT_CARD);
	}

}
