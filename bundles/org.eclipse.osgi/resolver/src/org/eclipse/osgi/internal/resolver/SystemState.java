/*******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Rob Harrop - SpringSource Inc. (bug 247522)
 *******************************************************************************/
package org.eclipse.osgi.internal.resolver;

import org.eclipse.osgi.service.resolver.*;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

// this class provides synchronous access to resolve and add/remove/update bundle for the framework
public class SystemState extends StateImpl {
	private final BundleContext context;

	public SystemState(BundleContext context) {
		this.context = context;
	}

	@Override
	boolean basicAddBundle(BundleDescription description) {
		if (context != null && description.getUserObject() == null)
			// We always set the user object to a BundleReference object;
			// This allows the resolver to implement BundleRevisions for 
			// the resolver hooks.
			description.setUserObject(context.getBundle(description.getBundleId()));
		return super.basicAddBundle(description);
	}

	/**
	 * @throws BundleException  
	 */
	public StateDelta compare(State state) throws BundleException {
		// we don't implement this (no big deal: the system state is private to the framework)
		throw new UnsupportedOperationException();
	}
}
