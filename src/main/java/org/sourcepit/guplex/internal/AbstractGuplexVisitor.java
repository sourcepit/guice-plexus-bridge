/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.guplex.internal;

import org.sonatype.guice.bean.scanners.EmptyAnnotationVisitor;
import org.sonatype.guice.bean.scanners.asm.AnnotationVisitor;
import org.sonatype.guice.bean.scanners.asm.Attribute;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public abstract class AbstractGuplexVisitor
{
   protected boolean hasInjectAnnotation;

   protected String namedAnnotationValue;

   private final AnnotationVisitor namedAnnotationVisitor = new EmptyAnnotationVisitor()
   {
      @Override
      public void visit(String attributeName, Object value)
      {
         AbstractGuplexVisitor.this.namedAnnotationValue = (String) value;
      }
   };

   public AnnotationVisitor visitAnnotation(String desc, boolean visible)
   {
      if ("Ljavax/inject/Inject;".equals(desc))
      {
         hasInjectAnnotation = true;
      }
      else if ("Ljavax/inject/Named;".equals(desc))
      {
         return namedAnnotationVisitor;
      }
      return null;
   }

   public void visitAttribute(Attribute attr)
   {
   }

   public void visitEnd()
   {
      hasInjectAnnotation = false;
      namedAnnotationValue = null;
   }

}
