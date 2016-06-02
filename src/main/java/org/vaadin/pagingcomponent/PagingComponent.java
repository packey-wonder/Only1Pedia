package org.vaadin.pagingcomponent;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.vaadin.pagingcomponent.builder.ElementsBuilder;
import org.vaadin.pagingcomponent.button.ButtonPageNavigator;
import org.vaadin.pagingcomponent.customizer.DefaultCustomizerFactory;
import org.vaadin.pagingcomponent.customizer.adaptator.GlobalCustomizer;
import org.vaadin.pagingcomponent.customizer.element.ElementsCustomizer;
import org.vaadin.pagingcomponent.customizer.element.impl.BackwardCompatibilityElemensCustomizer;
import org.vaadin.pagingcomponent.customizer.element.impl.CssElementsCustomizer;
import org.vaadin.pagingcomponent.customizer.style.StyleCustomizer;
import org.vaadin.pagingcomponent.customizer.style.impl.BackwardCompatibilityStyleCustomizer;
import org.vaadin.pagingcomponent.customizer.style.impl.CssStyleCustomizer;
import org.vaadin.pagingcomponent.utilities.FakeList;
import org.vaadin.pagingcomponent.utilities.Utils;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

/**
 * This class creates a layout to navigate in different pages of results, the layout contains a button "First", "Last", "Previous", "Next" 
 * and as many buttons (called button pages) as the variable "numberOfButtonsPage".
 * Each button has a caption as page number which is updated when navigating.
 */
public class PagingComponent<E> extends HorizontalLayout implements Button.ClickListener {

	private static final long serialVersionUID = -8403350410833192087L;
	protected ComponentsManager manager;
	protected List<E> itemsList;  // List with all items.

	// Necessary for the listener  (code copy/pasted from Vaadin's ButtonClickListener, looks heavy but necessary for makeing it work with anonymous Listeners)
	public static final Method METHOD_DISPLAY_PAGE;
	static {
		try {
			METHOD_DISPLAY_PAGE = PagingComponentListener.class.getDeclaredMethod("displayPage", new Class[] { ChangePageEvent.class });
		} catch (NoSuchMethodException e) {
			// This should never happen
			throw new RuntimeException("Internal error finding methods in PagingComponent", e);
		}
	}

	/**
	 * @param numberOfItemsPerPage number of items to display in one page
	 * @param itemsCollection the items to paginate
	 * @param pagingComponentListener your {@link PagingComponentListener} that allow you to add the items to display into your layout
	 * @deprecated Use {@link #paginate(Collection)} and the builder create by it.
	 */
	@Deprecated
	public PagingComponent(int numberOfItemsPerPage, Collection<E> itemsCollection, PagingComponentListener<E> pagingComponentListener) {
		this(numberOfItemsPerPage, 9, itemsCollection, new BackwardCompatibilityElemensCustomizer(), null, pagingComponentListener);
	} 

	/**
	 * @param numberOfItemsPerPage number of items to display in one page
	 * @param numberOfButtonsPage maximum number of buttons displayed to navigate between pages
	 * @param itemsCollection the items to paginate
	 * @param pagingComponentListener your {@link PagingComponentListener} that allow you to add the items to display into your layout
	 * @deprecated Use {@link #paginate(Collection)} and the builder create by it.
	 */
	@Deprecated
	public PagingComponent(int numberOfItemsPerPage, int numberOfButtonsPage, Collection<E> itemsCollection, PagingComponentListener<E> pagingComponentListener) {
		this(numberOfItemsPerPage, numberOfButtonsPage, itemsCollection, new BackwardCompatibilityElemensCustomizer(), null, pagingComponentListener);
	}
	
	/**
	 * 
	 * @param numberOfItemsPerPage number of items to display in one page
	 * @param numberOfButtonsPage maximum number of buttons displayed to navigate between pages
	 * @param itemsCollection the items to paginate
	 * @param elementsCustomizer the {@link ElementsCustomizer} to personalize the creation of the different buttons and separators
	 * @param pagingComponentListener your {@link PagingComponentListener} that allow you to add the items to display into your layout
	 * @deprecated Use {@link #paginate(Collection)} and the builder create by it.
	 */
	@Deprecated
	public PagingComponent(int numberOfItemsPerPage, int numberOfButtonsPage, Collection<E> itemsCollection, ElementsCustomizer elementsCustomizer, PagingComponentListener<E> pagingComponentListener) {
		this(numberOfItemsPerPage, numberOfButtonsPage, itemsCollection, elementsCustomizer, DefaultCustomizerFactory.cssStyle(), pagingComponentListener);
	}
	
