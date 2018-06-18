package com;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @author lizhibo (Employee ID: R0015004)
 * @version 1.0.0, 2018/6/18 18:30
 * @since 1.0.0, 2018/6/18 18:30
 */
public class ScanJava {

	private Resource[] resources;

	public ScanJava(String sourcePath) {
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		try {
			resources = resolver.getResources("file:" + sourcePath + "/**/*.java");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<CompilationUnit> findByAnnoMethod(Class<? extends Annotation> annoClass) {
		return Stream.of(resources).map(this::convertResourceToCompileUnit)
					.filter(Objects::nonNull)
					.filter(compilationUnit ->
							compilationUnit.findFirst(MethodDeclaration.class,md -> this.hasAnno(md,annoClass)).isPresent()
					).collect(toList());
	}

	private boolean hasAnno(MethodDeclaration methodDeclaration,Class<? extends Annotation> annoClass) {
		return methodDeclaration.getAnnotationByClass(annoClass).isPresent();
	}

	private CompilationUnit convertResourceToCompileUnit(Resource resource) {
		try {
			return JavaParser.parse(resource.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
