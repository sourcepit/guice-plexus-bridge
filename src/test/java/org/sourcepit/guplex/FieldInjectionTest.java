/*
 * Copyright (C) 2012 Bosch Software Innovations GmbH. All rights reserved.
 */

package org.sourcepit.guplex;

import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.codehaus.plexus.PlexusContainer;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.sourcepit.guplex.jsr330.Jsr330Component;
import org.sourcepit.guplex.jsr330.NamedJsr330Component;
import org.sourcepit.guplex.plexus.NamedPlexusComponent;
import org.sourcepit.guplex.plexus.PlexusComponent;
import org.sourcepit.guplex.test.GuplexTest;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class FieldInjectionTest extends GuplexTest
{
   @Inject
   private IComponent component;

   @Inject
   private List<IComponent> componentList;

   @Inject
   private Map<String, IComponent> componentMap;

   @Inject
   private PlexusContainer plexusContainer;

   @Inject
   @Named("namedPlexusComponent")
   private IComponent namedPlexusComponent;

   @Inject
   @Named("namedJsr330Component")
   private IComponent namedJsr330Component;

   @Override
   protected ClassLoader getClassLoader()
   {
      return new ClassLoader(super.getClassLoader())
      {
         @Override
         public Class<?> loadClass(String name) throws ClassNotFoundException
         {
            if (MethodInjectionTestCase.class.getName().equals(name))
            {
               throw new ClassNotFoundException();
            }
            if (ConstructorInjectionTestCase.class.getName().equals(name))
            {
               throw new ClassNotFoundException();
            }
            if (NeedConstructorInjection.class.getName().equals(name))
            {
               throw new ClassNotFoundException();
            }
            return super.loadClass(name);
         }
      };
   }

   @Test
   public void testAll() throws Exception
   {
      assertThat(plexusContainer, IsNull.notNullValue());

      assertThat(component, IsNull.notNullValue());

      assertThat(componentList, IsNull.notNullValue());
      assertThat(componentList.size(), Is.is(4));

      assertThat(componentMap, IsNull.notNullValue());
      assertThat(componentMap.size(), Is.is(4));
      assertThat(componentMap.get(PlexusComponent.class.getName()), IsNull.notNullValue());
      assertThat(componentMap.get("namedPlexusComponent"), IsNull.notNullValue());
      assertThat(componentMap.get(Jsr330Component.class.getName()), IsNull.notNullValue());
      assertThat(componentMap.get("namedJsr330Component"), IsNull.notNullValue());

      assertThat(namedPlexusComponent instanceof NamedPlexusComponent, Is.is(true));
      assertThat(namedJsr330Component instanceof NamedJsr330Component, Is.is(true));
   }
}
