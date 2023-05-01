# Trip-API (API сервис командировок)
Здесь представлена реализация API сервиса для оформления командировок.
### 1. **Какова задача этого проекта?**
Задача проекта в том, чтобы упростить взаимдействие между начальником и работником в случае оформления командировок.
### 2. **Функционал**
- Создание связей между начальниками и подчиненными.
- Оформление командировок.
- Создание уведомлений для оптимального рассмотрения заявок.
### 3. **Стек технологий**
- Spring boot
- PostgreSQL
- MyBatis
- Liquibase
- Spring Security
- MockMVC
- Docker
## **Методы API**



### 1. **Accommodation's endpoints** (Roles: USER, ADMIN)  
- POST /accommodations  
**Создание места размещения**  
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
**Удаление места размещения по id**  
Параметры: uuid (обязательно)      
- GET /accommodations/{uuid}  
**Получить место размещения по id**    
параметры: uuid (обязательно)      
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
**Обновление места размещения**    
Принимает на вход данные в формате *application/json*   
Передаваемый тип: Accommodation accommodation (обязательно)   



### 2. Destination's endpoints (Roles: USER, ADMIN)  
- POST /destinations  
**Создание места назначения**    
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
**Удаление места назначения по id**  
Параметры: uuid (обязательно)    
- GET /destinations/{uuid}  
**Получение места назначения по id**  
параметры: uuid (обязательно)      
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
**Обновление места назначения**    
Принимает на вход данные в формате *application/json*   
Передаваемый тип: DestinationDto destinationDto (обязательно)  



### 3. Login's endpoint (Авторизация не нужна)  
- POST /api/login  
**Аутентификация**  
Принимает на вход данные в формате *application/json*   
Передаваемый тип: LoginRequest LoginRequest (обязательно)  
Возвращаемый тип: String    



### 4. Notification's endpoints (Roles: USER, ADMIN)  
- GET /notifications/{uuid}  
**Получение уведомления по id**  
параметры: uuid (обязательно)      
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
**Изменение статуса уведомления по id на просмотренный**  
параметры: uuid (обязательно)  



### 5. Office's endpoints (Roles: ADMIN)
- POST /offices  
**Создание офиса**    
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
**Удаление офиса по id**  
Параметры: uuid (обязательно)    

- GET /offices/{uuid}  
**Получение офиса по id**  
параметры: uuid (обязательно)      
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
**Обновление данных об офисе**  
Принимает на вход данные в формате *application/json*   
Передаваемый тип: Office office (обязательно)  
