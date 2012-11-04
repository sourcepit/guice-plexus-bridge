/*
 * Copyright (C) 2012 Bosch Software Innovations GmbH. All rights reserved.
 */

package org.sourcepit.guplex;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.sonatype.guice.bean.binders.WireModule;
import org.sonatype.guice.bean.reflect.URLClassSpace;
import org.sonatype.inject.BeanScanning;
import org.sourcepit.guplex.internal.GuplexSpaceModule;

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

   public void inject(final Object injectionRoot, boolean useIndex, Module... modules)
   {
      final InjectionRequest request = new InjectionRequest();
      request.setUseIndex(useIndex);
      request.getInjectionRoots().add(injectionRoot);
      if (modules != null)
      {
         Collections.addAll(request.getModules(), modules);
      }
      internalInject(request);
   }

   public void inject(InjectionRequest request)
   {
      internalInject(new InjectionRequest(request));
   }

   public Injector createInjector(InjectorRequest request)
   {
      return internalCreateInjector(new InjectorRequest(request));
   }

   private Injector internalCreateInjector(InjectorRequest request)
   {
      populateDefaults(request);
      return Guice.createInjector(new WireModule(request.getModules()));
   }

   private void internalInject(InjectionRequest request)
   {
      populateDefaults(request);
      Guice.createInjector(new WireModule(request.getModules()));
   }

   private void populateDefaults(InjectionRequest request)
   {
      final List<Object> injectionRoots = request.getInjectionRoots();
      final Set<Class<?>> classes = request.getAdditionalClassesToScan();
      final List<Module> modules = request.getModules();
      for (final Object injectionRoot : injectionRoots)
      {
         collectClasses(classes, injectionRoot.getClass());
         modules.add(new AbstractModule()
         {
            @Override
            protected void configure()
            {
               requestInjection(injectionRoot);
            }
         });
      }
      populateDefaults((InjectorRequest) request);
   }

   private void collectClasses(final Set<Class<?>> classes, final Class<?> clazz)
   {
      classes.add(clazz);

      final Class<?> superClass = clazz.getSuperclass();
      if (superClass != null && !Object.class.equals(superClass))
      {
         collectClasses(classes, superClass);
      }

      for (Class<?> interfaze : clazz.getInterfaces())
      {
         collectClasses(classes, interfaze);
      }
   }

   private void populateDefaults(InjectorRequest request)
   {
      final Set<ClassLoader> classLoaders = request.getClassLoaders();
      if (classLoaders.isEmpty())
      {
         for (Class<?> clazz : request.getAdditionalClassesToScan())
         {
            classLoaders.add(clazz.getClassLoader());
         }
      }

      final Set<String> classNames = new LinkedHashSet<String>();
      for (Class<?> clazz : request.getAdditionalClassesToScan())
      {
         classNames.add(clazz.getName());
      }

      final List<Module> modules = request.getModules();
      for (ClassLoader classLoader : classLoaders)
      {
         modules.add(newGuplexSpaceModule(classLoader, classNames, request.isUseIndex()));
      }
   }

   private GuplexSpaceModule newGuplexSpaceModule(ClassLoader classLoader, Collection<String> classNames,
      boolean useIndex)
   {
      return new GuplexSpaceModule(plexusContainer, new URLClassSpace(classLoader), useIndex
         ? BeanScanning.INDEX
         : BeanScanning.ON, classNames);
   }
}
