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
      final List<Module> _modules = new ArrayList<Module>(modules == null ? 2 : modules.length + 2);
      _modules.add(new AbstractModule()
      {
         @Override
         protected void configure()
         {
            requestInjection(injectionRoot);
         }
      });
      Collections.addAll(_modules, modules);
      inject(injectionRoot.getClass().getClassLoader(), _modules);
   }

   public void inject(ClassLoader classLoader, Module... modules)
   {
      final List<Module> _modules = new ArrayList<Module>(modules == null ? 1 : modules.length + 1);
      Collections.addAll(_modules, modules);
      inject(classLoader, _modules);
   }

   private void inject(ClassLoader classLoader, List<Module> modules)
   {
      modules.add(newGuplexSpaceModule(classLoader));
      Guice.createInjector(new WireModule(modules));
   }

   private GuplexSpaceModule newGuplexSpaceModule(ClassLoader classLoader)
   {
      return new GuplexSpaceModule(plexusContainer, new URLClassSpace(classLoader), BeanScanning.CACHE);
   }
}
