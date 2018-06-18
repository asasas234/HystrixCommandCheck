package com;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;

import java.util.Optional;

/**
 * @author lizhibo (Employee ID: R0015004)
 * @version 1.0.0, 2018/6/18 20:06
 * @since 1.0.0, 2018/6/18 20:06
 */
public class JavaParserUtils {

	public static String getValueByName(AnnotationExpr annotationExpr, String name) {
		return annotationExpr.getChildNodes().stream().filter(node -> node instanceof MemberValuePair)
				.map(node -> (MemberValuePair) node).filter(node -> node.getNameAsString().equals(name)).findFirst()
				.map(node -> node.getValue().asStringLiteralExpr().asString()).orElse("");
	}

	public static Optional<MethodDeclaration> getMethodDeclaration(CompilationUnit unit,String methodName) {
		return unit.findFirst(MethodDeclaration.class,md -> md.getNameAsString().equals(methodName));
	}
}
