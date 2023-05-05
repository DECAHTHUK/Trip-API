package ru.tinkoff.lab.tripAPI.aop;

import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.tinkoff.lab.tripAPI.business.Id;

@Aspect
@Component
public class RequestAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Before("execution(* ru.tinkoff.lab.tripAPI.business.service.RequestService.createRequest(..)))")
    public void setLogCreateRequest() {
        logger.info("Before method of creating request");
    }

    @AfterReturning(
            pointcut=("execution(* ru.tinkoff.lab.tripAPI.business.service.RequestService.createRequest(..)))"),
            returning="uuid"
    )
    public void ifRequestCreatedReturnedValue(Id uuid) {
        logger.info("New office with " + uuid.getId() + " id was created successfully");
    }

    @AfterThrowing("execution(* ru.tinkoff.lab.tripAPI.business.service.RequestService.createRequest(..)))")
    public void ifRequestCreatedUnsuccessfully() {
        logger.error("There is error for creating request");
    }

    @AfterReturning("execution(* ru.tinkoff.lab.tripAPI.business.service.RequestService.getRequest(..)))")
    public void ifRequestGetsReturned() {
        logger.info("Request returned successfully");
    }

    @AfterThrowing("execution(* ru.tinkoff.lab.tripAPI.business.service.RequestService.getRequest(..)))")
    public void ifRequestGetsReturnedUnsuccessfully() {
        logger.error("Exception in getRequest() method");
    }

    @After("execution(* ru.tinkoff.lab.tripAPI.business.service.RequestService.deleteRequest(..)))")
    public void setLogDeleteRequest() {
        logger.info("Request deleting method");
    }

    @Before("execution(* ru.tinkoff.lab.tripAPI.business.service.RequestService.updateRequest(..)))")
    public void setLogUpdateRequest() {
        logger.info("Request updating method");
    }

    @AfterThrowing("execution(* ru.tinkoff.lab.tripAPI.business.service.RequestService.updateRequest(..)))")
    public void setLoggerUpdateException() {
        logger.error("request was not updated!");
    }

    @After("execution(* ru.tinkoff.lab.tripAPI.business.service.RequestService.getIncomingRequests(..)))")
    public void setLoggerGetIncomingRequests() {
        logger.info("after getting incoming requests");
    }

    @After("execution(* ru.tinkoff.lab.tripAPI.business.service.RequestService.getOutgoingRequests(..)))")
    public void setLoggerGetOutgoingRequests() {
        logger.info("after getting incoming requests");
    }

    @After("execution(* ru.tinkoff.lab.tripAPI.business.service.RequestService.changeStatus(..)))")
    public void setLoggerChangeStatus() {
        logger.info("Status of request has been changed");
    }

    @After("execution(* ru.tinkoff.lab.tripAPI.business.service.RequestService.approveRequest(..)))")
    public void setLoggerApproveRequest() {
        logger.info("Request has been approved");
    }
}