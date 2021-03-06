package com.africaapps.league.service.transaction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.transaction.annotation.Transactional;

/**
 * WritableTransaction is an extension to the Spring Transactional annotation.
 * It marks the transaction as non-read only and it will roll back the transaction for the application specific
 * exceptions.  
 * @author aisling
 * @version $Id$
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Transactional(
    readOnly=false, 
    rollbackForClassName={
    		"com.africaapps.league.exception.LeagueValidationException", 
        "com.africaapps.league.exception.LeagueException"})
public @interface WriteTransaction {

}
