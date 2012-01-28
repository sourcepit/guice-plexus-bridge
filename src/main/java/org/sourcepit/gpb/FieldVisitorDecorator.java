/*
 * Copyright (C) 2012 Bosch Software Innovations GmbH. All rights reserved.
 */

package org.sourcepit.gpb;

import org.sonatype.guice.bean.scanners.asm.AnnotationVisitor;
import org.sonatype.guice.bean.scanners.asm.Attribute;
import org.sonatype.guice.bean.scanners.asm.FieldVisitor;
import org.sourcepit.gpb.IDecoratorFactory;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class FieldVisitorDecorator implements FieldVisitor
{
   protected IDecoratorFactory decoratorFactory;
   protected FieldVisitor fieldVisitor;

   public FieldVisitorDecorator(IDecoratorFactory decoratorFactory, FieldVisitor fieldVisitor)
   {
      this.decoratorFactory = decoratorFactory;
      this.fieldVisitor = fieldVisitor;
   }

   public AnnotationVisitor visitAnnotation(String desc, boolean visible)
   {
      return decoratorFactory.decorate(fieldVisitor.visitAnnotation(desc, visible));
   }

   public void visitAttribute(Attribute attr)
   {
      fieldVisitor.visitAttribute(attr);
   }

   public void visitEnd()
   {
      fieldVisitor.visitEnd();
   }
}
