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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.project.IMavenProjectChangedListener;
import org.eclipse.m2e.core.project.MavenProjectChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.part.ViewPart;

public class MavenExecutionView extends ViewPart {

	private static final String POM_EDITOR_ID = "org.eclipse.m2e.editor.MavenPomEditor";
	private TableViewer viewer;
	private IProject projectShown;
	private IPartListener2 editorListener;
	private IMavenProjectChangedListener mavenListener;

	@Override
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(createTable(parent));
		viewer.setContentProvider(new MojoContentProvider());
		viewer.setLabelProvider(new MojoLabelProvider());

		IEditorPart editor = getViewSite().getPage().getActiveEditor();
		if (editor != null) {
			if (POM_EDITOR_ID.equals(editor.getEditorSite().getId()))
				setViewerInput();
		}

		editorListener = new IPartListener2() {
			@Override
			public void partVisible(IWorkbenchPartReference partRef) {
				fillView(partRef);
			}

			@Override
			public void partOpened(IWorkbenchPartReference partRef) {
				fillView(partRef);

			}

			private void fillView(IWorkbenchPartReference partRef) {
				if (POM_EDITOR_ID.equals(partRef.getId())) {
					setViewerInput();
					// System.err.println(new Throwable().getStackTrace()[1]);
				}
			}

			private void clearView(IWorkbenchPartReference partRef) {
				if (POM_EDITOR_ID.equals(partRef.getId())) {
					emptyViewer();
					// System.err.println(new Throwable().getStackTrace()[1]);
				}
			}

			@Override
			public void partInputChanged(IWorkbenchPartReference partRef) {
				// Nothing
			}

			@Override
			public void partHidden(IWorkbenchPartReference partRef) {
				clearView(partRef);
			}

			@Override
			public void partDeactivated(IWorkbenchPartReference partRef) {
				// Nothing to do
			}

			@Override
			public void partClosed(IWorkbenchPartReference partRef) {
				clearView(partRef);
			}

			@Override
			public void partBroughtToTop(IWorkbenchPartReference partRef) {
				fillView(partRef);
			}

			@Override
			public void partActivated(IWorkbenchPartReference partRef) {
				fillView(partRef);
			}
		};

		getViewSite().getPage().addPartListener(editorListener);
	}

	private void setViewerInput() {
		final IProject newProject = ((IResource) getViewSite().getPage().getActiveEditor().getEditorInput().getAdapter(IResource.class)).getProject();
		if (projectShown != null && projectShown.equals(newProject))
			return;

		projectShown = newProject;
		viewer.setInput(projectShown);

		mavenListener = new IMavenProjectChangedListener() {
			public void mavenProjectChanged(MavenProjectChangedEvent[] events, IProgressMonitor monitor) {
				for (int i = 0; i < events.length; i++) {
					if (events[i].getMavenProject() != null)
						if (projectShown.equals(events[i].getMavenProject().getProject()))
							Display.getDefault().asyncExec(new Runnable() {
								@Override
								public void run() {
									viewer.setInput(projectShown);
								}
							});
				}
			}
		};
		MavenPlugin.getMavenProjectRegistry().addMavenProjectChangedListener(mavenListener);
	}

	private void emptyViewer() {
		projectShown = null;
		MavenPlugin.getMavenProjectRegistry().removeMavenProjectChangedListener(mavenListener);
		viewer.setInput(null);
	}

	@Override
	public void setFocus() {
		// Nothing to do
	}

	@Override
	public void dispose() {
		getViewSite().getPage().removePartListener(editorListener);
	}

	private Table createTable(Composite parent) {
		Table table = new Table(parent, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 200;
		for (ColumnNames columnDescriptor : ColumnNames.values()) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(columnDescriptor.getName());
			column.setWidth(columnDescriptor.getWidth());
		}
		return table;
	}
}
