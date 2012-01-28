/*
 * Copyright (C) 2012 Bosch Software Innovations GmbH. All rights reserved.
 */

package org.sourcepit.guplex;

import org.sonatype.guice.bean.scanners.asm.AnnotationVisitor;
import org.sonatype.guice.bean.scanners.asm.Attribute;
import org.sonatype.guice.bean.scanners.asm.ClassVisitor;
import org.sonatype.guice.bean.scanners.asm.FieldVisitor;
import org.sonatype.guice.bean.scanners.asm.MethodVisitor;
import org.sourcepit.guplex.IDecoratorFactory;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class ClassVisitorDecorator implements ClassVisitor
{
   protected IDecoratorFactory decoratorFactory;

   protected ClassVisitor classVisitor;

   public ClassVisitorDecorator(IDecoratorFactory decoratorFactory, ClassVisitor delegate)
   {
      this.decoratorFactory = decoratorFactory;
      this.classVisitor = delegate;
   }

   public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
   {
      classVisitor.visit(version, access, name, signature, superName, interfaces);
   }

   public void visitSource(String source, String debug)
   {
      classVisitor.visitSource(source, debug);
   }

   public void visitOuterClass(String owner, String name, String desc)
   {
      classVisitor.visitOuterClass(owner, name, desc);
   }

   public AnnotationVisitor visitAnnotation(String desc, boolean visible)
   {
      return decoratorFactory.decorate(classVisitor.visitAnnotation(desc, visible));
   }

   public void visitAttribute(Attribute attr)
   {
      classVisitor.visitAttribute(attr);
   }

   public void visitInnerClass(String name, String outerName, String innerName, int access)
   {
      classVisitor.visitInnerClass(name, outerName, innerName, access);
   }

   public FieldVisitor visitField(int access, String name, String desc, String signature, Object value)
   {
      return decoratorFactory.decorate(classVisitor.visitField(access, name, desc, signature, value));
   }

   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
   {
      return decoratorFactory.decorate(classVisitor.visitMethod(access, name, desc, signature, exceptions));
   }

   public void visitEnd()
   {
      classVisitor.visitEnd();
   }

}
