package com;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.util.Optional;

/**
 * @author lizhibo (Employee ID: R0015004)
 * @version 1.0.0, 2018/6/18 17:41
 * @since 1.0.0, 2018/6/18 17:41
 */
public class ProjectUtils {

	public static Optional<String> getSourceOutPath(Filer filer,String pkg,String relativeName) {
		try {
			FileObject fileObject = filer.getResource(StandardLocation.SOURCE_OUTPUT,pkg,relativeName);
			return Optional.ofNullable(fileObject.toUri().getPath());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	public static String convertSourcePath(String sourceOutPath) {
		return sourceOutPath.substring(0,sourceOutPath.lastIndexOf("/target")) + "/src/main/java";
	}
}
