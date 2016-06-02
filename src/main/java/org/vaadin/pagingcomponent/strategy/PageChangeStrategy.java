package org.vaadin.pagingcomponent.strategy;


import java.io.Serializable;
import java.util.List;

import org.vaadin.pagingcomponent.ComponentsManager;
import org.vaadin.pagingcomponent.Page;
import org.vaadin.pagingcomponent.builder.ElementsBuilder;
import org.vaadin.pagingcomponent.button.ButtonPageNavigator;
import org.vaadin.pagingcomponent.customizer.style.StyleCustomizer;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

/**
 * Define a strategy to find the page number that the buttons page have to display and to center the button page containing the current page number if it's possible.
 * It allow also to choose and set the styles to the buttons and the separators thanks to {@link StyleCustomizer}.
 */
public abstract class PageChangeStrategy implements Serializable {

	private static final long serialVersionUID = 8421311407339650393L;
	protected int buttonPageMargin;
	protected ComponentsManager manager;

	public PageChangeStrategy(ComponentsManager manager) {
		this.manager = manager;
		buttonPageMargin = manager.getNumberOfButtonsPage() >> 1; // divide by 2
	}
	
	public boolean go(int pageNumber) {
		if (manager.getCurrentPage()==pageNumber) {
			return false;
        }
		
		manager.setCurrentPage(pageNumber);
    	refreshStyleOfAllComponents();
    	return true;
	}
	
	public boolean go(Page page) {
		switch(page) {
			case FIRST:
				return go(1);
			case PREVIOUS:
				return go(manager.getCurrentPage() != 1 ? manager.getCurrentPage() - 1 : manager.getNumberTotalOfPages());
			case NEXT:
				return go(manager.getCurrentPage() != manager.getNumberTotalOfPages() ? manager.getCurrentPage() + 1 : 1);
			case LAST:
				return go(manager.getNumberTotalOfPages());
			default:
				throw new UnsupportedOperationException("This page value [" + page.name() + "] can't be processed.");
		}
	}
	
	/**
     * Reorganizes the caption of buttons each time we change a page. 
     */
	public void reorganizeButtons(ClickEvent event) {
        if (event == null) {
			refreshStyleOfAllComponents();
			return;
		}
		
		Button buttonPressed=event.getButton();
		ElementsBuilder builder = manager.getElementsBuilder();

        if (buttonPressed==builder.getButtonFirst()){
        	go(Page.FIRST);
        } else if (buttonPressed==builder.getButtonLast()){
        	go(Page.LAST);
        } else if (buttonPressed==builder.getButtonPrevious()){
        	go(Page.PREVIOUS);
        } else if (buttonPressed==builder.getButtonNext()){
        	go(Page.NEXT);
        } else { // Management of buttons page
            go(((ButtonPageNavigator) buttonPressed).getPage());
        }
    }
	
	/**
	 * Decide which of the methods of {@link StyleCustomizer} must be called on the button given in parameter according that this one display the current page number or not.
	 * 
	 * @param styleCustomizer 
	 * @param button the button to style
	 * @param currentPageNumber the page number selected by the user.
	 * @param pageNumberForTheButton the page number that the button should display.
	 */
	public void chooseStyle(StyleCustomizer styleCustomizer, ButtonPageNavigator button, int currentPageNumber, int pageNumberForTheButton) {
		if (currentPageNumber == pageNumberForTheButton) {
			styleCustomizer.styleButtonPageCurrentPage(button, pageNumberForTheButton);
		} else {
			styleCustomizer.styleButtonPageNormal(button, pageNumberForTheButton);
		}
	}
	
	/**
	 * Set style of others buttons (first, previous, next, last).
	 */
	protected void refreshStyleOfAllComponents() {
		ElementsBuilder builder = manager.getElementsBuilder();
		StyleCustomizer styleCustomizer = manager.getButtonsStyleCustomizer();
		styleCustomizer.styleTheOthersElements(manager, builder);
		setStyleAndPageNumberToButtonsPage(builder, styleCustomizer);
	}
	
	/**
	 * Calculate the page number and set the styles for each buttons page.
	 */
	protected void setStyleAndPageNumberToButtonsPage(ElementsBuilder builder, StyleCustomizer styleCustomizer) {
		int currentPage = manager.getCurrentPage();
		int numberOfButtonsPage = manager.getNumberOfButtonsPage();
		int numberTotalOfPages = manager.getNumberTotalOfPages();
		List<ButtonPageNavigator> listButtons = builder.getListButtons();
		
		if (manager.isBeingInitialized()) { // if the PagagingComponent is being initialized. We need also to initialize the style of all buttons page.
			for (int i = 0, limit = listButtons.size(); i < limit; i++) {
				chooseStyle(styleCustomizer, listButtons.get(i), currentPage, i + 1);
			}
		} else if (numberTotalOfPages > numberOfButtonsPage) { // if we have more pages of results then the number of buttons -> we need to reorganize every button
			int pageNumberWhereStartTheIteration;
			if (currentPage <= buttonPageMargin) { // current page is in the lower margin
				pageNumberWhereStartTheIteration = 1;
			} else if (currentPage >= numberTotalOfPages - buttonPageMargin) { // current page is in the higher margin
				pageNumberWhereStartTheIteration = numberTotalOfPages - numberOfButtonsPage + 1;
			} else { // current page is not in the margins and we must scroll the number of the button page
				pageNumberWhereStartTheIteration = calculatePageNumberWhereStartTheIteration(currentPage, buttonPageMargin);
			}

			for (int i = 0; i < numberOfButtonsPage; pageNumberWhereStartTheIteration++, i++) {
				chooseStyle(styleCustomizer, listButtons.get(i), currentPage, pageNumberWhereStartTheIteration);
			}
		} else { // if we have less pages of results then the number of buttons -> we only need to highlight the button of the current page and remove highlighting at the button of the previous page
			int previousPage = manager.getPreviousPage();
			styleCustomizer.styleButtonPageNormal(listButtons.get(previousPage - 1), previousPage);
			styleCustomizer.styleButtonPageCurrentPage(listButtons.get(currentPage - 1), currentPage);
		}
	}
	
	protected abstract int calculatePageNumberWhereStartTheIteration(int currentPage, int buttonPageMargin);
	
}
