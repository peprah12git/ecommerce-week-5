package com.smartcommerce.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Aspect for monitoring execution time of service and DAO layer methods.
 * Logs warnings for methods exceeding the performance threshold.
 */
@Aspect
@Component
public class PerformanceMonitoringAspect {

    private static final Logger logger = LoggerFactory.getLogger(PerformanceMonitoringAspect.class);
    
    /**
     * Performance threshold in milliseconds.
     * Methods exceeding this will trigger a warning log.
     */
    private static final long PERFORMANCE_THRESHOLD_MS = 1000;

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
     * Pointcut for all GraphQL controller methods
     */
    @Pointcut("execution(* com.smartcommerce.controller.graphiqlController..*.*(..))")
    public void graphqlControllerPointcut() {
    }

    /**
     * Monitor service layer methods execution time
     */
    @Around("serviceLayerPointcut()")
    public Object monitorServicePerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        return monitorPerformance(joinPoint, "SERVICE");
    }

    /**
     * Monitor DAO layer database operations execution time
     */
    @Around("daoLayerPointcut()")
    public Object monitorDaoPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        return monitorPerformance(joinPoint, "💾 DATABASE");
    }

    /**
     * Monitor GraphQL controller execution time
     */
    @Around("graphqlControllerPointcut()")
    public Object monitorGraphQLPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        return monitorPerformance(joinPoint, "🔷 GRAPHQL");
    }

    /**
     * Generic performance monitoring logic
     */
    private Object monitorPerformance(ProceedingJoinPoint joinPoint, String layer) throws Throwable {
        String className = getSimpleClassName(joinPoint.getSignature().getDeclaringTypeName());
        String methodName = joinPoint.getSignature().getName();
        String fullMethodName = className + "." + methodName + "()";
        
        long startTime = System.currentTimeMillis();
        boolean success = false;
        
        try {
            Object result = joinPoint.proceed();
            success = true;
            return result;
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            logPerformanceMetrics(layer, fullMethodName, executionTime, success);
        }
    }

    /**
     * Log performance metrics based on execution time
     */
    private void logPerformanceMetrics(String layer, String methodName, long executionTime, boolean success) {
        String status = success ? "✅" : "❌";
        
        if (executionTime > PERFORMANCE_THRESHOLD_MS) {
            // Log warning for slow executions
            logger.warn("⚠️ [{}] SLOW EXECUTION: {} took {}ms (threshold: {}ms) {}",
                    layer,
                    methodName,
                    executionTime,
                    PERFORMANCE_THRESHOLD_MS,
                    status);
        } else {
            // Log normal execution time
            logger.debug("⏱️ [{}] Execution time: {} completed in {}ms {}",
                    layer,
                    methodName,
                    executionTime,
                    status);
        }
    }

    /**
     * Extract simple class name from fully qualified name
     */
    private String getSimpleClassName(String fullClassName) {
        int lastDot = fullClassName.lastIndexOf('.');
        return lastDot > 0 ? fullClassName.substring(lastDot + 1) : fullClassName;
    }
}
