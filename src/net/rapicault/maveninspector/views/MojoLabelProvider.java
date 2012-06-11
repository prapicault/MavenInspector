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

import org.apache.maven.plugin.MojoExecution;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class MojoLabelProvider extends BaseLabelProvider implements ITableLabelProvider {
	
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		MojoExecution mojoToPrint = null;
		if(element instanceof MojoExecution) {
			mojoToPrint = (MojoExecution) element;
		} else {
			return "";
		}
			
		if (ColumnNames.PHASE.ordinal() == columnIndex) { 
			return mojoToPrint.getLifecyclePhase();
		}
	
		if (ColumnNames.GOAL.ordinal() == columnIndex) {
			return mojoToPrint.getGoal();
		}
			
		if (ColumnNames.PLUGIN.ordinal() == columnIndex) {
			return mojoToPrint.getPlugin().getId();
		}
		
		if (ColumnNames.EXECUTION_ID.ordinal() == columnIndex) {
			return mojoToPrint.getExecutionId();
		}
		return "";
	}
}
