package org.vaadin.pagingcomponent;

/**
 * Used during the construction of a {@link PagingComponent} to provide the initial page number.
 * 
 * Different implementations of this class can be found in the {@link PageNumberProviderFactory}.
 */
public interface PageNumberProvider {
	
	/**
	 * Create an initial page number of the {@link PagingComponent}.
	 * 
	 * @param numberTotalOfPages the total of pages that contains the {@link PagingComponent}.
	 * @return a page number
	 */
	int getPageNumber(int numberTotalOfPages);

}
