/**
 * Copyright (c) 2013 Sourcepit.org contributors and others. All rights reserved. This program and the accompanying
 * materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.sourcepit.guplex.emf;
public final class EPackageDeclaration
{
   private final String uri;
   private final String clazz;
   private final String genModel;

   public EPackageDeclaration(String uri, String clazz, String genModel)
   {
      this.uri = uri;
      this.clazz = clazz;
      this.genModel = genModel;
   }

   public String getUri()
   {
      return uri;
   }

   public String getClazz()
   {
      return clazz;
   }

   public String getGenModel()
   {
      return genModel;
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
      result = prime * result + ((genModel == null) ? 0 : genModel.hashCode());
      result = prime * result + ((uri == null) ? 0 : uri.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (obj == null)
      {
         return false;
      }
      if (getClass() != obj.getClass())
      {
         return false;
      }
      EPackageDeclaration other = (EPackageDeclaration) obj;
      if (clazz == null)
      {
         if (other.clazz != null)
         {
            return false;
         }
      }
      else if (!clazz.equals(other.clazz))
      {
         return false;
      }
      if (genModel == null)
      {
         if (other.genModel != null)
         {
            return false;
         }
      }
      else if (!genModel.equals(other.genModel))
      {
         return false;
      }
      if (uri == null)
      {
         if (other.uri != null)
         {
            return false;
         }
      }
      else if (!uri.equals(other.uri))
      {
         return false;
      }
      return true;
   }

   @Override
   public String toString()
   {
      return "EPackageDeclaration [uri=" + uri + ", clazz=" + clazz + ", genModel=" + genModel + "]";
   }

}
