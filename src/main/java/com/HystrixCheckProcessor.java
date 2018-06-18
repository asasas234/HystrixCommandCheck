package com;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author lizhibo (Employee ID: R0015004)
 * @version 1.0.0, 2018/5/20 14:55
 * @since 1.0.0, 2018/5/20 14:55
 */
@SupportedAnnotationTypes({
        "com.HystrixCommandCheck"
})
public class HystrixCheckProcessor extends AbstractProcessor {

    private CompileLog log;

    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.log = new CompileLog(processingEnv.getMessager());
        this.filer = processingEnv.getFiler();
    }

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(HystrixCommandCheck.class);
		if (elements.size() == 0) {
			return true;
		}
		if (elements.size() > 1) {
			log.error("HystrixCommandCheck 最多只能被标注一次，多余的请删掉");
			return true;
		}
		TypeElement element = (TypeElement) elements.iterator().next();
		CommandCheckContext context = new CommandCheckContext(element);
		Optional<String> srcOptional = ProjectUtils.getSourceOutPath(filer,context.getPackageName(),context.getClassName());
		String sourcePath = srcOptional.map(ProjectUtils::convertSourcePath).orElse("");
		if (sourcePath.isEmpty()) {
			log.warn("获取source path 失败");
			return true;
		}
		ScanJava scanJava = new ScanJava(sourcePath);
		List<CompilationUnit> unitList = scanJava.findByAnnoMethod(HystrixCommand.class);
		unitList.forEach(this::validate);
		return true;
	}

	/**
	 * 针对单个unit 验证HystrixCommand 语法
	 * @param compilationUnit
	 */
	private void validate(CompilationUnit compilationUnit) {
		MethodDeclaration methodDeclaration = compilationUnit.findFirst(MethodDeclaration.class,md -> md.getAnnotationByClass(HystrixCommand.class).isPresent()).get();
		AnnotationExpr annotationExpr = methodDeclaration.getAnnotationByClass(HystrixCommand.class).get();
		String fallbackMethodName = JavaParserUtils.getValueByName(annotationExpr,"fallbackMethod");
		Optional<MethodDeclaration> fallbackMethodOptional = JavaParserUtils.getMethodDeclaration(compilationUnit,fallbackMethodName);
		if (!fallbackMethodOptional.isPresent()) {
			log.error("fallbackMethodOptional [" + fallbackMethodName + "] 找不到!~~" );
		}
		MethodDeclaration fallbackMd = fallbackMethodOptional.get();
		if (!fallbackMd.getModifiers().contains(Modifier.PRIVATE)) {
			log.error("fallbackMethodOptional [" + fallbackMethodName + "] 必须是私有的!~~" );
		}
		if (!fallbackMd.getParameters().equals(methodDeclaration.getParameters())) {
			log.error("fallbackMethodOptional [" + fallbackMethodName + "] 参数列表不匹配!~~" );
		}
		if (!fallbackMd.getType().equals(methodDeclaration.getType())) {
			log.error("fallbackMethodOptional [" + fallbackMethodName + "] 返回值不匹配!~~" );
		}
	}

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

}
