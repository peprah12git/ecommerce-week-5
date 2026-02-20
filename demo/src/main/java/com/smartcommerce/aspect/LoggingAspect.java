package com.smartcommerce.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Aspect for centralized logging of method calls across the application.
 * Provides entry and exit logging for service and controller layers.
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * Pointcut for all service layer methods
     */
    @Pointcut("execution(* com.smartcommerce.service..*.*(..))")
    public void serviceLayerPointcut() {
    }

    /**
     * Pointcut for all REST controller methods
     */
    @Pointcut("execution(* com.smartcommerce.controller..*.*(..))")
    public void controllerLayerPointcut() {
    }

    /**
     * Pointcut for all GraphQL controller methods
     */
    @Pointcut("execution(* com.smartcommerce.controller.graphiqlController..*.*(..))")
    public void graphqlControllerPointcut() {
    }

    /**
     * Log service method entry with parameters
     */
    @Before("serviceLayerPointcut()")
    public void logServiceMethodEntry(JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        logger.debug(" [SERVICE] Entering: {}.{}() with arguments: {}",
                getSimpleClassName(className),
                methodName,
                formatArguments(args));
    }

    /**
     * Log service method completion
     */
    @AfterReturning(pointcut = "serviceLayerPointcut()", returning = "result")
    public void logServiceMethodExit(JoinPoint joinPoint, Object result) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        logger.debug(" [SERVICE] Completed: {}.{}() with result: {}",
                getSimpleClassName(className),
                methodName,
                formatResult(result));
    }

    /**
     * Log REST API controller invocations
     */
    @Before("controllerLayerPointcut() && !graphqlControllerPointcut()")
    public void logControllerMethodEntry(JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        logger.info(" [REST API] Request: {}.{}() with parameters: {}",
                getSimpleClassName(className),
                methodName,
                formatArguments(args));
    }

    /**
     * Log REST API controller completion
     */
    @AfterReturning(pointcut = "controllerLayerPointcut() && !graphqlControllerPointcut()", returning = "result")
    public void logControllerMethodExit(JoinPoint joinPoint, Object result) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        logger.info(" [REST API] Response: {}.{}() completed successfully",
                getSimpleClassName(className),
                methodName);
    }

    /**
     * Log GraphQL controller invocations
     */
    @Before("graphqlControllerPointcut()")
    public void logGraphQLMethodEntry(JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        logger.info(" [GRAPHQL] Query/Mutation: {}.{}() with arguments: {}",
                getSimpleClassName(className),
                methodName,
                formatArguments(args));
    }

    /**
     * Log GraphQL controller completion
     */
    @AfterReturning(pointcut = "graphqlControllerPointcut()", returning = "result")
    public void logGraphQLMethodExit(JoinPoint joinPoint, Object result) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        logger.info(" [GRAPHQL] Response: {}.{}() completed successfully",
                getSimpleClassName(className),
                methodName);
    }

    /**
     * Extract simple class name from fully qualified name
     */
    private String getSimpleClassName(String fullClassName) {
        int lastDot = fullClassName.lastIndexOf('.');
        return lastDot > 0 ? fullClassName.substring(lastDot + 1) : fullClassName;
    }

    /**
     * Format method arguments for logging (truncate if too long)
     */
    private String formatArguments(Object[] args) {
        if (args == null || args.length == 0) {
            return "[]";
        }
        String argsString = Arrays.toString(args);
        if (argsString.length() > 200) {
            return argsString.substring(0, 200) + "...]";
        }
        return argsString;
    }

    /**
     * Format result for logging (truncate if too long)
     */
    private String formatResult(Object result) {
        if (result == null) {
            return "null";
        }
        String resultString = result.toString();
        if (resultString.length() > 200) {
            return resultString.substring(0, 200) + "...";
        }
        return resultString;
    }
}
