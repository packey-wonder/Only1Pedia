package org.vaadin.pagingcomponent.strategy.impl;

import org.vaadin.pagingcomponent.ComponentsManager;
import org.vaadin.pagingcomponent.strategy.PageChangeStrategy;

/**
 * Strategy for an odd number of buttons page.
 */
public final class OddPageChangeStrategy extends PageChangeStrategy {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3402267682278488930L;

	public OddPageChangeStrategy(ComponentsManager manager) {
		super(manager);
	}

	@Override
	protected int calculatePageNumberWhereStartTheIteration(int currentPage, int buttonPageMargin) {
		return currentPage - buttonPageMargin;
	}

}
