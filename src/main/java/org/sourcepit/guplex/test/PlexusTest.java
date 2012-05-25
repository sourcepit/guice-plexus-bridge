/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.guplex.test;

import static org.junit.Assert.fail;

import java.io.InputStream;

import org.codehaus.plexus.ContainerConfiguration;
import org.codehaus.plexus.DefaultContainerConfiguration;
import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.PlexusContainerException;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.context.Context;
import org.codehaus.plexus.context.DefaultContext;
import org.junit.After;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public abstract class PlexusTest
{
   private PlexusContainer container;

   protected void setupContainer()
   {
      // ----------------------------------------------------------------------------
      // Context Setup
      // ----------------------------------------------------------------------------

      final DefaultContext context = new DefaultContext();

      customizeContext(context);

      // ----------------------------------------------------------------------------
      // Configuration
      // ----------------------------------------------------------------------------

      final String config = getCustomConfigurationName();

      final ContainerConfiguration containerConfiguration = new DefaultContainerConfiguration().setName("test")
         .setContext(context.getContextData());

      if (config != null)
      {
         containerConfiguration.setContainerConfiguration(config);
      }
      else
      {
         final String resource = getConfigurationName(null);

         containerConfiguration.setContainerConfiguration(resource);
      }

      customizeContainerConfiguration(containerConfiguration);

      try
      {
         container = newPlexusContainer(containerConfiguration);
      }
      catch (final PlexusContainerException e)
      {
         e.printStackTrace();
         fail("Failed to create plexus container.");
      }
   }

   protected DefaultPlexusContainer newPlexusContainer(final ContainerConfiguration containerConfiguration)
      throws PlexusContainerException
   {
      return new DefaultPlexusContainer(containerConfiguration);
   }

   /**
    * Allow custom test case implementations do augment the default container configuration before executing tests.
    * 
    * @param containerConfiguration
    */
   protected void customizeContainerConfiguration(final ContainerConfiguration containerConfiguration)
   {
   }

   protected void customizeContext(final Context context)
   {
   }

   protected PlexusConfiguration customizeComponentConfiguration()
   {
      return null;
   }

   @After
   protected void tearDown() throws Exception
   {
      if (container != null)
      {
         container.dispose();

         container = null;
      }
   }

   protected PlexusContainer getContainer()
   {
      if (container == null)
      {
         setupContainer();
      }

      return container;
   }

   protected InputStream getConfiguration() throws Exception
   {
      return getConfiguration(null);
   }

   protected InputStream getConfiguration(final String subname) throws Exception
   {
      return getResourceAsStream(getConfigurationName(subname));
   }

   protected String getCustomConfigurationName()
   {
      return null;
   }

   /**
    * Allow the retrieval of a container configuration that is based on the name of the test class being run. So if you
    * have a test class called org.foo.FunTest, then this will produce a resource name of org/foo/FunTest.xml which
    * would be used to configure the Plexus container before running your test.
    * 
    * @param subname
    * @return
    */
   protected String getConfigurationName(final String subname)
   {
      return getClass().getName().replace('.', '/') + ".xml";
   }

   protected InputStream getResourceAsStream(final String resource)
   {
      return getClass().getResourceAsStream(resource);
   }

   protected ClassLoader getClassLoader()
   {
      return getClass().getClassLoader();
   }

   // ----------------------------------------------------------------------
   // Container access
   // ----------------------------------------------------------------------

   protected Object lookup(final String componentKey) throws Exception
   {
      return getContainer().lookup(componentKey);
   }

   protected Object lookup(final String role, final String roleHint) throws Exception
   {
      return getContainer().lookup(role, roleHint);
   }

   protected <T> T lookup(final Class<T> componentClass) throws Exception
   {
      return getContainer().lookup(componentClass);
   }

   protected <T> T lookup(final Class<T> componentClass, final String roleHint) throws Exception
   {
      return getContainer().lookup(componentClass, roleHint);
   }

   protected void release(final Object component) throws Exception
   {
      getContainer().release(component);
   }
}
