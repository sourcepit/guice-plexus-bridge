/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.guplex;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.sonatype.guice.bean.reflect.ClassSpace;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class GuplexBinder
{
   private final PlexusContainer plexus;

   private final Binder binder;

   private final ClassSpace classSpace;

   private final Set<Class<?>> processedTypes = new HashSet<Class<?>>();

   public GuplexBinder(PlexusContainer plexus, Binder binder, ClassSpace classSpace)
   {
      this.plexus = plexus;
      this.binder = binder;
      this.classSpace = classSpace;
   }

   public void visitedField(String className, String fieldName, String signature, boolean hasInjectAnnotation,
      String namedAnnotationValue)
   {
      final int length = signature.length();

      if (hasInjectAnnotation && length > 2 && signature.charAt(0) == 'L' && signature.charAt(length - 1) == ';')
      {
         processField(className, fieldName);
      }
   }

   private void processField(String className, String fieldName)
   {
      try
      {
         final Class<?> clazz = classSpace.loadClass(className);
         final Field field = clazz.getDeclaredField(fieldName);

         final Set<Class<?>> potentialTypes = new LinkedHashSet<Class<?>>();
         collectPotentialTypes(potentialTypes, field);
         for (Class<?> type : potentialTypes)
         {
            if (processedTypes.add(type))
            {
               processPotentialType(type);
            }
         }
      }
      catch (Exception e)
      { // fail silently
      }
   }

   public void visitedMethod(String className, String methodName, String signature, boolean hasInjectAnnotation,
      String namedAnnotationValue)
   {
      if (hasInjectAnnotation)
      {
         processMethod(className, methodName, signature);
      }
   }

   private void processMethod(String className, String methodName, String signature)
   {
      try
      {
         final Class<?> clazz = classSpace.loadClass(className);
         final Set<Class<?>> potentialTypes = new LinkedHashSet<Class<?>>();
         if ("<init>".equals(methodName))
         {
            for (Constructor<?> constructor : clazz.getDeclaredConstructors())
            {
               if (constructor.getAnnotation(Inject.class) != null)
               {
                  collectPotentialTypes(potentialTypes, constructor);
               }
            }
         }
         else
         {
            for (Method method : clazz.getDeclaredMethods())
            {
               if (methodName.startsWith(method.getName()))
               {
                  if (method.getAnnotation(Inject.class) != null)
                  {
                     collectPotentialTypes(potentialTypes, method);
                  }
               }
            }
         }
         for (Class<?> type : potentialTypes)
         {
            if (processedTypes.add(type))
            {
               processPotentialType(type);
            }
         }
      }
      catch (Exception e)
      { // fail silently
      }
   }
   
   private void collectPotentialTypes(final Set<Class<?>> potentialTypes, Constructor<?> constructor)
   {
      for (Type type : constructor.getGenericParameterTypes())
      {
         collectPotentialTypes(potentialTypes, type);
      }
   }

   private void collectPotentialTypes(final Set<Class<?>> potentialTypes, Method method)
   {
      for (Type type : method.getGenericParameterTypes())
      {
         collectPotentialTypes(potentialTypes, type);
      }
   }

   private static void collectPotentialTypes(final Set<Class<?>> potentialTypes, final Field field)
   {
      final Type type = field.getGenericType();
      collectPotentialTypes(potentialTypes, type);
   }

   private static void collectPotentialTypes(final Set<Class<?>> potentialTypes, final Type type)
   {
      if (type instanceof Class)
      {
         potentialTypes.add((Class<?>) type);
      }
      else if (type instanceof ParameterizedType)
      {
         final ParameterizedType parameterizedType = (ParameterizedType) type;
         collectPotentialTypes(potentialTypes, parameterizedType.getRawType());
         final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
         for (Type typeArg : actualTypeArguments)
         {
            collectPotentialTypes(potentialTypes, typeArg);
         }
      }
      else if (type instanceof GenericArrayType)
      {
         GenericArrayType genericArrayType = (GenericArrayType) type;
         collectPotentialTypes(potentialTypes, genericArrayType.getGenericComponentType());
      }
   }

   private <T> void processPotentialType(final Class<T> type)
   {
      List<ComponentDescriptor<T>> descriptorList = plexus.getComponentDescriptorList(type, null);
      if (descriptorList != null)
      {
         for (ComponentDescriptor<T> componentDescriptor : descriptorList)
         {
            bind(type, componentDescriptor);
         }
      }
   }

   private <T> void bind(final Class<T> bindingType, ComponentDescriptor<T> componentDescriptor)
   {
      final String implTypeName = componentDescriptor.getImplementation();
      final String roleHint = componentDescriptor.getRoleHint();

      final Named bindingName;
      if ("default".equals(roleHint) || roleHint == null)
      {
         bindingName = Names.named(implTypeName);
      }
      else
      {
         bindingName = Names.named(roleHint);
      }

      binder.bind(Key.get(bindingType, bindingName)).toProvider(
         newPlexusProvider(plexus, bindingType, "default".equals(roleHint) ? null : roleHint));
   }

   private <T> Provider<T> newPlexusProvider(final PlexusContainer plexus, final Class<T> type, final String roleHint)
   {
      return new Provider<T>()
      {
         public T get()
         {
            try
            {
               if (roleHint == null)
               {
                  return plexus.lookup(type);
               }
               return plexus.lookup(type, roleHint);
            }
            catch (ComponentLookupException e)
            {
               throw new IllegalStateException(e);
            }
         }
      };
   }
}
