/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.guplex;

import static org.junit.Assert.assertThat;

import javax.inject.Inject;

import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.sourcepit.guplex.test.GuplexTestCase;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class ConstructorInjectionTestCase extends GuplexTestCase
{
   @Inject
   private NeedConstructorInjection foo;
   
   @Override
   protected ClassLoader getPlexusCoreClassLoader()
   {
      return new ClassLoader(super.getClassLoader())
      {
         @Override
         public Class<?> loadClass(String name) throws ClassNotFoundException
         {
            if (FieldInjectionTestCase.class.getName().equals(name))
            {
               throw new ClassNotFoundException();
            }
            if (MethodInjectionTestCase.class.getName().equals(name))
            {
               throw new ClassNotFoundException();
            }
            return super.loadClass(name);
         }
      };
   }
   
   @Override
   protected boolean isUseIndex()
   {
      return getName().contains("_Indexed");
   }
   
   @Test
   public void testConstructor_Indexed() throws Exception
   {
      testConstructor();
   }
   
   public void testConstructor() throws Exception
   {
      assertThat(foo, IsNull.notNullValue());
      assertThat(foo.getComponent(), IsNull.notNullValue());
   }
}
