/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.guplex;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.google.inject.Module;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class InjectorRequest
{
   private final List<Module> modules;
   private final Set<ClassLoader> classLoaders;
   private final Set<Class<?>> additionalClassesToScan;
   private boolean useIndex;

   public InjectorRequest()
   {
      this.modules = new ArrayList<Module>();
      this.classLoaders = new LinkedHashSet<ClassLoader>();
      this.additionalClassesToScan = new LinkedHashSet<Class<?>>();
   }

   public InjectorRequest(InjectorRequest origin)
   {
      this.modules = new ArrayList<Module>(origin.getModules());
      this.classLoaders = new LinkedHashSet<ClassLoader>(origin.getClassLoaders());
      this.additionalClassesToScan = new LinkedHashSet<Class<?>>(origin.getAdditionalClassesToScan());
      this.useIndex = origin.isUseIndex();
   }

   public List<Module> getModules()
   {
      return modules;
   }

   public Set<ClassLoader> getClassLoaders()
   {
      return classLoaders;
   }

   public Set<Class<?>> getAdditionalClassesToScan()
   {
      return additionalClassesToScan;
   }

   public boolean isUseIndex()
   {
      return useIndex;
   }

   public void setUseIndex(boolean useIndex)
   {
      this.useIndex = useIndex;
   }
}