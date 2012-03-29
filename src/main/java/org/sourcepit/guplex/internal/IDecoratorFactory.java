/*
 * Copyright (C) 2012 Bosch Software Innovations GmbH. All rights reserved.
 */

package org.sourcepit.guplex.internal;

import org.sonatype.guice.bean.scanners.asm.AnnotationVisitor;
import org.sonatype.guice.bean.scanners.asm.ClassVisitor;
import org.sonatype.guice.bean.scanners.asm.FieldVisitor;
import org.sonatype.guice.bean.scanners.asm.MethodVisitor;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public interface IDecoratorFactory
{
   ClassVisitor decorate(final ClassVisitor classVisitor);

   FieldVisitor decorate(FieldVisitor fieldVisitor);

   AnnotationVisitor decorate(AnnotationVisitor annotationVisitor);

   MethodVisitor decorate(MethodVisitor methodVisitor);
}
