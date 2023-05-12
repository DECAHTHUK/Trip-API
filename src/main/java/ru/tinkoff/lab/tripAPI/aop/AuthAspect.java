package ru.tinkoff.lab.tripAPI.aop;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Before("execution(* ru.tinkoff.lab.tripAPI.security.AuthService.login(..))")
    public void setLoggerBeforeAuthorization() {
        logger.info("User wants to be authorized");
    }

    @AfterThrowing("execution(* ru.tinkoff.lab.tripAPI.security.AuthService.login(..))")
    public void setLoggerWrongData() {
        logger.error("Wrong password or login / access denied");
    }
}
