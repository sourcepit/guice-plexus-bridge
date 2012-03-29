/**
 * Copyright (c) 2012 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.guplex.internal;

import org.sonatype.guice.bean.scanners.asm.AnnotationVisitor;
import org.sonatype.guice.bean.scanners.asm.Label;
import org.sonatype.guice.bean.scanners.asm.MethodVisitor;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public abstract class GuplexMethodVisitor extends AbstractGuplexVisitor implements MethodVisitor
{
   private String className;

   private String methodName;

   private String signature;

   public GuplexMethodVisitor visitMethod(String className, int access, String name, String desc, String signature)
   {
      this.className = className;
      this.methodName = name;
      this.signature = signature == null ? desc : signature;
      return this;
   }

   public void visitEnd()
   {
      visitedMethod(className, methodName, signature, hasInjectAnnotation, namedAnnotationValue);
      super.visitEnd();
   }

   protected abstract void visitedMethod(String className, String methodName, String signature,
      boolean hasInjectAnnotation, String namedAnnotationValue);

   public AnnotationVisitor visitAnnotationDefault()
   {
      return null;
   }

   public void visitCode()
   {
   }

   public void visitFieldInsn(int arg0, String arg1, String arg2, String arg3)
   {
   }

   public void visitFrame(int arg0, int arg1, Object[] arg2, int arg3, Object[] arg4)
   {
   }

   public void visitIincInsn(int arg0, int arg1)
   {
   }

   public void visitInsn(int arg0)
   {
   }

   public void visitIntInsn(int arg0, int arg1)
   {
   }

   public void visitJumpInsn(int arg0, Label arg1)
   {
   }

   public void visitLabel(Label arg0)
   {
   }

   public void visitLdcInsn(Object arg0)
   {
   }

   public void visitLineNumber(int arg0, Label arg1)
   {
   }

   public void visitLocalVariable(String arg0, String arg1, String arg2, Label arg3, Label arg4, int arg5)
   {
   }

   public void visitLookupSwitchInsn(Label arg0, int[] arg1, Label[] arg2)
   {
   }

   public void visitMaxs(int arg0, int arg1)
   {
   }

   public void visitMethodInsn(int arg0, String arg1, String arg2, String arg3)
   {
   }

   public void visitMultiANewArrayInsn(String arg0, int arg1)
   {
   }

   public AnnotationVisitor visitParameterAnnotation(int arg0, String arg1, boolean arg2)
   {
      return null;
   }

   public void visitTableSwitchInsn(int arg0, int arg1, Label arg2, Label[] arg3)
   {
   }

   public void visitTryCatchBlock(Label arg0, Label arg1, Label arg2, String arg3)
   {
   }

   public void visitTypeInsn(int arg0, String arg1)
   {
   }

   public void visitVarInsn(int arg0, int arg1)
   {
   }

}
