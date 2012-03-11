/*
 * Copyright (C) 2012 Bosch Software Innovations GmbH. All rights reserved.
 */

package org.sourcepit.guplex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.sonatype.guice.bean.binders.WireModule;
import org.sonatype.guice.bean.reflect.URLClassSpace;
import org.sonatype.inject.BeanScanning;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
@Component(role = Guplex.class)
public class Guplex
{
   @Requirement
   private PlexusContainer plexusContainer;

   public void inject(final Object injectionRoot, Module... modules)
   {
      inject(injectionRoot, injectionRoot.getClass().getClassLoader(), modules);
   }

   public void inject(ClassLoader classLoader, Module... modules)
   {
      final List<Module> _modules = new ArrayList<Module>(modules == null ? 1 : modules.length + 1);
      Collections.addAll(_modules, modules);
      inject(Collections.singletonList(classLoader), _modules);
   }

   public void inject(final Object injectionRoot, ClassLoader classLoader, Module... modules)
   {
      inject(injectionRoot, Collections.singletonList(classLoader), modules);
   }

   public void inject(final Object injectionRoot, List<ClassLoader> classLoaders, Module... modules)
   {
      final List<Module> _modules = new ArrayList<Module>(modules == null ? 2 : modules.length + 2);
      _modules.add(new AbstractModule()
      {
         @Override
         protected void configure()
         {
            requestInjection(injectionRoot);
         }
      });
      if (modules != null)
      {
         Collections.addAll(_modules, modules);
      }
      inject(classLoaders, _modules);
   }

   private void inject(List<ClassLoader> classLoaders, List<Module> modules)
   {
      for (ClassLoader classLoader : classLoaders)
      {
         modules.add(newGuplexSpaceModule(classLoader));
      }
      Guice.createInjector(new WireModule(modules));
   }

   public Injector createInjector(List<? extends ClassLoader> classLoaders, List<Module> modules)
   {
      if (modules == null)
      {
         modules = new ArrayList<Module>(classLoaders.size());
      }
      for (ClassLoader classLoader : classLoaders)
      {
         modules.add(newGuplexSpaceModule(classLoader));
      }
      return Guice.createInjector(new WireModule(modules));
   }

   private GuplexSpaceModule newGuplexSpaceModule(ClassLoader classLoader)
   {
      return new GuplexSpaceModule(plexusContainer, new URLClassSpace(classLoader), BeanScanning.ON);
   }
}
