package org.vaadin.pagingcomponent.customizer;

import org.vaadin.pagingcomponent.customizer.adaptator.GlobalCustomizer;
import org.vaadin.pagingcomponent.customizer.adaptator.impl.CssCustomizerAdaptator;
import org.vaadin.pagingcomponent.customizer.element.ElementsCustomizer;
import org.vaadin.pagingcomponent.customizer.element.impl.CssElementsCustomizer;
import org.vaadin.pagingcomponent.customizer.style.StyleCustomizer;
import org.vaadin.pagingcomponent.customizer.style.impl.CssStyleCustomizer;

/**
 * Allow to create the different standard customizers.
 */
public class DefaultCustomizerFactory {

	protected static final StyleCustomizer DEFAULT_STYLE_CUSTOMIZER = new CssStyleCustomizer();
	protected static final ElementsCustomizer DEFAULT_ELEMENTS_CUSTOMIZER = new CssElementsCustomizer();
	protected static final GlobalCustomizer DEFAULT_GLOBAL_CUSTOMIZER = new CssCustomizerAdaptator();
	
	public static StyleCustomizer cssStyle() {
		return DEFAULT_STYLE_CUSTOMIZER;
	}
	
	public static ElementsCustomizer cssElements() {
		return DEFAULT_ELEMENTS_CUSTOMIZER;
	}
	
	public static GlobalCustomizer cssGeneral() {
		return DEFAULT_GLOBAL_CUSTOMIZER;
	}

}
