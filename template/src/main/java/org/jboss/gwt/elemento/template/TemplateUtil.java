/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.gwt.elemento.template;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import elemental2.dom.Attr;
import elemental2.dom.Element;
import elemental2.dom.HTMLElement;
import elemental2.dom.NamedNodeMap;
import elemental2.dom.Node;
import elemental2.dom.NodeFilter;
import elemental2.dom.TreeWalker;
import jsinterop.base.Js;
import org.jboss.gwt.elemento.core.Elements;
import org.jboss.gwt.elemento.core.IsElement;

import static elemental2.dom.DomGlobal.document;

/**
 * Static helper methods used from code generated by {@code @Templated} annotation processors. You should not need to
 * call any of these methods manually.
 */
@SuppressWarnings("UnnecessaryLocalVariable")
public final class TemplateUtil {

    @FunctionalInterface
    private interface SelectorFunction {

        Element select(HTMLElement context, String identifier);
    }


    private static SelectorFunction DATA_ELEMENT = (context, identifier) ->
            context.querySelector("[data-element=" + identifier + "]");

    private TemplateUtil() {}


    // ------------------------------------------------------ HTMLElement methods

    public static HTMLElement resolveElement(HTMLElement context, String identifier) {
        Element element = DATA_ELEMENT.select(context, identifier);
        HTMLElement htmlElement = Js.cast(element);
        return htmlElement;
    }

    public static <E extends HTMLElement> E resolveElementAs(HTMLElement context, String identifier) {
        Element element = DATA_ELEMENT.select(context, identifier);
        E htmlElement = Js.cast(element);
        return htmlElement;
    }

    public static void replaceElement(HTMLElement context, String identifier, HTMLElement newElement) {
        if (newElement == null) {
            throw new NullPointerException("New element must not be null in TemplateUtils.replaceElement()");
        }
        HTMLElement oldElement = resolveElement(context, identifier);
        if (oldElement != null && oldElement.parentNode != null) {
            oldElement.parentNode.replaceChild(newElement, oldElement);
        }
    }


    // ------------------------------------------------------ IsElement / (Is)Widget methods

    public static void replaceIsElement(HTMLElement context, String identifier, IsElement newElement) {
        replaceElement(context, identifier, newElement.asElement());
    }

    public static void replaceWidget(HTMLElement context, String identifier, Widget newWidget) {
        replaceElement(context, identifier, Elements.asElement(newWidget));
    }

    public static void replaceIsWidget(HTMLElement context, String identifier, IsWidget newWidget) {
        replaceElement(context, identifier, Elements.asElement(newWidget));
    }


    // ------------------------------------------------------ custom elements

    public static <E> E resolveCustomElement(HTMLElement context, String identifier) {
        Element element = DATA_ELEMENT.select(context, identifier);
        E customElement = Js.cast(element);
        return customElement;
    }

    public static <E> void replaceCustomElement(HTMLElement context, String identifier, E customElement) {
        HTMLElement element = Js.cast(customElement);
        replaceElement(context, identifier, element);
    }


    // ------------------------------------------------------ expressions

    public static void replaceExpression(HTMLElement context, String expression, String value) {
        replaceNestedExpressionInText(context, expression, value);
        replaceNestedExpressionInAttributes(context, expression, value);
        // The call above does not catch the attributes in 'context', we need to replace them explicitly.
        replaceExpressionInAttributes(context, expression, value);
    }

    private static void replaceNestedExpressionInText(HTMLElement context, String expression, String value) {
        TreeWalker treeWalker = document.createTreeWalker(context, NodeFilter.SHOW_TEXT, node -> {
            if (node.nodeValue != null && node.nodeValue.contains(expression)) {
                return NodeFilter.FILTER_ACCEPT;
            }
            return NodeFilter.FILTER_SKIP;
        }, false);

        while (treeWalker.nextNode() != null) {
            treeWalker.getCurrentNode().nodeValue = treeWalker.getCurrentNode().nodeValue.replace(expression, value);
        }
    }

    private static void replaceNestedExpressionInAttributes(HTMLElement context, String expression, String value) {
        TreeWalker treeWalker = document.createTreeWalker(context, NodeFilter.SHOW_ELEMENT, null, false);
        while (treeWalker.nextNode() != null) {
            if (treeWalker.getCurrentNode() instanceof HTMLElement) {
                replaceExpressionInAttributes((HTMLElement) treeWalker.getCurrentNode(), expression, value);
            }
        }
    }

    private static void replaceExpressionInAttributes(HTMLElement context, String expression, String value) {
        NamedNodeMap<Attr> attributes = context.attributes;
        for (int i = 0; i < attributes.getLength(); i++) {
            Node attribute = attributes.item(i);
            String currentValue = attribute.nodeValue;
            if (currentValue.contains(expression)) {
                attribute.nodeValue = currentValue.replace(expression, value);
            }
        }
    }
}
