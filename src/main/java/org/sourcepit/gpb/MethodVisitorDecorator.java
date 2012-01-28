/*
 * Copyright (C) 2012 Bosch Software Innovations GmbH. All rights reserved.
 */

package org.sourcepit.gpb;

import org.sonatype.guice.bean.scanners.asm.AnnotationVisitor;
import org.sonatype.guice.bean.scanners.asm.Attribute;
import org.sonatype.guice.bean.scanners.asm.Label;
import org.sonatype.guice.bean.scanners.asm.MethodVisitor;

/**
 * MethodVisitorDecorator
 * 
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class MethodVisitorDecorator implements MethodVisitor
{
   protected IDecoratorFactory decoratorFactory;

   protected MethodVisitor methodVisitor;

   public MethodVisitorDecorator(IDecoratorFactory decoratorFactory, MethodVisitor methodVisitor)
   {
      this.decoratorFactory = decoratorFactory;
      this.methodVisitor = methodVisitor;
   }

   public AnnotationVisitor visitAnnotationDefault()
   {
      final AnnotationVisitor annotationVisitor = methodVisitor.visitAnnotationDefault();
      return annotationVisitor == null ? null : decoratorFactory.decorate(annotationVisitor);
   }

   public AnnotationVisitor visitAnnotation(String desc, boolean visible)
   {
      return methodVisitor.visitAnnotation(desc, visible);
   }

   public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible)
   {
      return methodVisitor.visitParameterAnnotation(parameter, desc, visible);
   }

   public void visitAttribute(Attribute attr)
   {
      methodVisitor.visitAttribute(attr);
   }

   public void visitCode()
   {
      methodVisitor.visitCode();
   }

   public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack)
   {
      methodVisitor.visitFrame(type, nLocal, local, nStack, stack);
   }

   public void visitInsn(int opcode)
   {
      methodVisitor.visitInsn(opcode);
   }

   public void visitIntInsn(int opcode, int operand)
   {
      methodVisitor.visitIntInsn(opcode, operand);
   }

   public void visitVarInsn(int opcode, int var)
   {
      methodVisitor.visitVarInsn(opcode, var);
   }

   public void visitTypeInsn(int opcode, String type)
   {
      methodVisitor.visitTypeInsn(opcode, type);
   }

   public void visitFieldInsn(int opcode, String owner, String name, String desc)
   {
      methodVisitor.visitFieldInsn(opcode, owner, name, desc);
   }

   public void visitMethodInsn(int opcode, String owner, String name, String desc)
   {
      methodVisitor.visitMethodInsn(opcode, owner, name, desc);
   }

   public void visitJumpInsn(int opcode, Label label)
   {
      methodVisitor.visitJumpInsn(opcode, label);
   }

   public void visitLabel(Label label)
   {
      methodVisitor.visitLabel(label);
   }

   public void visitLdcInsn(Object cst)
   {
      methodVisitor.visitLdcInsn(cst);
   }

   public void visitIincInsn(int var, int increment)
   {
      methodVisitor.visitIincInsn(var, increment);
   }

   public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels)
   {
      methodVisitor.visitTableSwitchInsn(min, max, dflt, labels);
   }

   public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels)
   {
      methodVisitor.visitLookupSwitchInsn(dflt, keys, labels);
   }

   public void visitMultiANewArrayInsn(String desc, int dims)
   {
      methodVisitor.visitMultiANewArrayInsn(desc, dims);
   }

   public void visitTryCatchBlock(Label start, Label end, Label handler, String type)
   {
      methodVisitor.visitTryCatchBlock(start, end, handler, type);
   }

   public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index)
   {
      methodVisitor.visitLocalVariable(name, desc, signature, start, end, index);
   }

   public void visitLineNumber(int line, Label start)
   {
      methodVisitor.visitLineNumber(line, start);
   }

   public void visitMaxs(int maxStack, int maxLocals)
   {
      methodVisitor.visitMaxs(maxStack, maxLocals);
   }

   public void visitEnd()
   {
      methodVisitor.visitEnd();
   }


}
