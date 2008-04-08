/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.equinox.internal.security.storage;

import org.eclipse.core.runtime.*;
import org.eclipse.equinox.internal.security.storage.friends.IUICallbacks;

public class CallbacksProvider {

	final private static String EXTENSION_POINT = "org.eclipse.equinox.security.internalUI"; //$NON-NLS-1$
	final private static String PROVIDER_MODULE = "provider";//$NON-NLS-1$
	final private static String CLASS_NAME = "class";//$NON-NLS-1$

	final private static int NUMBER_OF_QUESTIONS = 2;

	private IUICallbacks callback = null;

	static private CallbacksProvider instance = null;

	private CallbacksProvider() {
		// hides constructor
	}

	static public CallbacksProvider getDefault() {
		if (instance == null)
			instance = new CallbacksProvider();
		return instance;
	}

	private void init() {
		IExtensionRegistry registry = RegistryFactory.getRegistry();
		IExtensionPoint point = registry.getExtensionPoint(EXTENSION_POINT);
		IExtension[] extensions = point.getExtensions();

		// little bit of validation
		if (extensions.length == 0)
			return;
		IConfigurationElement[] elements = extensions[0].getConfigurationElements();
		if (elements.length == 0)
			return;
		IConfigurationElement element = elements[0]; // only one module is allowed per extension
		if (!PROVIDER_MODULE.equals(element.getName()))
			return;

		Object clazz;
		try {
			clazz = element.createExecutableExtension(CLASS_NAME);
		} catch (CoreException e) {
			return;
		}

		if ((clazz instanceof IUICallbacks))
			callback = ((IUICallbacks) clazz);
	}

	public String[][] formChallengeResponse() {
		if (callback == null)
			init();
		if (callback != null)
			return callback.setupPasswordRecovery(NUMBER_OF_QUESTIONS);
		return null;
	}

	public IUICallbacks getCallback() {
		if (callback == null)
			init();
		return callback;
	}
}