	/**
	 * 
	 * @param numberOfItemsPerPage number of items to display in one page
	 * @param numberOfButtonsPage maximum number of buttons displayed to navigate between pages
	 * @param itemsCollection the items to paginate
	 * @param styleCustomizer the {@link StyleCustomizer} to personalize the style of the different buttons and separators
	 * @param pagingComponentListener your {@link PagingComponentListener} that allow you to add the items to display into your layout
	 * @deprecated Use {@link #paginate(Collection)} and the builder create by it.
	 */
	@Deprecated
	public PagingComponent(int numberOfItemsPerPage, int numberOfButtonsPage, Collection<E> itemsCollection, StyleCustomizer styleCustomizer, PagingComponentListener<E> pagingComponentListener) {
		this(numberOfItemsPerPage, numberOfButtonsPage, itemsCollection, DefaultCustomizerFactory.cssElements(), styleCustomizer, pagingComponentListener);
	}
	
	/**
	 * 
	 * @param numberOfItemsPerPage number of items to display in one page
	 * @param numberOfButtonsPage maximum number of buttons displayed to navigate between pages
	 * @param itemsCollection the items to paginate
	 * @param customizer the {@link GlobalCustomizer} to personalize the creation and the style of the different buttons and separators
	 * @param pagingComponentListener your {@link PagingComponentListener} that allow you to add the items to display into your layout
	 * @deprecated Use {@link #paginate(Collection)} and the builder create by it.
	 */
	@Deprecated
	public PagingComponent(int numberOfItemsPerPage, int numberOfButtonsPage, Collection<E> itemsCollection, GlobalCustomizer customizer, PagingComponentListener<E> pagingComponentListener) {
		this(numberOfItemsPerPage, numberOfButtonsPage, itemsCollection, customizer, customizer, pagingComponentListener);
	}

	/**
	 * 
	 * @param numberOfItemsPerPage number of items to display in one page
	 * @param numberOfButtonsPage maximum number of buttons displayed to navigate between pages
	 * @param itemsCollection the items to paginate
	 * @param elementsCustomizer the {@link ElementsCustomizer} to personalize the creation of the different buttons and separators
	 * @param styleCustomizer the {@link StyleCustomizer} to personalize the style of the different buttons and separators
	 * @param pagingComponentListener your {@link PagingComponentListener} that allow you to add the items to display into your layout
	 * @deprecated Use {@link #paginate(Collection)} and the builder create by it.
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	protected PagingComponent(int numberOfItemsPerPage, int numberOfButtonsPage, Collection<E> itemsCollection, ElementsCustomizer elementsCustomizer, StyleCustomizer styleCustomizer, PagingComponentListener<E> pagingComponentListener) {
		this(PageNumberProviderFactory.firstPage(), numberOfItemsPerPage, numberOfButtonsPage, itemsCollection, elementsCustomizer, styleCustomizer, Arrays.asList(pagingComponentListener));
	}
	
	/**
	 * Allow to provide the backward compatibility between the old constructors and the new builder.
	 */
	@Deprecated
	protected PagingComponent(PageNumberProvider pageNumberProvider, int numberOfItemsPerPage, int numberOfButtonsPage, Collection<E> itemsCollection, ElementsCustomizer elementsCustomizer, StyleCustomizer styleCustomizer, List<PagingComponentListener<E>> listeners) {
		manager = createComponentsManager(pageNumberProvider, numberOfItemsPerPage, numberOfButtonsPage, itemsCollection, elementsCustomizer, styleCustomizer);
		
		itemsList = createItemsList(itemsCollection);
		
		addComponents();
		
		for (PagingComponentListener<E> listener : listeners) {
			addListener(listener);
		}

		//throw event for PagingComponentListener and update for the first time RangeDisplayer
		runChangePageEvent(null);
	}
	
