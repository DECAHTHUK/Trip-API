# Trip-API (API сервис командировок)
Здесь представлена реализация API сервиса для оформления командировок.
## Содержание
#### 1. [Задача проекта](#1-задача-проекта-1)
#### 2. [Функционал](#2-функционал-1)
#### 3. [Стек технологий](#3-стек-технологий-1)
#### 4. [Методы API](#методы-api)
- [Accommodation's endpoints](#1-accommodations-endpoints-roles-user-admin)
- [Destination's endpoints](#2-destinations-endpoints-roles-user-admin)
- [Login's endpoints](#3-logins-endpoint-авторизация-не-нужна)
- [Notification's endpoints](#4-notifications-endpoints-roles-user-admin)
- [Office's endpoints](#5-offices-endpoints-roles-admin)
- [Request's endpoints](#6-requests-endpoints-roles-user-admin)
- [Trip's endpoints](#7-trips-endpoints--roles-user-admin)
- [User's endpoints](#8-users-endpoints)
#### 5. [Модели](#модели)
- [Accommodation](#1-accommodation)
- [Destination](#2-destination)
- [DestinationDto](#3-destinationdto)
- [Id](#4-id)
- [LoginRequest](#5-loginrequest)
- [Notification](#6-notification)
- [NotificationDto](#7-notificationdto)
- [Office](#8-office)
- [Request](#9-request)
- [RequestDto](#10-requestdto)
- [RequestStatusChangeDto](#11-requeststatuschangedto)
- [Trip](#12-trip)
- [TripDto](#13-tripdto)
- [User](#14-user)
---
### 1. Задача проекта.
Задача проекта в том, чтобы упростить взаимдействие между начальником и работником в случае оформления командировок.
### 2. Функционал.
- Создание связей между начальниками и подчиненными.
- Оформление командировок.
- Создание уведомлений для оптимального рассмотрения заявок.
### 3. Стек технологий.
- Spring boot
- PostgreSQL
- MyBatis
- Liquibase
- Spring Security
- MockMVC
- Docker
- Yandex Cloud
- GitHub Actions
- OpenAPI

## Методы API



### 1. Accommodation's endpoints (Roles: USER, ADMIN)  
- POST /accommodations  
**Создание места размещения.**  
Принимает на вход данные в формате *application/json*  
Передаваемый тип: Accommodation accommodation (обязательно)   
Возвращаемый тип: Id   
Данные возвращаются в формате *application/json*  
Пример:  
```
{
  "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
}
```
- DELETE /accommodations/{uuid}  
**Удаление места размещения по UUID.**  
Параметры: uuid (обязательно)      
- GET /accommodations/{uuid}  
**Получить место размещения по UUID.**    
Параметры: uuid (обязательно)      
Возвращаемый тип: Accommodation  
Данные возвращаются в формате *application/json*  
Пример:    
```
{
  "address" : "address",
  "bookingUrl" : "http://example.com/aeiou",
  "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
}
```
- PUT /accommodations  
**Обновление места размещения.**    
Принимает на вход данные в формате *application/json*   
Передаваемый тип: Accommodation accommodation (обязательно)   



### 2. Destination's endpoints (Roles: USER, ADMIN)  
- POST /destinations  
**Создание места назначения.**    
Принимает на вход данные в формате *application/json*     
Передаваемый тип: DestinationDto destinationDto (обязательно)   
Возвращаемый тип: Id  
Данные возвращаются в формате *application/json*  
```
{
  "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
}
```
- DELETE /destinations/{uuid}  
**Удаление места назначения по UUID.**  
Параметры: uuid (обязательно)    
- GET /destinations/{uuid}  
**Получение места назначения по UUID.**  
Параметры: uuid (обязательно)      
Возвращаемый тип: Destination  
Данные возвращаются в формате *application/json*  
Пример:  
```
{
  "description" : "description",
  "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
  "office" : {
    "address" : "address",
    "description" : "description",
    "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
  },
  "seatPlace" : "seatPlace"
}
```
- PUT /destinations  
**Обновление места назначения.**    
Принимает на вход данные в формате *application/json*   
Передаваемый тип: DestinationDto destinationDto (обязательно)  



### 3. Login's endpoint (Авторизация не нужна)  
- POST /api/login  
**Аутентификация.**  
Принимает на вход данные в формате *application/json*   
Передаваемый тип: LoginRequest LoginRequest (обязательно)  
Возвращаемый тип: String    



### 4. Notification's endpoints (Roles: USER, ADMIN)  
- GET /notifications/{uuid}  
**Получение уведомления по UUID.**  
Параметры: uuid (обязательно)      
Возвращаемый тип: Notification  
Данные возвращаются в формате *application/json*  
Пример:  
```
{
  "request" : {
    "workerFirstName" : "workerFirstName",
    "ticketsUrl" : "http://example.com/aeiou",
    "workerSecondName" : "workerSecondName",
    "workerEmail" : "workerEmail",
    "endDate" : "2000-01-23",
    "approverId" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
    "destination" : {
      "description" : "description",
      "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
      "office" : {
        "address" : "address",
        "description" : "description",
        "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
      },
      "seatPlace" : "seatPlace"
    },
    "description" : "description",
    "comment" : "comment",
    "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
    "requestStatus" : "approved",
    "startDate" : "2000-01-23"
  },
  "watched" : true,
  "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
  "userId" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
}
```

- PUT /notifications/{uuid}/watch  
**Изменение статуса уведомления по UUID на просмотренный.**  
Параметры: uuid (обязательно)  



### 5. Office's endpoints (Roles: ADMIN)
- POST /offices  
**Создание офиса.**    
Принимает на вход данные в формате *application/json*     
Передаваемый тип: Office office (обязательно)   
Возвращаемый тип: Id  
Данные возвращаются в формате *application/json*  
```
{
  "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
}
```
- DELETE /offices/{uuid}  
**Удаление офиса по UUID.**  
Параметры: uuid (обязательно)    
- GET /offices/{uuid}  
**Получение офиса по UUID.**  
Параметры: uuid (обязательно)      
Возвращаемый тип: Notification  
Данные возвращаются в формате *application/json*  
Пример:  
```
{
  "address" : "address",
  "description" : "description",
  "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
}
```
- PUT /offices  
**Обновление данных об офисе.**  
Принимает на вход данные в формате *application/json*   
Передаваемый тип: Office office (обязательно)  




### 6. Request's endpoints (Roles: USER, ADMIN)  
- PUT /requests/{uuid}/approve  
**Подтверждение заявки по UUID с последующим созданием сущности Trip в базе данных (Accommodation и Destination должны создаваться заранее).**  
Параметры: uuid (обязательно)      
Принимает на вход данные в формате *application/json*   
Передаваемый тип: RequestStatusChangeDto requestStatusChangeDto (обязательно)  
Возвращаемый тип: Id  
Данные возвращаются в формате *application/json*  
Пример:  
```
{
  "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
}
```
- POST /requests  
**Создание нового запроса.**  
Принимает на вход данные в формате *application/json*   
Передаваемый тип: RequestDto requestDto (обязательно)  
Возвращаемый тип: Id  
Данные возвращаются в формате *application/json*  
Пример:  
```
{
  "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
}
```
- PUT /requests/{uuid}/decline  
**Изменение статуса заявки на DECLINED с добавлением комментария и айди аппрувера.**  
Параметры: uuid (обязательно)      
Принимает на вход данные в формате *application/json*   
Передаваемый тип: RequestStatusChangeDto requestStatusChangeDto (обязательно)  
- GET /requests/{uuid}  
**Получение реквеста по заданному UUID.**  
Параметры: uuid (обязательно)      
Возвращаемый тип: Request  
Данные возвращаются в формате *application/json*  
Пример:  
```
{
  "workerFirstName" : "workerFirstName",
  "ticketsUrl" : "http://example.com/aeiou",
  "workerSecondName" : "workerSecondName",
  "workerEmail" : "workerEmail",
  "endDate" : "2000-01-23",
  "approverId" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
  "destination" : {
    "description" : "description",
    "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
    "office" : {
      "address" : "address",
      "description" : "description",
      "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
    },
    "seatPlace" : "seatPlace"
  },
  "description" : "description",
  "comment" : "comment",
  "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
  "requestStatus" : "approved",
  "startDate" : "2000-01-23"
}
```
- PUT /requests/{uuid}/send-for-editing  
**Изменение статуса заявки на AWAIT_CHANGES с добавлением комментария и айди аппрувера.**  
Параметры: uuid (обязательно)      
Принимает на вход данные в формате *application/json*   
Передаваемый тип: RequestStatusChangeDto requestStatusChangeDto (обязательно)  
- PUT /requests  
**Обновление запроса.**  
Принимает на вход данные в формате *application/json*   
Передаваемый тип: RequestDto requestDto (обязательно)  



### 7. Trip's endpoints  (Roles: USER, ADMIN)  
- PUT /trips/{uuid}/cancel  
**Отмена командировки по UUID.**  
Параметры: uuid (обязательно)    
- POST /trips  
**Создание командировки.**  
Принимает на вход данные в формате *application/json*   
Передаваемый тип: TripDto tripDto (обязательно)  
Возвращаемый тип: Id  
Данные возвращаются в формате *application/json*  
Пример:  
```
{
  "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
}
```
- GET /trips/{uuid}  
**Получение командировки по заданному UUID.**  
Параметры: uuid (обязательно)      
Возвращаемый тип: Trip  
Данные возвращаются в формате *application/json*  
Пример:  
```
{
  "endDate" : "2000-01-23",
  "requestId" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
  "accommodation" : {
    "address" : "address",
    "bookingUrl" : "http://example.com/aeiou",
    "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
  },
  "destination" : {
    "description" : "description",
    "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
    "office" : {
      "address" : "address",
      "description" : "description",
      "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
    },
    "seatPlace" : "seatPlace"
  },
  "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
  "startDate" : "2000-01-23",
  "tripStatus" : "pending"
}
```
- PUT /trips  
**Обновление данных командировки.**  
Принимает на вход данные в формате *application/json*   
Передаваемый тип: TripDto tripDto (обязательно)  




### 8. User's endpoints.  
- POST /users (Roles: ADMIN)  
**Создание нового пользователя.**
Принимает на вход данные в формате *application/json*   
Передаваемый тип: User user (обязательно)  
Возвращаемый тип: Id  
Данные возвращаются в формате *application/json*  
Пример:  
```
{
  "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
}
```
- POST /users/{approver}/subordinates/{subordinate} (Roles: ADMIN)  
**Создание отношений начальник-подчиненный.**  
Параметры: uuid of approver (обязательно), uuid of subordinate (обязательно).  
- DELETE /users/{approver}/subordinates/{subordinate} (Roles: ADMIN)  
**Удаление отношений начальник-подчиненный.**  
Параметры: uuid of approver (обязательно), uuid of subordinate (обязательно).  
- GET /users/{uuid}/incoming-requests-at/{page} (Roles: USER, ADMIN)  
**Получение страницы номер {page} (либо все сразу если не указано) из n запросов для ментора с заданным {id}.**  
Параметры:  
uuid: UUID (обязательно)  
page: int (не обязательно)  
Возвращаемый тип: array[Request]  
Данные возвращаются в формате *application/json*  
Пример:  
```
[ {
  "workerFirstName" : "workerFirstName",
  "ticketsUrl" : "http://example.com/aeiou",
  "workerSecondName" : "workerSecondName",
  "workerEmail" : "workerEmail",
  "endDate" : "2000-01-23",
  "approverId" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
  "destination" : {
    "description" : "description",
    "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
    "office" : {
      "address" : "address",
      "description" : "description",
      "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
    },
    "seatPlace" : "seatPlace"
  },
  "description" : "description",
  "comment" : "comment",
  "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
  "requestStatus" : "approved",
  "startDate" : "2000-01-23"
}, {
  "workerFirstName" : "workerFirstName",
  "ticketsUrl" : "http://example.com/aeiou",
  "workerSecondName" : "workerSecondName",
  "workerEmail" : "workerEmail",
  "endDate" : "2000-01-23",
  "approverId" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
  "destination" : {
    "description" : "description",
    "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
    "office" : {
      "address" : "address",
      "description" : "description",
      "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
    },
    "seatPlace" : "seatPlace"
  },
  "description" : "description",
  "comment" : "comment",
  "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
  "requestStatus" : "approved",
  "startDate" : "2000-01-23"
} ]
```  
- GET /{uuid}/incoming-requests-at/{page}/{status}  (Roles: USER, ADMIN)  
**Получение страницы номер {page}(либо все сразу если не указано) из n запросов для ментора с заданным {id} и статусом {status}.** (Roles: USER, ADMIN)  
Параметры:  
uuid: UUID (обязательно)  
page: int (не обязательно)  
status: RequestStatus (обязательно)  
Возвращаемый тип: array[Request]  
Данные возвращаются в формате *application/json*  
Пример:  
```
[ {
  "workerFirstName" : "workerFirstName",
  "ticketsUrl" : "http://example.com/aeiou",
  "workerSecondName" : "workerSecondName",
  "workerEmail" : "workerEmail",
  "endDate" : "2000-01-23",
  "approverId" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
  "destination" : {
    "description" : "description",
    "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
    "office" : {
      "address" : "address",
      "description" : "description",
      "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
    },
    "seatPlace" : "seatPlace"
  },
  "description" : "description",
  "comment" : "comment",
  "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
  "requestStatus" : "approved",
  "startDate" : "2000-01-23"
}, {
  "workerFirstName" : "workerFirstName",
  "ticketsUrl" : "http://example.com/aeiou",
  "workerSecondName" : "workerSecondName",
  "workerEmail" : "workerEmail",
  "endDate" : "2000-01-23",
  "approverId" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
  "destination" : {
    "description" : "description",
    "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
    "office" : {
      "address" : "address",
      "description" : "description",
      "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
    },
    "seatPlace" : "seatPlace"
  },
  "description" : "description",
  "comment" : "comment",
  "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
  "requestStatus" : "approved",
  "startDate" : "2000-01-23"
} ]
```
- GET /users/{uuid}/outgoing-requests-at/{page} (Roles: USER, ADMIN)  
**Получение страницы номер {page}(либо все сразу если не указано) из n запросов для сотрудника с заданным {id}.**
Параметры:  
uuid: UUID (обязательно)  
page: int (не обязательно)  
Возвращаемый тип: array[Request]  
Данные возвращаются в формате *application/json*  
Пример:  
```
[ {
  "workerFirstName" : "workerFirstName",
  "ticketsUrl" : "http://example.com/aeiou",
  "workerSecondName" : "workerSecondName",
  "workerEmail" : "workerEmail",
  "endDate" : "2000-01-23",
  "approverId" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
  "destination" : {
    "description" : "description",
    "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
    "office" : {
      "address" : "address",
      "description" : "description",
      "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
    },
    "seatPlace" : "seatPlace"
  },
  "description" : "description",
  "comment" : "comment",
  "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
  "requestStatus" : "approved",
  "startDate" : "2000-01-23"
}, {
  "workerFirstName" : "workerFirstName",
  "ticketsUrl" : "http://example.com/aeiou",
  "workerSecondName" : "workerSecondName",
  "workerEmail" : "workerEmail",
  "endDate" : "2000-01-23",
  "approverId" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
  "destination" : {
    "description" : "description",
    "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
    "office" : {
      "address" : "address",
      "description" : "description",
      "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
    },
    "seatPlace" : "seatPlace"
  },
  "description" : "description",
  "comment" : "comment",
  "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
  "requestStatus" : "approved",
  "startDate" : "2000-01-23"
} ]
```
- GET /{uuid}/outgoing-requests-at/{page}/{status} (Roles: USER, ADMIN)  
**Получение страницы номер {page}(либо все сразу если не указано) из n запросов для сотрудника с заданным {id} и статусом {status}.**  
Параметры:  
uuid: UUID (обязательно)  
page: int (не обязательно)  
status: RequestStatus (обязательно)  
Возвращаемый тип: array[Request]  
Данные возвращаются в формате *application/json*  
Пример:  
```
[ {
  "workerFirstName" : "workerFirstName",
  "ticketsUrl" : "http://example.com/aeiou",
  "workerSecondName" : "workerSecondName",
  "workerEmail" : "workerEmail",
  "endDate" : "2000-01-23",
  "approverId" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
  "destination" : {
    "description" : "description",
    "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
    "office" : {
      "address" : "address",
      "description" : "description",
      "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
    },
    "seatPlace" : "seatPlace"
  },
  "description" : "description",
  "comment" : "comment",
  "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
  "requestStatus" : "approved",
  "startDate" : "2000-01-23"
}, {
  "workerFirstName" : "workerFirstName",
  "ticketsUrl" : "http://example.com/aeiou",
  "workerSecondName" : "workerSecondName",
  "workerEmail" : "workerEmail",
  "endDate" : "2000-01-23",
  "approverId" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
  "destination" : {
    "description" : "description",
    "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
    "office" : {
      "address" : "address",
      "description" : "description",
      "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
    },
    "seatPlace" : "seatPlace"
  },
  "description" : "description",
  "comment" : "comment",
  "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
  "requestStatus" : "approved",
  "startDate" : "2000-01-23"
} ]
```  
- GET /{uuid}/trips-at/{page}/{status} (Roles: USER, ADMIN)  
**Получение страницы номер {page}(либо все сразу если не указано) из n командировок для пользователя с заданным {id} и статусом {status}.**  
Параметры:  
uuid: UUID (обязательно)  
page: int (не обязательно)  
status: RequestStatus (обязательно)  
Возвращаемый тип: array[Trip]  
Данные возвращаются в формате *application/json*  
Пример:  
```
[ {
  "endDate" : "2000-01-23",
  "requestId" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
  "accommodation" : {
    "address" : "address",
    "bookingUrl" : "http://example.com/aeiou",
    "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
  },
  "destination" : {
    "description" : "description",
    "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
    "office" : {
      "address" : "address",
      "description" : "description",
      "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
    },
    "seatPlace" : "seatPlace"
  },
  "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
  "startDate" : "2000-01-23",
  "tripStatus" : "pending"
}, {
  "endDate" : "2000-01-23",
  "requestId" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
  "accommodation" : {
    "address" : "address",
    "bookingUrl" : "http://example.com/aeiou",
    "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
  },
  "destination" : {
    "description" : "description",
    "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
    "office" : {
      "address" : "address",
      "description" : "description",
      "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
    },
    "seatPlace" : "seatPlace"
  },
  "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
  "startDate" : "2000-01-23",
  "tripStatus" : "pending"
} ]
```
- GET /users/{uuid}/trips-at/{page} (Roles: USER, ADMIN)  
**Получение страницы номер {page}(либо все сразу если не указано) из n командировок для пользователя с заданным {id}.**  
Параметры:  
uuid: UUID (обязательно)  
page: int (не обязательно)  
Возвращаемый тип: array[Trip]  
Данные возвращаются в формате *application/json*  
Пример:  
```
[ {
  "endDate" : "2000-01-23",
  "requestId" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
  "accommodation" : {
    "address" : "address",
    "bookingUrl" : "http://example.com/aeiou",
    "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
  },
  "destination" : {
    "description" : "description",
    "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
    "office" : {
      "address" : "address",
      "description" : "description",
      "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
    },
    "seatPlace" : "seatPlace"
  },
  "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
  "startDate" : "2000-01-23",
  "tripStatus" : "pending"
}, {
  "endDate" : "2000-01-23",
  "requestId" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
  "accommodation" : {
    "address" : "address",
    "bookingUrl" : "http://example.com/aeiou",
    "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
  },
  "destination" : {
    "description" : "description",
    "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
    "office" : {
      "address" : "address",
      "description" : "description",
      "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
    },
    "seatPlace" : "seatPlace"
  },
  "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
  "startDate" : "2000-01-23",
  "tripStatus" : "pending"
} ]
```
- GET /users/{uuid}/unwatched-notifications (Roles: USER, ADMIN)  
**Получить список непросмотренных уведомлений пользователя по UUID.**  
Параметры:  
uuid: UUID (обязательно)  
Возвращаемый тип: array[Notification]  
Данные возвращаются в формате *application/json*  
Пример:  
```
[ {
  "request" : {
    "workerFirstName" : "workerFirstName",
    "ticketsUrl" : "http://example.com/aeiou",
    "workerSecondName" : "workerSecondName",
    "workerEmail" : "workerEmail",
    "endDate" : "2000-01-23",
    "approverId" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
    "destination" : {
      "description" : "description",
      "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
      "office" : {
        "address" : "address",
        "description" : "description",
        "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
      },
      "seatPlace" : "seatPlace"
    },
    "description" : "description",
    "comment" : "comment",
    "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
    "requestStatus" : "approved",
    "startDate" : "2000-01-23"
  },
  "watched" : true,
  "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
  "userId" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
}, {
  "request" : {
    "workerFirstName" : "workerFirstName",
    "ticketsUrl" : "http://example.com/aeiou",
    "workerSecondName" : "workerSecondName",
    "workerEmail" : "workerEmail",
    "endDate" : "2000-01-23",
    "approverId" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
    "destination" : {
      "description" : "description",
      "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
      "office" : {
        "address" : "address",
        "description" : "description",
        "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
      },
      "seatPlace" : "seatPlace"
    },
    "description" : "description",
    "comment" : "comment",
    "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
    "requestStatus" : "approved",
    "startDate" : "2000-01-23"
  },
  "watched" : true,
  "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
  "userId" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
} ]
```
- GET /users/{uuid} (Roles: USER, ADMIN)  
**Получение пользователя по его {id}.**  
Параметры:  
uuid: UUID (обязательно)  
Возвращаемый тип: User  
Данные возвращаются в формате *application/json*  
Пример:  
```
{
  "firstName" : "firstName",
  "password" : "password",
  "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91",
  "userRole" : "userRole",
  "subordinates" : [ null, null ],
  "email" : "email",
  "secondName" : "secondName"
}
```
- PUT /users (Roles: ADMIN)  
**Обновление данных пользователя**  
Принимает на вход данные в формате *application/json*   
Передаваемый тип: User user (обязательно)  
- DELETE /users/{uuid} (Roles: ADMIN)  
**Удаление пользователя по заданному UUID.**  
Параметры: uuid: UUID (обязательно)  

## **Модели**
### 1. Accommodation
- id: UUID
- address: String
- bookingUrl: String
### 2. Destination
- id: UUID
- description: String
- office: Office
- seatPlace: String
### 3. DestinationDto
- id: UUID
- description: String
- officeId: UUID
- seatPlace: String
### 4. Id
- id: UUID
### 5. LoginRequest
- email: String
- password: UUID
### 6. Notification
- id: UUID
- request: Request
- watched: Boolean
- userId: UUID
### 7. NotificationDto
- id: UUID
- requestId: UUID
- watched: Boolean
- userId: UUID
### 8. Office
- id: UUID
- address: String
- description: String
### 9. Request
- id: UUID
- requestStatus: String (Enum of: approved, pending, awaitChanges, declined)
- description: String
- workerFirstName: String
- workerSecondName: String
- workerEmail: String
- destination: Destination
- comment: String
- approvedId: UUID
- startDate: date
- endDate: date
- ticketsUrl: String
### 10. RequestDto
- id: UUID
- description: String
- workerId: UUID
- destinationId: UUID
- comment: String
- startDate: date
- endDate: date
- ticketsUrl: String
### 11. RequestStatusChangeDto
- approverId: UUID
- tripDto: TripDto
- comment: String
### 12. Trip
- id: UUID
- tripStatus: String (Enum of: pending, completed, cancelled)
- accommodation: Accommodation
- destination: Destination
- requestId: UUID
- startDate: date
- endDate: date
### 13. TripDto
- id: UUID
- tripStatus: String (Enum of: pending, completed, cancelled)
- accommodationId: UUID
- destinationId: UUID
- requestId: UUID
### 14. User
- id: UUID
- email: String
- password: String
- firstName: String
- secondName: String
- subordinates: array[User]
- userRole: String
