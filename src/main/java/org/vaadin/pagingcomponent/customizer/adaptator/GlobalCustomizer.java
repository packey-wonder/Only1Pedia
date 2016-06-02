package org.vaadin.pagingcomponent.customizer.adaptator;

import org.vaadin.pagingcomponent.PagingComponent;
import org.vaadin.pagingcomponent.customizer.element.ElementsCustomizer;
import org.vaadin.pagingcomponent.customizer.style.StyleCustomizer;

/**
 * If you need to personalize the buttons, the separators and their styles, this class is made for you.
 * Indeed, it extends {@link StyleCustomizer} and {@link ElementsCustomizer} which allow to do that.<br>
 * 
 * Override this class and give it in parameter to the {@link PagingComponent} to personalize this one.
 * 
 * @see StyleCustomizer
 * @see ElementsCustomizer
 */
public interface GlobalCustomizer extends ElementsCustomizer, StyleCustomizer {
	// regroup the two interfaces in one
}
