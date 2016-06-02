package org.vaadin.pagingcomponent;

import org.vaadin.pagingcomponent.PagingComponent.ChangePageEvent;
import org.vaadin.pagingcomponent.PagingComponent.PageRange;
import org.vaadin.pagingcomponent.PagingComponent.PagingComponentListener;

import com.vaadin.ui.Label;

/**
 * This class displays the items of current page. It can take a immutable caption as parameter which will be added at the front of the items range number.<br>
 * <br>
 * 
 * Examples:
 * <ol>
 * <li>
 * <dd><code>RangeDisplayer();</code>
 * <dd>display --> <i>1 - 10</i></li>
 * <br>
 * <br>
 * <li>
 * <dd><code>RangeDisplayer("Results: ");</code>
 * <dd>display --> <i>Results: 1 - 10</i></li>
 * </ol>
 */
public class RangeDisplayer<I> extends Label implements PagingComponentListener<I> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8337600401275254593L;

	protected String immutableCaption, range;
	protected PagingComponent<I> pagingNavigator;

	public RangeDisplayer() {
		this("");
	}

	public RangeDisplayer(String immutableCaption) {
		this.immutableCaption = immutableCaption;
		range = "";
		this.setImmediate(true);
	}

	public RangeDisplayer(PagingComponent<I> pagingNavigator) {
		this("", pagingNavigator);
	}

	public RangeDisplayer(String immutableCaption, PagingComponent<I> pagingNavigator) {
		this(immutableCaption);
		link(pagingNavigator);
	}

	/**
	 * Refresh the items range number. This method is typically called at each time the page changes.
	 */
	public void updateRange(PageRange<I> pageRange) {
		range = (pageRange.getIndexPageStart() + 1) + " - " + pageRange.getIndexPageEnd();
		setValue(null);
	}

	/**
	 * Connects RangeDisplayer to PaginNavigator so it can be kept updated
	 */
	public void link(PagingComponent<I> pagingNavigator) {
		if (this.pagingNavigator != null) {
			this.pagingNavigator.removeListener(this);
		}
		this.pagingNavigator = pagingNavigator;
		pagingNavigator.addListener(this);
		updateRange(pagingNavigator.getPageRange());
	}

	/**
	 * Breaks the connection between RangeDisplayer and PaginNavigator
	 */
	public void breakLink() {
		pagingNavigator.removeListener(this);
		pagingNavigator = null;
	}

	@Override
	public void setValue(String newValue) {
		if (newValue != null) {
			immutableCaption = newValue;
		}
		super.setValue(immutableCaption + range);
	}

	@Override
	public void displayPage(ChangePageEvent<I> event) {
		updateRange(event.getPageRange());
	}

}
