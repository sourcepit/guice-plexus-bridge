/*
 * Copyright (C) 2012 Bosch Software Innovations GmbH. All rights reserved.
 */

package org.sourcepit.guplex.test;

import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

import javax.inject.Inject;

import org.codehaus.plexus.PlexusTestCase;
import org.sonatype.guice.bean.binders.ParameterKeys;
import org.sonatype.guice.bean.locators.MutableBeanLocator;
import org.sourcepit.guplex.Guplex;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.name.Names;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public abstract class GuplexTestCase extends PlexusTestCase implements Module
{
   @Inject
   private MutableBeanLocator locator;

   @Override
   protected void setUp() throws Exception
   {
      super.setUp();
      lookup(Guplex.class).inject(this, new SetUpModule());
   }

   @Override
   protected void tearDown() throws Exception
   {
      locator.clear();
      super.tearDown();
   }

   final class SetUpModule implements Module
   {
      public void configure(final Binder binder)
      {
         binder.install(GuplexTestCase.this);

         final Properties properties = new Properties();
         properties.put("basedir", getBasedir());
         GuplexTestCase.this.configure(properties);

         binder.bind(ParameterKeys.PROPERTIES).toInstance(properties);

         // will be done internally by Guplex
         // binder.requestInjection(GuplexTestCase.this);
      }
   }

   /**
    * Custom injection bindings.
    * 
    * @param binder The Guice binder
    */
   public void configure(final Binder binder)
   {
      // place any per-test bindings here...
   }

   /**
    * Custom property values.
    * 
    * @param properties The test properties
    */
   public void configure(final Properties properties)
   {
      // put any per-test properties here...
   }

   public final <T> T gLookup(final Class<T> type)
   {
      return gLookup(Key.get(type));
   }

   public final <T> T gLookup(final Class<T> type, final String name)
   {
      return gLookup(type, Names.named(name));
   }

   public final <T> T gLookup(final Class<T> type, final Class<? extends Annotation> qualifier)
   {
      return gLookup(Key.get(type, qualifier));
   }

   public final <T> T gLookup(final Class<T> type, final Annotation qualifier)
   {
      return gLookup(Key.get(type, qualifier));
   }

   private final <T> T gLookup(final Key<T> key)
   {
      final Iterator<? extends Entry<Annotation, T>> i = locator.locate(key).iterator();
      return i.hasNext() ? i.next().getValue() : null;
   }
}