	/**
	 * Create a new {@link PagingComponent} thanks to the builder.
	 * 
	 * @param builder
	 */
	protected PagingComponent(Builder<E> builder) {
		this(builder.pageNumberProvider, builder.numberOfItemsPerPage, builder.numberOfButtonsPage, builder.itemsCollection, builder.elementsCustomizer, builder.styleCustomizer, builder.listeners);
	}
	
	/**
	 * @param itemsCollection the collection to paginate.
	 * @return a builder to create the {@link PagingComponent} more easily.
	 */
	public static <E> Builder<E> paginate(Collection<E> itemsCollection) {
		return new Builder<E>(itemsCollection);
	}

	@Override
	public void buttonClick(ClickEvent event) {
		runChangePageEvent(event);
	}

	public void addListener(PagingComponentListener<E> listener){
		addListener(ChangePageEvent.class, listener, METHOD_DISPLAY_PAGE);
	}

	public void removeListener(PagingComponentListener<E> listener){
		removeListener(ChangePageEvent.class, listener, METHOD_DISPLAY_PAGE);
	}

	/**
	 * @param numberOfButtonsPage maximum number of buttons displayed to navigate between pages
	 */
	public void setNumberOfButtonsPage(int numberOfButtonsPage){
		manager.setNumberOfButtonsPage(numberOfButtonsPage);
		runChangePageEvent(null);
		addComponents();
	}
	
	/**
	 * Display the given page.
	 * @param pageNumber the page number to display
	 * @throws IllegalArgumentException if the pageNumber is smaller than 1
	 * @throws IndexOutOfBoundsException if the pageNumber is not include in the page range
	 */
	public void go(int pageNumber){
		if (manager.getCurrentPage()==pageNumber) {
			return;
		}
		
		if (pageNumber < 1) {
			throw new IllegalArgumentException("The page number cannot be smaller than 1. Your value: " + pageNumber);
		} else if (pageNumber > manager.getNumberTotalOfPages()) {
			throw new IndexOutOfBoundsException(String.format("The pagingComponent have only %s pages and you want go on the page %s", manager.getNumberTotalOfPages(), pageNumber));
		}
		
		if (manager.getPageChangeStrategy().go(pageNumber)) {
			fireEvent(new ChangePageEvent<E>(this, getPageRange()));
		}
	}
	
	/**
	 * Display the given page.
	 * @param page which page to display
	 */
	public void go(Page page){
		if (manager.getPageChangeStrategy().go(page)) {
			fireEvent(new ChangePageEvent<E>(this, getPageRange()));
		}
	}

	public PageRange<E> getPageRange(){
		return new PageRange<E>(manager.getCurrentPage(), manager.getNumberOfItemsPerPage(), itemsList);
	}

	/**
	 * If you want to get something, it is through this object that you will get it.
	 * 
	 * @return the {@link ComponentsManager} of this {@link PagingComponent}
	 */
	public ComponentsManager getComponentsManager() {
		return manager;
	}

	/**
	 * Deprecated: use {@link ElementsBuilder#getButtonsPage()} that you can get by the {@link ComponentsManager}.
	 *
	 * @return list of all buttons page that allow to go to the page they indicate.
	 */
	@Deprecated
	public List<ButtonPageNavigator> getButtonsPage() {
		return manager.getElementsBuilder().getListButtons();
	}

	/**
	 * Deprecated: use {@link ElementsBuilder#getButtonPrevious()} that you can get by the {@link ComponentsManager}.
	 *
	 * @return the button that allow to go to the previous page.
	 */
	@Deprecated
	public Button getButtonPrevious() {
		return manager.getElementsBuilder().getButtonPrevious();
	}

	/**
	 * Deprecated: use {@link ElementsBuilder#getButtonNext()} that you can get by the {@link ComponentsManager}.
	 *
	 * @return the button that allow to go to the next page.
	 */
	@Deprecated
	public Button getButtonNext() {
		return manager.getElementsBuilder().getButtonNext();
	}

