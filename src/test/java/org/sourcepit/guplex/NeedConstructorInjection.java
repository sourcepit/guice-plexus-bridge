/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.guplex;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
@Named
public class NeedConstructorInjection
{
   private IComponent component;

   @Inject
   public NeedConstructorInjection(@Named("namedPlexusComponent") IComponent component)
   {
      this.component = component;
   }

   public IComponent getComponent()
   {
      return component;
   }
}
