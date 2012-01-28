/*
 * Copyright (C) 2012 Bosch Software Innovations GmbH. All rights reserved.
 */

package org.sourcepit.guplex;

import org.codehaus.plexus.PlexusContainer;
import org.sonatype.guice.bean.binders.SpaceModule;
import org.sonatype.guice.bean.reflect.ClassSpace;
import org.sonatype.guice.bean.scanners.ClassSpaceVisitor;
import org.sonatype.guice.bean.scanners.asm.ClassVisitor;
import org.sonatype.guice.bean.scanners.asm.FieldVisitor;
import org.sonatype.inject.BeanScanning;

import com.google.inject.Binder;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public class GuplexSpaceModule extends SpaceModule
{
   private final PlexusContainer plexus;

   public GuplexSpaceModule(PlexusContainer plexus, ClassSpace space, BeanScanning scanning)
   {
      super(space, scanning);
      this.plexus = plexus;
   }

   @Override
   protected ClassSpaceVisitor visitor(final Binder binder)
   {
      return new ClassSpaceVisitorDecorator(super.visitor(binder))
      {
         private GuplexFieldBinder fieldBinder;

         @Override
         public void visit(ClassSpace space)
         {
            fieldBinder = new GuplexFieldBinder(plexus, binder, space);
            super.visit(space);
         }

         @Override
         public ClassVisitorDecorator decorate(ClassVisitor classVisitor)
         {
            return new ClassVisitorDecorator(this, classVisitor)
            {
               private String clazzName;

               @Override
               public void visit(int version, int access, String name, String signature, String superName,
                  String[] interfaces)
               {
                  clazzName = name.replace('/', '.');
                  super.visit(version, access, name, signature, superName, interfaces);
               }

               @Override
               public FieldVisitor visitField(int access, String name, String desc, String signature, Object value)
               {
                  return fieldBinder.visitField(clazzName, access, name, desc, signature);
               }
            };
         }
      };
   }

}