	/**
	 * Deprecated: use {@link ElementsBuilder#getButtonFirst()} that you can get by the {@link ComponentsManager}.
	 * 
	 * @return the button that allow to go to the first page.
	 */
	@Deprecated
	public Button getButtonFirst() {
		return manager.getElementsBuilder().getButtonFirst();
	}

	/**
	 * Deprecated: use {@link ElementsBuilder#getButtonLast()} that you can get by the {@link ComponentsManager}.
	 * 
	 * @return the button that allow to go to the last page.
	 */
	@Deprecated
	public Button getButtonLast() {
		return manager.getElementsBuilder().getButtonLast();
	}
	
	protected void runChangePageEvent(ClickEvent event) {
		manager.getPageChangeStrategy().reorganizeButtons(event);
		// TODO for next release: this event should be processed only when the the desired page is different than the current page.
		fireEvent(new ChangePageEvent<E>(this, getPageRange()));
	}
	
	/**
	 * Override this method if you want that the {@link PagingComponent} use a particular {@link ComponentsManager}.
	 * 
	 * @param pageNumberProvider contains the initial page number.
	 * @param numberOfItemsPerPage number of items to display in one page
	 * @param numberOfButtonsPage maximum number of buttons displayed to navigate between pages
	 * @param itemsCollection the items to paginate
	 * @param elementsCustomizer the {@link ElementsCustomizer} to personalize the creation of the different buttons and separators
	 * @param styleCustomizer the {@link StyleCustomizer} to personalize the style of the different buttons and separators
	 * @return a {@link ComponentsManager}
	 */
	protected ComponentsManager createComponentsManager(PageNumberProvider pageNumberProvider, int numberOfItemsPerPage, int numberOfButtonsPage, Collection<E> itemsCollection, ElementsCustomizer elementsCustomizer, StyleCustomizer styleCustomizer) {
		return new ComponentsManager(pageNumberProvider, numberOfItemsPerPage, numberOfButtonsPage, itemsCollection.size(), elementsCustomizer, styleCustomizer, this);
	}
	
	/**
	 * Override this method if you want use a special collection like Apache Commons Collections.
	 * 
	 * @param collection the collection given in parameter to the {@link PagingComponent} 
	 * @return a list of items to paginate
	 */
	protected List<E> createItemsList(Collection<E> collection) {
		return collection instanceof FakeList ? (FakeList<E>) collection : new ArrayList<E>(collection);
	}

	protected void addComponents() {
		setSpacing(true);
		removeAllComponents();
		ElementsBuilder builder = manager.getElementsBuilder();
		Utils.addComponent(this, builder.getButtonFirst());
		Utils.addComponent(this, builder.getButtonPrevious());
		Utils.addComponent(this, builder.getFirstSeparator());
		for (Button button : builder.getListButtons()) {
			addComponent(button);
		}
		Utils.addComponent(this, builder.getLastSeparator());
		Utils.addComponent(this, builder.getButtonNext());
		Utils.addComponent(this, builder.getButtonLast());
	}
	
	/////////////////////////////**these methods are used to control the style of buttons**
	/////////////////////////////**these methods are used to control the style of buttons**
	/////////////////////////////**these methods are used to control the style of buttons**
	/////////////////////////////**these methods are used to control the style of buttons**
	/////////////////////////////**these methods are used to control the style of buttons**
	/////////////////////////////**these methods are used to control the style of buttons**
	/////////////////////////////**these methods are used to control the style of buttons**

	/**
	 * Sets the same style to all buttons. First, Previous, 1, 2 ... Next, Last<br><br>
	 * 
	 * NOTE : Don't use it if you have created the <code>PagingComponent</code> with a "<code>Customizer</code>" in parameter because this one does this job.
	 */
	@Deprecated
	public void setStyleNameForAllButtons(String style) {
		getBackwardCompatibilityStyleCustomizer().setStyleNameForAllButtons(style);
	}

	/**
	 * Sets the style to buttons Previous and Next<br><br>
	 * 
	 * NOTE : Don't use it if you have created the <code>PagingComponent</code> with a "<code>Customizer</code>" in parameter because this one does this job.
	 */
	@Deprecated
	public void setStyleNameButtonsPreviousAndNext(String style) {
		getBackwardCompatibilityStyleCustomizer().setStyleNameButtonsPreviousAndNext(style);
	}

