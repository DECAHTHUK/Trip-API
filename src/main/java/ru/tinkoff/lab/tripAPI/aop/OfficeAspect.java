package ru.tinkoff.lab.tripAPI.aop;

import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.tinkoff.lab.tripAPI.business.Id;

@Aspect
@Component
public class OfficeAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Before("execution(* ru.tinkoff.lab.tripAPI.business.service.OfficeService.createOffice(..)))")
    public void setLogCreateOffice() {
        logger.info("Before method of creating office");
    }

    @AfterReturning(
            pointcut=("execution(* ru.tinkoff.lab.tripAPI.business.service.OfficeService.createOffice(..)))"),
            returning="uuid"
    )
    public void ifOfficeCreatedReturnedValue(Id uuid) {
        logger.info("New office with " + uuid.getId() + " id was created successfully");
    }

    @AfterThrowing("execution(* ru.tinkoff.lab.tripAPI.business.service.OfficeService.createOffice(..)))")
    public void ifOfficeCreatedUnsuccessfully() {
        logger.error("There is error for creating office");
    }

    @AfterReturning("execution(* ru.tinkoff.lab.tripAPI.business.service.OfficeService.getOffice(..)))")
    public void ifOfficeGetsReturned() {
        logger.info("Office returned successfully");
    }

    @AfterThrowing("execution(* ru.tinkoff.lab.tripAPI.business.service.OfficeService.getOffice(..)))")
    public void ifOfficeGetsReturnedUnsuccessfully() {
        logger.error("Exception in getOffice() method");
    }

    @After("execution(* ru.tinkoff.lab.tripAPI.business.service.OfficeService.deleteOffice(..)))")
    public void setLogDeleteOffice() {
        logger.info("Office deleting method");
    }

    @After("execution(* ru.tinkoff.lab.tripAPI.business.service.OfficeService.updateOffice(..)))")
    public void setLogUpdateOffice() {
        logger.info("Office updating method");
    }

    @Before("execution(* ru.tinkoff.lab.tripAPI.controllers.OfficeController.*(..)))")
    public void setLoggerBeforeAccessToOfficeController() {
        logger.info("Before method of OfficeController class");
    }
}