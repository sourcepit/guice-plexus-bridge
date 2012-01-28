/*
 * Copyright (C) 2012 Bosch Software Innovations GmbH. All rights reserved.
 */

package org.sourcepit.gpb.plexus;

import org.codehaus.plexus.component.annotations.Component;
import org.sourcepit.gpb.IComponent;

/**
 * @author Bernd Vogt <bernd.vogt@sourcepit.org>
 */
@Component(role = IComponent.class, hint = "namedPlexusComponent")
public class NamedPlexusComponent implements IComponent
{

}