	/**
	 * Sets the style to buttons First and Last<br><br>
	 * 
	 * NOTE : Don't use it if you have created the <code>PagingComponent</code> with a "<code>Customizer</code>" in parameter because this one does this job.
	 */
	@Deprecated
	public void setStyleNameButtonsFirstAndLast(String style) {
		getBackwardCompatibilityStyleCustomizer().setStyleNameButtonsFirstAndLast(style);
	}

	/**
	 * Sets the style to a button of current page<br><br>
	 * 
	 * NOTE : Don't use it if you have created the <code>PagingComponent</code> with a "<code>Customizer</code>" in parameter because this one does this job.
	 */
	@Deprecated
	public void setStyleNameCurrentButtonState(String style) {
		getBackwardCompatibilityStyleCustomizer().setStyleNameCurrentButtonState(style);
	}

	/**
	 * Sets the style to the buttons that don't correspond to the current page<br><br>
	 * 
	 * NOTE : Don't use it if you have created the <code>PagingComponent</code> with a "<code>Customizer</code>" in parameter because this one does this job.
	 */
	@Deprecated
	public void setStyleNameNormalButtonsState(String style) {
		getBackwardCompatibilityStyleCustomizer().setStyleNameNormalButtonsState(style);
	}

	/**
	 * Adds an existing style to all buttons<br><br>
	 * 
	 * NOTE : Don't use it if you have created the <code>PagingComponent</code> with a "<code>Customizer</code>" in parameter because this one does this job.
	 */
	@Deprecated
	public void addStyleNameForAllButtons(String style) {
		getBackwardCompatibilityStyleCustomizer().addStyleNameForAllButtons(style);
	}

	/**
	 * Adds an existing style to buttons Previous and Next<br><br>
	 * 
	 * NOTE : Don't use it if you have created the <code>PagingComponent</code> with a "<code>Customizer</code>" in parameter because this one does this job.
	 */
	@Deprecated
	public void addStyleNameButtonsPreviousAndNext(String style) {
		getBackwardCompatibilityStyleCustomizer().addStyleNameButtonsPreviousAndNext(style);
	}

	/**
	 * Adds an existing style to buttons First and Last<br><br>
	 * 
	 * NOTE : Don't use it if you have created the <code>PagingComponent</code> with a "<code>Customizer</code>" in parameter because this one does this job.
	 */
	@Deprecated
	public void addStyleNameButtonsFirstAndLast(String style) {
		getBackwardCompatibilityStyleCustomizer().addStyleNameButtonsFirstAndLast(style);
	}

	/**
	 * Adds an existing style to a button of current page<br><br>
	 * 
	 * NOTE : Don't use it if you have created the <code>PagingComponent</code> with a "<code>Customizer</code>" in parameter because this one does this job.
	 */
	@Deprecated
	public void addStyleNameCurrentButtonState(String style) {
		getBackwardCompatibilityStyleCustomizer().addStyleNameCurrentButtonState(style);
	}

	/**
	 * Adds an existing style to the buttons that don't correspond to the current page<br><br>
	 * 
	 * NOTE : Don't use it if you have created the <code>PagingComponent</code> with a "<code>Customizer</code>" in parameter because this one does this job.
	 */
	@Deprecated
	public void addStyleNameNormalButtonsState(String style) {
		getBackwardCompatibilityStyleCustomizer().addStyleNameNormalButtonsState(style);
	}
	
	/**
	 * @return {@link BackwardCompatibilityStyleCustomizer}
	 * @throws RuntimeException it is thrown when a {@link BackwardCompatibilityStyleCustomizer} isn't found
	 */
	protected BackwardCompatibilityStyleCustomizer getBackwardCompatibilityStyleCustomizer() throws RuntimeException {
		StyleCustomizer styleCustomizer = manager.getButtonsStyleCustomizer();
		
		if (!(styleCustomizer instanceof BackwardCompatibilityStyleCustomizer)) {
			throw new RuntimeException("Error in PagingComponent:\n" +
					"You can't use the style methods of the PagingComponent when this one is created with a \"Customizer\" in parameter.\n" +
					"If you want customize together the creation and the style of buttons, give a GlobalCustomizer as parameter to the PagingComponent constructor.\n");
		}
		
		return (BackwardCompatibilityStyleCustomizer) styleCustomizer;
	}

