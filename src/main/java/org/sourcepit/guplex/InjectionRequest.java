/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.guplex;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class InjectionRequest extends InjectorRequest
{
   private final List<Object> injectionRoots;

   public InjectionRequest()
   {
      injectionRoots = new ArrayList<Object>();
   }

   public InjectionRequest(InjectionRequest origin)
   {
      super(origin);
      this.injectionRoots = new ArrayList<Object>(origin.getInjectionRoots());
   }

   public List<Object> getInjectionRoots()
   {
      return injectionRoots;
   }
}