/*
 * Copyright (C) 2012 Bosch Software Innovations GmbH. All rights reserved.
 */

package org.sourcepit.guplex.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.sourcepit.guplex.Guplex;

import com.pyx4j.log4j.MavenLogAppender;


/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
public abstract class AbstractGuplexedMojo extends AbstractMojo
{
   /** @component */
   private Guplex guplex;

   public final void execute() throws MojoExecutionException, MojoFailureException
   {
      MavenLogAppender.startPluginLog(this);
      try
      {
         guplex.inject(this);
         doExecute();
      }
      finally
      {
         MavenLogAppender.endPluginLog(this);
      }
   }

   protected abstract void doExecute() throws MojoExecutionException, MojoFailureException;
}
