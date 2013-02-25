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
import org.hamcrest.core.IsInstanceOf;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.sourcepit.guplex.jsr330.Jsr330Component;
import org.sourcepit.guplex.jsr330.NamedJsr330Component;
import org.sourcepit.guplex.plexus.NamedPlexusComponent;
import org.sourcepit.guplex.plexus.PlexusComponent;
import org.sourcepit.guplex.test.GuplexTestCase;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class MethodInjectionTestCase extends GuplexTestCase
{
   private IComponent component;

   private List<IComponent> componentList;

   private Map<String, IComponent> componentMap;

   private PlexusContainer plexusContainer;

   @Named("namedPlexusComponent")
   private IComponent namedPlexusComponent;

   @Named("namedJsr330Component")
   private IComponent namedJsr330Component;

   @Override
   protected ClassLoader getPlexusCoreClassLoader()
   {
      return new ClassLoader(super.getPlexusCoreClassLoader())
      {
         @Override
         public Class<?> loadClass(String name) throws ClassNotFoundException
         {
            if (FieldInjectionTestCase.class.getName().equals(name))
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

   @Inject
   public void setComponent(@Named("namedPlexusComponent") IComponent component)
   {
      this.component = component;
   }

   @Inject
   public void setComponentList(List<IComponent> componentList)
   {
      this.componentList = componentList;
   }

   @Inject
   public void setComponentMap(Map<String, IComponent> componentMap)
   {
      this.componentMap = componentMap;
   }

   @Inject
   public void setPlexusContainer(PlexusContainer plexusContainer)
   {
      this.plexusContainer = plexusContainer;
   }

   @Inject
   public void setNamedPlexusComponent(@Named("namedPlexusComponent") IComponent namedPlexusComponent)
   {
      this.namedPlexusComponent = namedPlexusComponent;
   }

   @Inject
   public void setNamedJsr330Component(@Named("namedJsr330Component") IComponent namedJsr330Component)
   {
      this.namedJsr330Component = namedJsr330Component;
   }

   @Override
   protected boolean isUseIndex()
   {
      return getName().contains("_Indexed");
   }

   @Test
   public void testAll_Indexed() throws Exception
   {
      testAll();
   }

   public void testAll() throws Exception
   {
      assertThat(plexusContainer, IsNull.notNullValue());

      assertThat(component, IsNull.notNullValue());

      assertThat(componentList, IsNull.notNullValue());
      assertThat(componentList.size(), Is.is(4));

      assertThat(componentMap, IsNull.notNullValue());
      assertThat(componentMap.size(), Is.is(4));
      assertThat(componentMap.get("default" /* PlexusComponent.class.getName() */),
         IsInstanceOf.instanceOf(PlexusComponent.class));
      assertThat(componentMap.get("namedPlexusComponent"), IsNull.notNullValue());
      assertThat(componentMap.get(Jsr330Component.class.getName()), IsNull.notNullValue());
      assertThat(componentMap.get("namedJsr330Component"), IsNull.notNullValue());

      assertThat(namedPlexusComponent instanceof NamedPlexusComponent, Is.is(true));
      assertThat(namedJsr330Component instanceof NamedJsr330Component, Is.is(true));
   }
}
