package tn.esprit.spring.AOP;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

// La classe : Aspect
@Component
@Aspect
@Slf4j
public class FoyerAspect {

    private static final String METHOD_START_LOG = "hello %s";
    private static final String METHOD_END_LOG = "Bye bye %s";
    private static final String METHOD_EXECUTION_TIME_LOG = "Method execution time: %d milliseconds.";

    // Méthode: Advice
    @Before("execution(* tn.esprit.spring.Services..*.*(..))")
    public void method(JoinPoint jp) {
        log.info(String.format(METHOD_START_LOG, jp.getSignature().getName()));
    }

    @After("execution(* tn.esprit.spring.Services..*.*(..))")
    public void methodAfter(JoinPoint jp) {
        log.info(String.format(METHOD_END_LOG, jp.getSignature().getName()));
    }

    @Around("execution(* tn.esprit.spring.Services..*.*(..))")
    public Object profile(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        Object result;

        try {
            result = pjp.proceed(); // Execute the method
        } finally {
            long elapsedTime = System.currentTimeMillis() - start;
            log.info(String.format(METHOD_EXECUTION_TIME_LOG, elapsedTime));
        }

        return result; // Return the result from the method
    }
}
