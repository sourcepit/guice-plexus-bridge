/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.guplex;

import org.sonatype.guice.bean.scanners.EmptyAnnotationVisitor;
import org.sonatype.guice.bean.scanners.asm.AnnotationVisitor;
import org.sonatype.guice.bean.scanners.asm.Attribute;
import org.sonatype.guice.bean.scanners.asm.FieldVisitor;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public abstract class GuplexFieldVisitorImpl implements FieldVisitor
{
   private String className;

   private String fieldName;

   private boolean hasInjectAnnotation;

   private String namedAnnotationValue;

   private String signature;

   private final AnnotationVisitor namedAnnotationVisitor = new EmptyAnnotationVisitor()
   {
      @Override
      public void visit(String attributeName, Object value)
      {
         GuplexFieldVisitorImpl.this.namedAnnotationValue = (String) value;
      }
   };

   public GuplexFieldVisitorImpl visitField(String className, int access, String name, String desc, String signature)
   {
      this.className = className;
      this.fieldName = name;
      this.signature = signature == null ? desc : signature;
      return this;
   }

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

   public final void visitEnd()
   {
      visitedField(className, fieldName, signature, hasInjectAnnotation, namedAnnotationValue);
      className = null;
      fieldName = null;
      signature = null;
      hasInjectAnnotation = false;
      namedAnnotationValue = null;
   }

   protected abstract void visitedField(String className, String fieldName, String signature,
      boolean hasInjectAnnotation, String namedAnnotationValue);

}
