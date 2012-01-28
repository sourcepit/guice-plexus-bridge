/*
 * Copyright (C) 2012 Bosch Software Innovations GmbH. All rights reserved.
 */

package org.sourcepit.gpb;

import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.PlexusTestCase;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.sonatype.guice.bean.binders.WireModule;
import org.sonatype.guice.bean.reflect.ClassSpace;
import org.sonatype.guice.bean.reflect.URLClassSpace;
import org.sonatype.inject.BeanScanning;
import org.sourcepit.gpb.jsr330.Jsr330Component;
import org.sourcepit.gpb.jsr330.NamedJsr330Component;
import org.sourcepit.gpb.plexus.NamedPlexusComponent;
import org.sourcepit.gpb.plexus.PlexusComponent;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class GpbSpaceModuleTest extends PlexusTestCase
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

   public void testAll() throws Exception
   {
      final ClassSpace space = new URLClassSpace(getClass().getClassLoader());
      GpbSpaceModule gpbSpaceModule = new GpbSpaceModule(getContainer(), space, BeanScanning.CACHE);
      Guice.createInjector(new WireModule(new GpbModule(), gpbSpaceModule));
      
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

   final class GpbModule extends AbstractModule
   {
      @Override
      protected void configure()
      {
         requestInjection(GpbSpaceModuleTest.this);
      }
   }
}
