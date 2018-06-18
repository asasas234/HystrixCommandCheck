package com;

import javax.lang.model.element.TypeElement;

/**
 * @author lizhibo (Employee ID: R0015004)
 * @version 1.0.0, 2018/6/18 18:13
 * @since 1.0.0, 2018/6/18 18:13
 */
public class CommandCheckContext {

	private String packageName;

	private String className;

	public CommandCheckContext(TypeElement typeElement) {
		this.className = typeElement.getSimpleName().toString();
		String fullName = typeElement.getQualifiedName().toString();
		this.packageName = fullName.substring(0,fullName.lastIndexOf("."));
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
}
