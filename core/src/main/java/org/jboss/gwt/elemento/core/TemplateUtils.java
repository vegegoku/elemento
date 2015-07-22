/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.gwt.elemento.core;

import elemental.dom.Element;

/**
 * Static helper methods used from code generated by {@code TemplatedProcessor}.
 *
 * @author Harald Pehl
 */
public final class TemplateUtils {

    @FunctionalInterface
    private interface SelectorFunction {

        Element select(Element context, String identifier);
    }


    private static SelectorFunction DATA_ELEMENT = (context, identifier) ->
            context.querySelector("[data-element=" + identifier + "]");

    private TemplateUtils() {}


    // ------------------------------------------------------ Element methods

    public static Element resolveElement(Element context, String identifier) {
        return DATA_ELEMENT.select(context, identifier);
    }

    @SuppressWarnings("unchecked")
    public static <E extends Element> E resolveElementAs(Element context, String identifier) {
        return (E) DATA_ELEMENT.select(context, identifier);
    }

    public static void replaceElement(Element context, String identifier, Element newElement) {
        Element oldElement = resolveElement(context, identifier);
        if (oldElement != null && oldElement.getParentElement() != null) {
            oldElement.getParentElement().replaceChild(newElement, oldElement);
        }
    }


    // ------------------------------------------------------ IsElement methods

    public static IsElement resolveIsElement(Element context, String identifier) {
        return () -> {
            return DATA_ELEMENT.select(context, identifier);
        };
    }

    public static void replaceIsElement(Element context, String identifier, IsElement newElement) {
        replaceElement(context, identifier, newElement.asElement());
    }
}
