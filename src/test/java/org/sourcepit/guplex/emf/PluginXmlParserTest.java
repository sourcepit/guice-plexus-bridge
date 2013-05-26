/**
 * Copyright (c) 2013 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.guplex.emf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.util.IOUtil;
import org.junit.Test;

public class PluginXmlParserTest
{
   @Test
   public void testReadEmpty() throws IOException
   {
      final URL url = getClass().getResource(getClass().getSimpleName() + "_ReadEmpty.xml");
      assertNotNull(url);

      final List<EPackageDeclaration> ePackages = parse(url);
      assertEquals(0, ePackages.size());
   }

   @Test
   public void testReadEPackages() throws IOException
   {
      final URL url = getClass().getResource(getClass().getSimpleName() + "_ReadEPackages.xml");
      assertNotNull(url);

      final List<EPackageDeclaration> ePackages = parse(url);
      assertEquals(4, ePackages.size());
      for (int i = 1; i <= ePackages.size(); i++)
      {
         EPackageDeclaration ePackage = ePackages.get(i - 1);
         assertEquals("uri" + i, ePackage.getUri());
         assertEquals("class" + i, ePackage.getClazz());
         assertEquals("genModel" + i, ePackage.getGenModel());
      }
   }

   private List<EPackageDeclaration> parse(URL url) throws IOException
   {
      final List<EPackageDeclaration> declarations = new ArrayList<EPackageDeclaration>();
      final PluginXmlParser parser = new PluginXmlParser()
      {
         @Override
         protected void handle(EPackageDeclaration ePackageDeclaration)
         {
            declarations.add(ePackageDeclaration);
         }
      };

      InputStream in = null;
      try
      {
         in = url.openStream();
         parser.parse(in);
      }
      finally
      {
         IOUtil.close(in);
      }

      return declarations;
   }
}
