package org.vaadin.pagingcomponent.customizer.style.impl;

import org.vaadin.pagingcomponent.ComponentsManager;
import org.vaadin.pagingcomponent.builder.ElementsBuilder;
import org.vaadin.pagingcomponent.button.ButtonPageNavigator;
import org.vaadin.pagingcomponent.constant.StyleConstants;
import org.vaadin.pagingcomponent.customizer.style.StyleCustomizer;
import org.vaadin.pagingcomponent.utilities.Utils;

/**
 * Set the appropriate style name to the buttons and separators according to the selected page thus these ones can be styled by CSS.<br><br>
 * 
 * When a limit is reached, the relevant components have the CSS class "limit". For example, if the user click on the last page, the button last, next and the last separator will have the style "limit".<br><br>
 * Finally, the style "current" apply at the button page that has the page number selected by the user.
 *
 */
public class CssStyleCustomizer implements StyleCustomizer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4843587030028946487L;

	@Override
	public void styleButtonPageNormal(ButtonPageNavigator button, int pageNumber) {
		button.setPage(pageNumber);
		button.removeStyleName(StyleConstants.STYLE_CURRENT);
	}

	@Override
	public void styleButtonPageCurrentPage(ButtonPageNavigator button, int pageNumber) {
		button.setPage(pageNumber);
		button.focus();
		button.addStyleName(StyleConstants.STYLE_CURRENT);
	}
	
	@Override
	public void styleTheOthersElements(ComponentsManager manager, ElementsBuilder builder) {
		if (manager.isFirstPage()) {
			Utils.addStyleName(builder.getButtonFirst(), StyleConstants.STYLE_LIMIT);
			Utils.addStyleName(builder.getButtonPrevious(), StyleConstants.STYLE_LIMIT);
			Utils.addStyleName(builder.getFirstSeparator(), StyleConstants.STYLE_LIMIT);
		} else {
			Utils.removeStyleName(builder.getButtonFirst(), StyleConstants.STYLE_LIMIT);
			Utils.removeStyleName(builder.getButtonPrevious(), StyleConstants.STYLE_LIMIT);
			Utils.removeStyleName(builder.getFirstSeparator(), StyleConstants.STYLE_LIMIT);
		}
		
		if (manager.isLastPage()) {
			Utils.addStyleName(builder.getButtonLast(), StyleConstants.STYLE_LIMIT);
			Utils.addStyleName(builder.getButtonNext(), StyleConstants.STYLE_LIMIT);
			Utils.addStyleName(builder.getLastSeparator(), StyleConstants.STYLE_LIMIT);
		} else {
			Utils.removeStyleName(builder.getButtonLast(), StyleConstants.STYLE_LIMIT);
			Utils.removeStyleName(builder.getButtonNext(), StyleConstants.STYLE_LIMIT);
			Utils.removeStyleName(builder.getLastSeparator(), StyleConstants.STYLE_LIMIT);
		}
	}
	
}
