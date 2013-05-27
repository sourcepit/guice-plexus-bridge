/**
 * Copyright (c) 2013 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.guplex.emf;

import com.google.inject.AbstractModule;

public class EPackageInitializingModule extends AbstractModule
{
   private final ClassLoader classLoader;

   private final EPackageInitializor initializor = new EPackageInitializor();

   public EPackageInitializingModule()
   {
      this(EPackageInitializingModule.class.getClassLoader());
   }

   public EPackageInitializingModule(ClassLoader classLoader)
   {
      this.classLoader = classLoader;
   }

   @Override
   protected void configure()
   {
      initializor.initEPackages(classLoader);
   }

}
