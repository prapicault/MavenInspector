/*******************************************************************************
 * Copyright (c) 2012 Pascal Rapicault
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Pascal Rapicault - initial API and implementation
 *******************************************************************************/
package net.rapicault.maveninspector.views;

import java.util.List;

import org.apache.maven.plugin.MojoExecution;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.internal.project.registry.MavenProjectFacade;
import org.eclipse.m2e.core.project.IMavenProjectFacade;

public class MojoContentProvider implements IStructuredContentProvider {
	IMavenProjectFacade project = null;

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		project = MavenPlugin.getMavenProjectRegistry().getProject((IProject) newInput);
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (project == null)
			return new Object[0];
		List<MojoExecution> mojos = null;
		try {
			mojos = ((MavenProjectFacade) project).getExecutionPlan("deploy", new NullProgressMonitor());
			if (mojos != null)
				return mojos.toArray(new MojoExecution[mojos.size()]);
			else
				return new Object[0];
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return new Object[0];
	}

}
