package org.vaadin.pagingcomponent;

/**
 * Allow to create many {@link PageNumberProvider}.
 */
public class PageNumberProviderFactory {
	
	private static final PageNumberProvider FIRST_PAGE_PROVIDER = new PageNumberProvider() {
		
		@Override
		public int getPageNumber(int numberTotalOfPages) {
			return 1;
		}
	};
	
	private static final PageNumberProvider LAST_PAGE_PROVIDER = new PageNumberProvider() {
		
		@Override
		public int getPageNumber(int numberTotalOfPages) {
			return numberTotalOfPages == 0 ? 1 : numberTotalOfPages;
		}
	};
	
	public static PageNumberProvider firstPage() {
		return FIRST_PAGE_PROVIDER;
	}
	
	public static PageNumberProvider lastPage() {
		return LAST_PAGE_PROVIDER;
	}
	
	public static PageNumberProvider pageNumber(int pageNumber) {
		return new SpecificPageNumberProvider(pageNumber);
	}
	
	public static PageNumberProvider page(Page page) {
		return Page.LAST == page ? lastPage() : firstPage();
	}
	
	private static class SpecificPageNumberProvider implements PageNumberProvider {
		
		private int pageNumber;
		
		public SpecificPageNumberProvider(int pageNumber) {
			this.pageNumber = pageNumber;
		}

		@Override
		public int getPageNumber(int numberTotalOfPages) {
			return pageNumber > numberTotalOfPages ? numberTotalOfPages : pageNumber;
		}
		
	}

}