	////////////////////////////////////////////// INNER CLASS /////////////////////////////////////
	////////////////////////////////////////////// INNER CLASS /////////////////////////////////////
	////////////////////////////////////////////// INNER CLASS /////////////////////////////////////
	////////////////////////////////////////////// INNER CLASS /////////////////////////////////////
	////////////////////////////////////////////// INNER CLASS /////////////////////////////////////
	////////////////////////////////////////////// INNER CLASS /////////////////////////////////////
	////////////////////////////////////////////// INNER CLASS /////////////////////////////////////

	/**
	 * Retrieves the list of items which will be displayed and the index range.
	 */
	public static class PageRange<I> implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 8376551162542298825L;
		protected int indexPageStart, indexPageEnd;
		protected List<I> itemsList;

		public PageRange(int currentPage, int itemsPerPage, List<I> itemsList) {
			indexPageEnd = currentPage * itemsPerPage;
			indexPageStart = indexPageEnd - itemsPerPage;
			if (indexPageEnd > itemsList.size()){
				indexPageEnd = itemsList.size();
			}
			this.itemsList = itemsList.subList(indexPageStart, indexPageEnd);
		}

		/**
		 * Gets items which will be displayed (in the current page)
		 */
		public List<I> getItemsList() {
			return itemsList;
		}

		public int getIndexPageStart() {
			return indexPageStart;
		}

