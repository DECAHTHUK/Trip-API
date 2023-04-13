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
### 1. **Accommodation's endpoints**
- POST /accommodations  
**Создание места размещения**  
Принимает на вход application/json  
RequestBody: Accommodation Accommodation (required)  
Return type: Id  
Example data:  
Content-Type: application/json
```
{
  "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
}
```
- DELETE /accommodations/{uuid}  
**Удаление места размещения**  
Path parameters:  
uuid (required)  
Path Parameter — default: null format: uuid  
- GET /accommodations/{uuid}  
**Получить место размещения** (getAccommodation)  
Path parameters:  
uuid (required)  
Path Parameter — default: null format: uuid  
Return type:  
Accommodation
Example data:  
Content-Type: application/json  
```
{
  "address" : "address",
  "bookingUrl" : "http://example.com/aeiou",
  "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
}
```
- PUT /accommodations  
**Обновление места размещения** (updateAccommodation)  
Consumes  
This API call consumes the following media types via the Content-Type request header:  
application/json  
Request body  
Accommodation Accommodation (required)  
### 2. Destination's endpoints
- POST /destinations  
**Destination creation** (createNewDestination)  
Consumes: application/json  
Request body: DestinationDto DestinationDto (required)  
Return type: Id  
Content-Type: application/json  
```
{
  "id" : "046b6c7f-0b8a-43b9-b35d-6489e6daee91"
}
```
- DELETE /destinations/{uuid}  
**Delete a destination** (deleteDestination)  
Path parameters:  
uuid (required)  
Path Parameter — default: null format: uuid  
- GET /destinations/{uuid}
**Get a destination** (getDestination)  
Path parameters:  
uuid (required)  
Path Parameter — default: null format: uuid  
Return type: Destination  
Content-Type: application/json  
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
**Destination update** (updateDestination)  
Consumes: application/json  
Request body: DestinationDto DestinationDto (required)
