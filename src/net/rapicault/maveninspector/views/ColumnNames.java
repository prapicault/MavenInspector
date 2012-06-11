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

public enum ColumnNames {
	PHASE("Phase", 125), PLUGIN("Plugin", 280), GOAL("Goal", 150), EXECUTION_ID("Execution id", 150);
	
	private String name;
	private int width;
	
	ColumnNames(String columnHeader, int width) {
		name = columnHeader;
		this.width = width;
	}
	
	public String getName() {
		return name;
	}
	
	public int getWidth() {
		return width;
	}
}
