package com.empty.util;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class Logger {

	//服务器
	//private String logFilePath = "//home//log.txt";
	// private String logFilePath = Logger.class.getResource("log.txt").getFile();
	//private String logFilePath = "D://0-Project//Empty//empty_video//log.txt";
	@Pointcut("execution(* com.empty.service..*.*(..))")
	public void declearJoinPointExpression() {
	}
	
	/*
	@Around(value = "declearJoinPointExpression()")
	public Object aroundMethod(ProceedingJoinPoint point) {

		DataTools.WriteStringToFile2(logFilePath, DataTools.GetCurrDate());
		Object result = null;
		String methodName = point.getSignature().getName();

		try {
			// 前置通知

			DataTools.WriteStringToFile2(logFilePath,
					"The method " + methodName + " start. param<" + Arrays.asList(point.getArgs()) + ">");
			// 执行目标方法
			result = point.proceed();
			// 返回通知
			DataTools.WriteStringToFile2(logFilePath, "The method " + methodName + " end. result<" + result + ">");
		} catch (Throwable e) {
			// 异常通知
			DataTools.WriteStringToFile2(logFilePath,
					"！！错误！！ : this method " + methodName + " end.ex message<" + e + ">");
			throw new RuntimeException(e);
		}
		// 后置通知
		DataTools.WriteStringToFile2(logFilePath, "The method " + methodName + " end.");
		return result;
	}
	
	 */

	

	/*
	 * @Before("declearJoinPointExpression()") public void beforMethod(JoinPoint
	 * joinPoint){ String methodName = joinPoint.getSignature().getName();
	 * List<Object> args = Arrays.asList(joinPoint.getArgs());
	 * DataTools.WriteStringToFile2(logFilePath,
	 * "this method "+methodName+" begin. param<"+ args+">"); }
	 *
	 *
	 * @After("declearJoinPointExpression()") public void afterMethod(JoinPoint
	 * joinPoint){ String methodName = joinPoint.getSignature().getName();
	 * DataTools.WriteStringToFile2(logFilePath, "this method "+methodName+" end.");
	 * }
	 *
	 * @AfterReturning(value="declearJoinPointExpression()",returning="result")
	 * public void afterReturnMethod(JoinPoint joinPoint,Object result){ String
	 * methodName = joinPoint.getSignature().getName();
	 * DataTools.WriteStringToFile2(logFilePath,
	 * "this method "+methodName+" end.result<"+result+">"); }
	 *
	 * @AfterThrowing(value="declearJoinPointExpression()",throwing="ex") public
	 * void afterThrowingMethod(JoinPoint joinPoint,Exception ex){ String methodName
	 * = joinPoint.getSignature().getName();
	 * DataTools.WriteStringToFile2(logFilePath,
	 * "this method "+methodName+" end.ex message<"+ex+">"); }
	 */

}
