package com.smartcommerce.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Aspect for centralized exception logging across the application.
 * Captures and logs exceptions from service and DAO layers with full context.
 */
@Aspect
@Component
public class ExceptionLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionLoggingAspect.class);

    /**
     * Pointcut for all service layer methods
     */
    @Pointcut("execution(* com.smartcommerce.service..*.*(..))")
    public void serviceLayerPointcut() {
    }

    /**
     * Pointcut for all DAO layer methods
     */
    @Pointcut("execution(* com.smartcommerce.dao..*.*(..))")
    public void daoLayerPointcut() {
    }

    /**
     * Log service layer exceptions with context
     */
    @AfterThrowing(pointcut = "serviceLayerPointcut()", throwing = "exception")
    public void logServiceException(JoinPoint joinPoint, Throwable exception) {
        logException(joinPoint, exception, "SERVICE");
    }

    /**
     * Log DAO layer database errors with details
     */
    @AfterThrowing(pointcut = "daoLayerPointcut()", throwing = "exception")
    public void logDaoException(JoinPoint joinPoint, Throwable exception) {
        logException(joinPoint, exception, "💾 DATABASE");
    }

    /**
     * Generic exception logging logic with full context
     */
    private void logException(JoinPoint joinPoint, Throwable exception, String layer) {
        String className = getSimpleClassName(joinPoint.getSignature().getDeclaringTypeName());
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        
        logger.error("❌ [{}] Exception in {}.{}()",
                layer,
                className,
                methodName);
        
        logger.error("❌ [{}] Method arguments: {}",
                layer,
                formatArguments(args));
        
        logger.error("❌ [{}] Exception type: {}",
                layer,
                exception.getClass().getName());
        
        logger.error("❌ [{}] Exception message: {}",
                layer,
                exception.getMessage());
        
        // Log the root cause if different from the main exception
        Throwable rootCause = getRootCause(exception);
        if (rootCause != null && rootCause != exception) {
            logger.error("❌ [{}] Root cause: {} - {}",
                    layer,
                    rootCause.getClass().getName(),
                    rootCause.getMessage());
        }
        
        // Log stack trace at debug level to avoid log pollution
        logger.debug("❌ [{}] Full stack trace for {}.{}():",
                layer,
                className,
                methodName,
                exception);
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
        if (argsString.length() > 500) {
            return argsString.substring(0, 500) + "...]";
        }
        return argsString;
    }

    /**
     * Get the root cause of an exception chain
     */
    private Throwable getRootCause(Throwable throwable) {
        Throwable rootCause = throwable;
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }
        return rootCause;
    }
}
