package org.kettle.ext.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @description: 异常工具
 *
 * @author:   ZX
 * @date:     2018/11/21 11:28
 */
public class ExceptionUtils {

	public static String toString(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}
	
}
