/*
 * Copyright (C) 2012 Bosch Software Innovations GmbH. All rights reserved.
 */

package org.sourcepit.gpb;

import org.sonatype.guice.bean.scanners.asm.AnnotationVisitor;
import org.sourcepit.gpb.IDecoratorFactory;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class AnnotationVisitorDecorator implements AnnotationVisitor
{
   protected IDecoratorFactory decoratorFactory;
   protected AnnotationVisitor annotationVisitor;

   public AnnotationVisitorDecorator(IDecoratorFactory decoratorFactory, AnnotationVisitor annotationVisitor)
   {
      this.decoratorFactory = decoratorFactory;
      this.annotationVisitor = annotationVisitor;
   }

   public void visit(String name, Object value)
   {
      annotationVisitor.visit(name, value);
   }

   public void visitEnum(String name, String desc, String value)
   {
      annotationVisitor.visitEnum(name, desc, value);
   }

   public AnnotationVisitor visitAnnotation(String name, String desc)
   {
      return decoratorFactory.decorate(this.annotationVisitor.visitAnnotation(name, desc));
   }

   public AnnotationVisitor visitArray(String name)
   {
      return decoratorFactory.decorate(this.annotationVisitor.visitArray(name));
   }

   public void visitEnd()
   {
      annotationVisitor.visitEnd();
   }
}
