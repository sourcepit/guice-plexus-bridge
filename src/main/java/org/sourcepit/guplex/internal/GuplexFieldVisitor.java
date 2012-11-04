/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.guplex.internal;

import org.sonatype.guice.bean.scanners.asm.FieldVisitor;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public abstract class GuplexFieldVisitor extends AbstractGuplexVisitor implements FieldVisitor
{
   private String className;

   private String fieldName;

   private String signature;

   public GuplexFieldVisitor visitField(String className, int access, String name, String desc, String signature)
   {
      this.className = className;
      this.fieldName = name;
      this.signature = signature == null ? desc : signature;
      return this;
   }

   public final void visitEnd()
   {
      visitedField(className, fieldName, signature, hasInjectAnnotation, namedAnnotationValue);
      super.visitEnd();
      className = null;
      fieldName = null;
      signature = null;
   }

   protected abstract void visitedField(String className, String fieldName, String signature,
      boolean hasInjectAnnotation, String namedAnnotationValue);

}
