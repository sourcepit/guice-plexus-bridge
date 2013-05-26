/**
 * Copyright (c) 2013 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.guplex.emf;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.codehaus.plexus.util.IOUtil;

public class EPackageInitializor
{
   public void initEPackages(ClassLoader classLoader)
   {
      final Set<EPackageDeclaration> ePackageDeclarations = new LinkedHashSet<EPackageDeclaration>();
      discoverEPackageDeclarations(ePackageDeclarations, classLoader);
      if (!ePackageDeclarations.isEmpty())
      {
         initEPackages(classLoader, ePackageDeclarations);
      }
   }

   private static void discoverEPackageDeclarations(final Collection<EPackageDeclaration> ePackageDeclarations,
      ClassLoader classLoader)
   {
      final PluginXmlParser pluginXmlParser = new PluginXmlParser()
      {
         @Override
         protected void handle(EPackageDeclaration ePackageDeclaration)
         {
            ePackageDeclarations.add(ePackageDeclaration);
         }
      };

      final Enumeration<URL> resources;
      try
      {
         resources = classLoader.getResources("plugin.xml");
      }
      catch (IOException e)
      {
         throw new IllegalStateException(e);
      }

      while (resources.hasMoreElements())
      {
         final URL url = resources.nextElement();
         InputStream in = null;
         try
         {
            in = url.openStream();
            pluginXmlParser.parse(in);
         }
         catch (IOException e)
         {
            throw new IllegalStateException(e);
         }
         finally
         {
            IOUtil.close(in);
         }

      }
   }

   private static void initEPackages(final ClassLoader classLoader, final Set<EPackageDeclaration> ePackageDeclarations)
   {
      final Set<Class<?>> ePackageClasses = new LinkedHashSet<Class<?>>();
      final Map<String, Object> uriToForeignEPackage = new LinkedHashMap<String, Object>(ePackageDeclarations.size());
      for (EPackageDeclaration ePackageDeclaration : ePackageDeclarations)
      {
         final Class<?> ePackageClass;
         try
         {
            ePackageClass = classLoader.loadClass(ePackageDeclaration.getClazz());
         }
         catch (ClassNotFoundException e)
         {
            throw new IllegalStateException(e);
         }

         if (classLoader.equals(ePackageClass.getClassLoader()))
         {
            ePackageClasses.add(ePackageClass);
         }
         else
         {
            uriToForeignEPackage.put(ePackageDeclaration.getUri(),
               register(ePackageClass.getClassLoader(), ePackageClass));
         }
      }

      final Object registry = getRegistry(classLoader);

      for (Entry<String, Object> entry : uriToForeignEPackage.entrySet())
      {
         final String uri = entry.getKey();
         final Object ePackage = entry.getValue();
         if (getEPackage(registry, uri) == null)
         {
            put(registry, uri, ePackage);
         }
      }

      for (Class<?> ePackageClass : ePackageClasses)
      {
         register(classLoader, ePackageClass);
      }
   }

   private static Object register(ClassLoader classLoader, Class<?> ePackageClass)
   {
      final Thread currentThread = Thread.currentThread();
      final ClassLoader ctx = currentThread.getContextClassLoader();
      try
      {
         currentThread.setContextClassLoader(classLoader);

         final Field field;
         try
         {
            field = ePackageClass.getField("eINSTANCE");
         }
         catch (NoSuchFieldException e)
         {
            throw new IllegalStateException(e);
         }

         try
         {
            return field.get(null);
         }
         catch (IllegalAccessException e)
         {
            throw new IllegalStateException(e);
         }

      }
      finally
      {
         currentThread.setContextClassLoader(ctx);
      }
   }

   private static void put(Object registry, String uri, Object ePackage)
   {
      invoke(getMethod(registry, "put", Object.class, Object.class), registry, uri, ePackage);
   }

   private static Object getEPackage(Object registry, String uri)
   {
      return invoke(getMethod(registry, "getEPackage", String.class), registry, uri);
   }

   private static Object invoke(final Method method, Object object, Object... args)
   {
      try
      {
         return method.invoke(object, args);
      }
      catch (IllegalAccessException e)
      {
         throw new IllegalStateException(e);
      }
      catch (InvocationTargetException e)
      {
         final Throwable cause = e.getTargetException();
         if (cause instanceof RuntimeException)
         {
            throw (RuntimeException) cause;
         }
         if (cause instanceof Exception)
         {
            throw new IllegalStateException(cause);
         }
         if (cause instanceof Error)
         {
            throw (Error) cause;
         }
         throw new Error(cause);
      }
   }

   private static Method getMethod(Object object, String name, Class<?>... parameterTypes)
   {
      final Method method;
      try
      {
         method = object.getClass().getMethod(name, parameterTypes);
      }
      catch (NoSuchMethodException e)
      {
         throw new IllegalStateException(e);
      }
      return method;
   }

   private static Object getRegistry(ClassLoader classLoader)
   {
      final Thread currentThread = Thread.currentThread();
      final ClassLoader ctx = currentThread.getContextClassLoader();
      try
      {
         currentThread.setContextClassLoader(classLoader);

         final Class<?> registryClass;
         try
         {
            registryClass = classLoader.loadClass("org.eclipse.emf.ecore.impl.EPackageRegistryImpl");
         }
         catch (ClassNotFoundException e)
         {
            throw new IllegalStateException(e);
         }


         final Method method;
         try
         {
            method = registryClass.getMethod("getRegistry", ClassLoader.class);
         }
         catch (NoSuchMethodException e)
         {
            throw new IllegalStateException(e);
         }

         return invoke(method, null, classLoader);
      }
      finally
      {
         currentThread.setContextClassLoader(ctx);
      }
   }
}
