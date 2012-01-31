/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.guplex;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
public class GuplexFieldBinder extends GuplexFieldVisitor
{
   private final PlexusContainer plexus;

   private final Binder binder;

   private final ClassSpace classSpace;

   private final Set<Class<?>> processedTypes = new HashSet<Class<?>>();

   public GuplexFieldBinder(PlexusContainer plexus, Binder binder, ClassSpace classSpace)
   {
      this.plexus = plexus;
      this.binder = binder;
      this.classSpace = classSpace;
   }

   @Override
   protected void visitedField(String className, String fieldName, String signature, boolean hasInjectAnnotation,
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

   private static void collectPotentialTypes(final Set<Class<?>> potentialTypes, final Field field)
   {
      final Class<?> fieldType = field.getType();
      if (fieldType != null)
      {
         potentialTypes.add(fieldType);
      }

      final Type genericType = field.getGenericType();
      if (genericType instanceof ParameterizedType)
      {
         final Type[] actualTypeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
         for (Type typeArg : actualTypeArguments)
         {
            if (typeArg instanceof Class)
            {
               potentialTypes.add((Class<?>) typeArg);
            }
         }
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

      binder.bind(Key.get(bindingType, bindingName)).toProvider(newPlexusProvider(plexus, bindingType, "default".equals(roleHint) ? null : roleHint));
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
