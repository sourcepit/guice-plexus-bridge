/**
 * Copyright (c) 2013 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.guplex.emf;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public abstract class PluginXmlParser
{
   private static final XPathExpression EXTENSION = compile("plugin/extension[@point='org.eclipse.emf.ecore.generated_package']");
   private static final XPathExpression DECLARATION = compile("package");

   protected abstract void handle(EPackageDeclaration ePackageDeclaration);

   public void parse(InputStream inputStream)
   {
      parse(parseDocument(inputStream));
   }

   private void parse(final Document document)
   {
      final NodeList generatedPackageNodes = evaluate(document, EXTENSION);
      for (int i = 0; i < generatedPackageNodes.getLength(); i++)
      {
         final NodeList packageNodes = evaluate(generatedPackageNodes.item(i), DECLARATION);
         for (int j = 0; j < packageNodes.getLength(); j++)
         {
            final Element packageElem = (Element) packageNodes.item(j);
            final String uri = packageElem.getAttribute("uri");
            final String clazz = packageElem.getAttribute("class");
            final String genModel = packageElem.getAttribute("genModel");
            if (uri != null && clazz != null)
            {
               handle(new EPackageDeclaration(uri, clazz, genModel));
            }
         }
      }
   }

   private static XPathExpression compile(String expression)
   {
      try
      {
         return XPathFactory.newInstance().newXPath().compile(expression);
      }
      catch (XPathExpressionException e)
      {
         throw new IllegalStateException(e);
      }
   }

   private static NodeList evaluate(Object item, XPathExpression expression)
   {
      try
      {
         return (NodeList) expression.evaluate(item, XPathConstants.NODESET);
      }
      catch (XPathExpressionException e)
      {
         throw new IllegalStateException(e);
      }
   }

   private static Document parseDocument(InputStream inputStream)
   {
      final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setIgnoringComments(true);
      factory.setIgnoringElementContentWhitespace(true);
      try
      {
         final DocumentBuilder builder = factory.newDocumentBuilder();
         return builder.parse(inputStream);
      }
      catch (SAXException e)
      {
         throw new IllegalArgumentException(e);
      }
      catch (IOException e)
      {
         throw new IllegalStateException(e);
      }
      catch (ParserConfigurationException e)
      {
         throw new IllegalStateException(e);
      }
   }
}
