/*
 * Copyright (C) 2012 Bosch Software Innovations GmbH. All rights reserved.
 */

package org.sourcepit.gpb;

import java.net.URL;

import org.sonatype.guice.bean.reflect.ClassSpace;
import org.sonatype.guice.bean.scanners.ClassSpaceVisitor;
import org.sonatype.guice.bean.scanners.asm.AnnotationVisitor;
import org.sonatype.guice.bean.scanners.asm.ClassVisitor;
import org.sonatype.guice.bean.scanners.asm.FieldVisitor;
import org.sonatype.guice.bean.scanners.asm.MethodVisitor;
import org.sourcepit.gpb.IDecoratorFactory;
import org.sourcepit.gpb.MethodVisitorDecorator;

/**
 * ClassSpaceVisitorDecorator
 * 
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class ClassSpaceVisitorDecorator implements ClassSpaceVisitor, IDecoratorFactory
{
   protected IDecoratorFactory decoratorFactory;

   protected ClassSpaceVisitor classSpaceVisitor;

   protected boolean decorate;

   public ClassSpaceVisitorDecorator(ClassSpaceVisitor classSpaceVisitor)
   {
      this.classSpaceVisitor = classSpaceVisitor;
   }

   public void visit(ClassSpace space)
   {
      classSpaceVisitor.visit(space);
   }

   public ClassVisitor visitClass(URL url)
   {
      return decorate(classSpaceVisitor.visitClass(url));
   }

   public void visitEnd()
   {
      classSpaceVisitor.visitEnd();
   }

   public ClassVisitor decorate(final ClassVisitor classVisitor)
   {
      if (classVisitor == null || !decorate)
      {
         return classVisitor;
      }
      return new ClassVisitorDecorator(this, classVisitor);
   }

   public FieldVisitor decorate(FieldVisitor fieldVisitor)
   {
      if (fieldVisitor == null || !decorate)
      {
         return fieldVisitor;
      }
      return new FieldVisitorDecorator(this, fieldVisitor);
   }

   public AnnotationVisitor decorate(AnnotationVisitor annotationVisitor)
   {
      if (annotationVisitor == null || !decorate)
      {
         return annotationVisitor;
      }
      return new AnnotationVisitorDecorator(this, annotationVisitor);
   }

   public MethodVisitor decorate(MethodVisitor methodVisitor)
   {
      if (methodVisitor == null || !decorate)
      {
         return methodVisitor;
      }
      return new MethodVisitorDecorator(this, methodVisitor);
   }
}