		public int getIndexPageEnd() {
			return indexPageEnd;
		}
	}

	/** 
	 * Listens when changing a page. It allow to retrieve the items to display and add these into the layout of your choice.
	 */
	public static interface PagingComponentListener<I> extends Serializable {
		
		/**
		 * Allow to retrieve the items to display by the event and add these into the layout of your choice.
		 * 
		 * @param event
		 */
		public void displayPage(ChangePageEvent<I> event);
	}

	/** 
	 * It's fired when changing a page.
	 */
	public static class ChangePageEvent<I> extends Event{
		/**
		 * 
		 */
		private static final long serialVersionUID = -1488862492808637211L;
		protected PageRange<I> pageRange;

		public ChangePageEvent(Component source, PageRange<I> pageRange) {
			super(source);
			this.pageRange=pageRange;
		}

		/**
		 * @return a {@link PageRange} which contains the items to display and the index range
		 */
		public PageRange<I> getPageRange(){
			return pageRange;
		}
	}
	
	/**
	 * Allow to create a new {@link PagingComponent} more easily than the old PagingComponent constructors.
	 *
	 * @param <E> the type of the paginated items.
	 */
	public static class Builder<E> {
		
		protected PageNumberProvider pageNumberProvider;
		protected int numberOfItemsPerPage;
		protected int numberOfButtonsPage;
		protected Collection<E> itemsCollection;
		protected ElementsCustomizer elementsCustomizer;
		protected StyleCustomizer styleCustomizer;
		protected List<PagingComponentListener<E>> listeners;
		
		public Builder(Collection<E> itemsCollection) {
			this.pageNumberProvider = PageNumberProviderFactory.firstPage();
			this.numberOfItemsPerPage = 10;
			this.numberOfButtonsPage = 9;
			this.itemsCollection = itemsCollection;
			this.elementsCustomizer = DefaultCustomizerFactory.cssElements();
			this.styleCustomizer = DefaultCustomizerFactory.cssStyle();
			this.listeners = new ArrayList<PagingComponentListener<E>>();
		}
		
		/**
		 * Specifies the initial page displayed by the {@link PagingComponent}.<br>
		 * If the given page is same than {@link Page#LAST}, the last page will be displayed otherwise it will be always the first page.
		 * 
		 * @param page the initial page of the {@link PagingComponent}. Default value is the first page. 
		 * @return the builder
		 */
		public Builder<E> page(Page page) {
			if (page == null) {
				throw new IllegalArgumentException("The page parameter can't be null");
			}
			
			this.pageNumberProvider = PageNumberProviderFactory.page(page);
			return this;
		}
		
		/**
		 * Specifies the initial page number displayed by the {@link PagingComponent}.<br>
		 * The pageNumber will be equal to the total number of pages if it is greater than this last one.
		 * 
		 * @param pageNumber it must be greater than zero. Default value is <code>1</code>.
		 * @return the builder
		 */
		public Builder<E> pageNumber(int pageNumber) {
			if (pageNumber < 1 ) {
				throw new IllegalArgumentException("The pageNumber parameter must be greater than zero. Your value is: " + pageNumber);
			}
			
			this.pageNumberProvider = PageNumberProviderFactory.pageNumber(pageNumber);
			return this;
		}
		
		/**
		 * Specifies the number of items displayed in a page.
		 * 
		 * @param numberOfItemsPerPage it must be greater than zero. Default value is <code>10</code>.
		 * @return the builder
		 */
		public Builder<E> numberOfItemsPerPage(int numberOfItemsPerPage) {
			if (numberOfItemsPerPage < 1 ) {
				throw new IllegalArgumentException("The numberOfItemsPerPage parameter must be greater than zero. Your value is: " + numberOfItemsPerPage);
			}
			
			this.numberOfItemsPerPage = numberOfItemsPerPage;
			return this;
		}
		
		/**
		 * Specifies the maximum number of buttons to navigate between the different pages.
		 * 
		 * @param numberOfButtonsPage it must be greater than zero. Default value is Default value is <code>9</code>.
		 * @return the builder
		 */
		public Builder<E> numberOfButtonsPage(int numberOfButtonsPage) {
			if (numberOfButtonsPage < 1 ) {
				throw new IllegalArgumentException("The numberOfButtonsPage parameter must be greater than zero. Your value is: " + numberOfButtonsPage);
			}
			
			this.numberOfButtonsPage = numberOfButtonsPage;
			return this;
		}
		
		/**
		 * Allow to create the elements (Buttons, separators, ...) that compose the {@link PagingComponent}.
		 * 
		 * @param elementsCustomizer Default value is {@link CssElementsCustomizer}.
		 * @return the builder
		 */
		public Builder<E> elementsCustomizer(ElementsCustomizer elementsCustomizer) {
			if (elementsCustomizer == null) {
				throw new IllegalArgumentException("The elementsCustomizer parameter can't be null");
			}
			
			this.elementsCustomizer = elementsCustomizer;
			return this;
		}
		
		/**
		 * Allow to style the elements (Buttons, separators, ...) of the {@link PagingComponent} after a page change.
		 * 
		 * @param styleCustomizer Default value is {@link CssStyleCustomizer}.
		 * @return the builder
		 */
		public Builder<E> styleCustomizer(StyleCustomizer styleCustomizer) {
			if (styleCustomizer == null) {
				throw new IllegalArgumentException("The styleCustomizer parameter can't be null");
			}
			
			this.styleCustomizer = styleCustomizer;
			return this;
		}
		
		/**
		 * Allow to create and style the elements (Buttons, separators, ...) of the {@link PagingComponent}.
		 * 
		 * @param globalCustomizer Default value has same behavior than the {@link CssElementsCustomizer} and {@link CssStyleCustomizer}.
		 * @return the builder
		 */
		public Builder<E> globalCustomizer(GlobalCustomizer globalCustomizer) {
			if (globalCustomizer == null) {
				throw new IllegalArgumentException("The globalCustomizer parameter can't be null");
			}
			
			this.elementsCustomizer = globalCustomizer;
			this.styleCustomizer = globalCustomizer;
			return this;
		}
		
		/**
		 * Add a {@link PagingComponentListener}. This one allow to do some stuff when a page change event is fired (see {@link PagingComponent.ChangePageEvent}).
		 * 
		 * @param listener your own listener.
		 * @return the builder
		 */
		public Builder<E> addListener(PagingComponentListener<E> listener) {
			if (listener == null) {
				throw new IllegalArgumentException("The listener parameter can't be null");
			}
			
			this.listeners.add(listener);
			return this;
		}
		
		/**
		 * Build a new {@link PagingComponent}.
		 * 
		 * @return the created PagingComponent.
		 */
		public PagingComponent<E> build() {
			return new PagingComponent<E>(this);
		}
	}
}