# Kakaopay 뿌리기 API 구현하기

## 요구사항

- 뿌리기, 받기, 조회 기능을 수행하는 REST API 구현
    - 요청한 사용자의 식별값은 숫자 형태이며 `X-USER-ID` 라는 HTTP Header로 전달됨
    - 요청한 사용자가 속한 대화방의 식별값은 문자 형태이며 `X-ROOM-ID` 라는 HTTP Header로 전달 됨
    - 모든 사용자는 뿌리기에 충분한 잔액을 보유하고 있다고 가정하여 ~~별도로 잔액에 관련된 체크는 하지 않습니다.~~
- 작성한 어플리케이션이 다수의 서버에 다수의 인스턴스로 동작하더라도 기능에 문제가 없도록 설계되어야 함. → concurrency
- **각 기능 및 제약사항에 대한 단위 테스트를 반드시 작성함**

## 상세

1. 뿌리기 API
    - 다음 조건을 만족하는 뿌리기 API를 만들어주세요.
        - `뿌릴 금액`, `뿌릴 인원`을 요청값으로 받습니다.
        - 뿌리기 요청건에 대한 고유 token을 발급하고 응답값으로 내려줍니다.
        - 뿌릴 금액을 인원수에 맞게 분배하여 저장합니다. (분배 로직은 자유롭게)
        - **token은 3자리 문자열로 구성되며 예측이 불가능해야 됨.**

2. 받기 API
    - 뿌리기 시 발급된 token을 요청값으로 받습니다.
    - token에 해당하는 뿌리기 건 중 아직 누구에게도 할당되지 않은 분배건 하나를 API를 호출한 사용자에게 할당하고, 그 금액을 응답값으로 내려줍니다.
    - 뿌리기 당 한 사용자는 한번만 받을 수 있습니다.
    - 자신이 뿌리기한 건은 자신이 받을 수 없습니다.
    - 뿌리기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수 있습니다.
    - 뿌린건은 10분간만 유효합니다. **뿌린지 10분이 지난 요청에 대해서는 받기 실패 응답이 내려가야 됨.**

3. 조회 API
    - 뿌리기 시 발급된 token을 요청값으로 받습니다.
    - token에 해당하는 뿌리기 건의 현재 상태를 응답값으로 내려줍니다. 현재 상태는 아래의 정보를 포함함
    - `뿌린 시각`, `뿌린 금액`, `받기 완료된 금액`, `받기 완료된 정보 ([받은 금액, 받은 사용자 아이디] 리스트)`
    - 뿌린 사람 자신만 조회를 할 수 있습니다. 다른사람의 뿌리기 건이나 유효하지 않은 token에 대해서는 조회 실패 응답이 내려가야 됨.
    - **뿌린 건에 대한 조회는 7일 동안 할 수 있음.**

## 주요 문제 해결 전략

1. 다수의 서버에 다수의 인스턴스로 동작하더라도 기능에 문제가 없도록 설계

## 개발도구 및 환경

- OS : Window, OS X
- IDE : IntelliJ IDEA
- Java 8
- Spring Boot
- Maven
- JPA + Hibernate
- MariaDB

## 실행

```bash
mvn spring-boot:run
```

## 데이터베이스 생성

```sql
CREATE DATABASE KAKAOPAY;
```

## API Reference

### 뿌리기 생성

- Request

```
POST /v1/sprinkle

Host: localhost:8080
Content-type: application/json;charset=utf-8
X-ROOM-ID: {ROOM_ID}
X-USER-ID: {USER_ID}
```

- Parameter

|Name|Type|Description|Required|
|:---:|:---:|:---|:---:|
|money|`Number`|뿌리기 금액|O|
|division|`Number`|뿌리기 인원|O|

- Response

```
{
  "code": 0,
  "message": "정상처리",
  "data": "Kaf"
}
```

|Name|Type|Description|
|:---:|:---:|:---|
|code|`Number`|결과 코드|
|message|`String`|결과 메세지|
|data|`String`|뿌리기 생성시 발급된 토큰|

### 뿌리기 줍기

- Request

```
PUT /v1/sprinkle/{token}

Host: localhost:8080
X-ROOM-ID: {ROOM_ID}
X-USER-ID: {USER_ID}
```

- Parameter

|Name|Type|Description|
|:---:|:---:|:---|
|token|`String`|뿌리기 생성시 발급된 토큰|

- Response

```
{
  "code": 0,
  "message": "정상처리",
  "data": 818
}
```

|Name|Type|Description|
|:---:|:---:|:---|
|code|`Number`|결과 코드|
|message|`String`|결과 메세지|
|data|`Number`|줍기 성공한 금액|

### 뿌리기 조회

- Request

```
GET /v1/sprinkle/{token}

Host: localhost:8080
X-ROOM-ID: {ROOM_ID}
X-USER-ID: {USER_ID}
```

- Parameter

|Name|Type|Description|
|:---:|:---:|:---|
|token|`String`|뿌리기 생성시 발급된 토큰|

- Response

```
{
  "code": 0,
  "message": "정상처리",
  "data": {
    "money": 1000,
    "create_at": "20201119131512",
    "pickup_money: 700,
    "pickup_list": [
      {
        "money": 655,
        "user_id": 123
      },
      {
        "money": 45,
        "user_id": 456
      }
    ]
  }
}
```

|Name|Type|Description|
|:---:|:---:|:---|
|code|`Number`|결과 코드|
|message|`String`|결과 메세지|
|data|`Object`|뿌리기 정보|

##### Object

|Name|Type|Description|
|:---|:---:|:---|
|money|`Number`|뿌린 금액|
|create_at|`Number`|뿌린 일시|
|pickup_money|`Number`|총 주운 금액|
|pickup_list|`Array`|줍기 처리된 목록|
|pickup_list.money|`Number`|주운 금액|
|pickup_list.user_id|`Number`|주운 사용자 ID|

- 결과 코드

|Code|Description|
|:---|:---|
|0|정상처리|