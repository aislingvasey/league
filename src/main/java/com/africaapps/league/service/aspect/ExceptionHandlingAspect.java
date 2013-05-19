package com.africaapps.league.service.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.africaapps.league.exception.LeagueException;
import com.africaapps.league.exception.LeagueValidationException;

@Component
@Aspect
@Order(-20000)
public class ExceptionHandlingAspect {

  private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlingAspect.class);

  public ExceptionHandlingAspect() {
    LOG.info("Created ExceptionHandlingAspect.");
  }
  
  @Around("execution(* com.africaapps.league..service.*..*(..))")
  public Object process(ProceedingJoinPoint pjp) throws Throwable {
    logServiceCalling(pjp);
    try {
      Object result = pjp.proceed();
      logServiceCalled(pjp);
      return result;
    } catch (Throwable t) {
      //Log the service exception's message and just re-throw the exception
      if (t instanceof LeagueValidationException || t instanceof LeagueException) {                
        throw t;
      } else {
        LOG.error("Interceptor caught throwable:", t);
        throw new LeagueException(t.getMessage(), t);
      }
    }
  }

  private void logServiceCalling(ProceedingJoinPoint pjp) {
    if (LOG.isDebugEnabled()) {
      if (pjp.getTarget() != null) {
        LOG.debug("Calling service: " + pjp.getTarget().getClass() + " method: " + pjp.toShortString());
      } else {
        LOG.debug("Calling unknown: " + pjp.toShortString() + " method: " + pjp.toShortString());
      }
    }
  }

  private void logServiceCalled(ProceedingJoinPoint pjp) {
    if (LOG.isDebugEnabled()) {
      if (pjp.getTarget() != null) {
        LOG.debug("Called service: " + pjp.getTarget().getClass());
      } else {
        LOG.debug("Called unknown: " + pjp.toShortString());
      }
    }
  }  
}