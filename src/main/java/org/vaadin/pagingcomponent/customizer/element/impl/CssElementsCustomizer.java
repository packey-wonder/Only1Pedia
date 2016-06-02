package org.vaadin.pagingcomponent.customizer.element.impl;

import org.vaadin.pagingcomponent.button.ButtonPageNavigator;
import org.vaadin.pagingcomponent.constant.CaptionConstants;
import org.vaadin.pagingcomponent.constant.StyleConstants;
import org.vaadin.pagingcomponent.customizer.element.ElementsCustomizer;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.BaseTheme;

/**
 * Allow to style the buttons and separators by CSS.<br><br>
 * The components have the following style name:
 * <ul>
 * 	<li>button first: button-first</li>
 * 	<li>button previous: button-previous</li>
 * 	<li>buttons page: button-page</li>
 * 	<li>button next: button-next</li>
 * 	<li>button last: button-last</li>
 *  <li>first separator: separator-first</li>
 *  <li>second separator: separator-last</li>
 * </ul>
 */
public class CssElementsCustomizer implements ElementsCustomizer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3016077970044057717L;

	@Override
	public Button createButtonFirst() {
		Button button = new Button(CaptionConstants.CAPTION_BUTTON_FIRST);
		button.setStyleName(BaseTheme.BUTTON_LINK);
		button.addStyleName(StyleConstants.STYLE_BUTTON_FIRST);
		return button;
	}

	@Override
	public Button createButtonLast() {
		Button button = new Button(CaptionConstants.CAPTION_BUTTON_LAST);
		button.setStyleName(BaseTheme.BUTTON_LINK);
		button.addStyleName(StyleConstants.STYLE_BUTTON_LAST);
		return button;
	}

	@Override
	public Button createButtonPrevious() {
		Button button = new Button(CaptionConstants.CAPTION_BUTTON_PREVIOUS);
		button.setStyleName(BaseTheme.BUTTON_LINK);
		button.addStyleName(StyleConstants.STYLE_BUTTON_PREVIOUS);
		return button;
	}

	@Override
	public Button createButtonNext() {
		Button button = new Button(CaptionConstants.CAPTION_BUTTON_NEXT);
		button.setStyleName(BaseTheme.BUTTON_LINK);
		button.addStyleName(StyleConstants.STYLE_BUTTON_NEXT);
		return button;
	}

	@Override
	public ButtonPageNavigator createButtonPage() {
		ButtonPageNavigator button = new ButtonPageNavigator();
		button.setStyleName(BaseTheme.BUTTON_LINK);
		button.addStyleName(StyleConstants.STYLE_BUTTON_PAGE);
		return button;
	}

	@Override
	public Component createFirstSeparator() {
		AbstractComponent separator = new Label(CaptionConstants.CAPTION_SEPARATOR_FIRST);
		separator.setStyleName(StyleConstants.STYLE_SEPARATOR_FIRST);
		return separator;
	}

	@Override
	public Component createLastSeparator() {
		AbstractComponent separator = new Label(CaptionConstants.CAPTION_SEPARATOR_LAST);
		separator.setStyleName(StyleConstants.STYLE_SEPARATOR_LAST);
		return separator;
	}

}
