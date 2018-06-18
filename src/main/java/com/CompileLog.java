package com;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

/**
 * @author lizhibo (Employee ID: R0015004)
 * @version 1.0.0, 2018/6/18 17:32
 * @since 1.0.0, 2018/6/18 17:32
 */
public class CompileLog {

	public CompileLog(Messager messager) {
		this.messager = messager;
	}

	private Messager messager;

	public void error(String content) {
		messager.printMessage(Diagnostic.Kind.ERROR,content);
	}

	public void warn(String content) {
		messager.printMessage(Diagnostic.Kind.WARNING,content);
	}
}
