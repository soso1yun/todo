## JWT 기반 TODO 백엔드 API 구현 과제

### 1. 환경 구성 및 실행 방법

### 기본 프로젝트 구성 정보

- JDK21
- Spring boot 3.4.5
- JPA
- sqlite

### **Prerequisites**
- Java Development Kit (JDK) 21 설치 필요
- Gradle 설치 필요 (`./gradlew` 명령어를 사용하면 설치된 Gradle이 필요하지 않음)
- 인터넷 연결 (의존성 다운로드를 위해)

### **실행 명령**
Spring Boot 애플리케이션을 실행하려면 아래 명령을 터미널에서 실행하세요:

#### **Gradle 빌드 및 실행**
```bash
./gradlew bootRun
```

#### **Spring Boot 애플리케이션 실행**
프로젝트 루트 디렉토리에서 Gradle Wrapper를 사용합니다:

```bash
./gradlew bootRun
```

#### **또는 직접 빌드 후 JAR 실행**
1. **JAR 빌드**:
   ```bash
   ./gradlew build
   ```
   성공적으로 빌드되면, `build/libs/<프로젝트명>.jar` 파일이 생성됩니다.

2. **JAR 파일 실행**:
   ```bash
   java -jar build/libs/<project-name>-0.0.1-SNAPSHOT.jar
   ```
   여기서 `<project-name>`은 `build.gradle` 파일에서 설정된 프로젝트 이름입니다 (`com.llm`).

3. **애플리케이션 URL**:
   기본적으로 애플리케이션은 `[http://localhost:8080](http://localhost:8080)`에서 실행됩니다.


#### **테스트 코드 실행 방법 및 커버리지 결과 확인**
테스트 파일 경로 java/com/llm/todo/UserControllerTest.java

**테스트 코드 커버리지는 결과는 htmlReport/index.html 파일을 브라우저에서 열어 확인할 수 있습니다. 



#### SQLite 초기화 코드
JPA 사용, create-drop 모드 사용 중입니다. 

---
### 2. API 명세 요약

### **1. 회원 관련 API**

#### **1.1 회원가입**
```http
POST http://localhost:8080/users/signup
Content-Type: application/json

{
  "userName": "test",      // 사용자 이름
  "userId": "test2",       // 사용자 ID
  "password": "admin1234"  // 비밀번호
}
```


#### **1.2 로그인**
```http
POST http://localhost:8080/users/login
Content-Type: application/json

{
  "userId": "test",        // 사용자 ID
  "password": "admin1234"  // 비밀번호
}
```

#### **1.3 회원 정보 조회**
```http
GET http://localhost:8080/users/me
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MiwidXNlcklkIjoidGVzdDEiLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNzQ3MDQ5MTc0LCJleHAiOjE4NzAyMDM0ODd9.GJSKb0gvw1Q1c4URhi26uyfzts5jpyRLJrYflTCd4ZE
```


#### **1.4 회원 정보 수정**
```http
PUT http://localhost:8080/users/me
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MiwidXNlcklkIjoidGVzdDEiLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNzQ3MDQ5MTc0LCJleHAiOjE4NzAyMDM0ODd9.GJSKb0gvw1Q1c4URhi26uyfzts5jpyRLJrYflTCd4ZE

{
  "userName": "newName",
  "password": "admin12345"
}
```


#### **1.5 회원 삭제**
```http
DELETE http://localhost:8080/users/me
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MiwidXNlcklkIjoidGVzdDEiLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNzQ3MDQ5MTc0LCJleHAiOjE4NzAyMDM0ODd9.GJSKb0gvw1Q1c4URhi26uyfzts5jpyRLJrYflTCd4ZE
```


#### **2. TODO 관련 API**

#### **2.1 TODO 등록**
```http
POST http://localhost:8080/todos
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlck5hbWUiOiJ0ZXN0IiwidXNlcklkIjoidGVzdCIsInJvbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE3NDcxNDY5MTQsImV4cCI6MTg3MDMwMTIyN30.vd724q2VdgQ_0UNs9IRqtEFXTrQrzOqsy8WQ-GMCrec

{
  "title": "title1",            // 제목
  "description": "description22", // 상세 설명
  "dueDate": "2025-05-15",      // 마감일
  "status": "대기"              // 상태
}
```


#### **2.2 TODO 목록 조회**
```http
GET http://localhost:8080/todos
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlck5hbWUiOiJ0ZXN0IiwidXNlcklkIjoidGVzdCIsInJvbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE3NDcxNDY5MTQsImV4cCI6MTg3MDMwMTIyN30.vd724q2VdgQ_0UNs9IRqtEFXTrQrzOqsy8WQ-GMCrec
```

#### **2.3 TODO ID 기준 조회**
```http
GET http://localhost:8080/todos/2
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlck5hbWUiOiJ0ZXN0IiwidXNlcklkIjoidGVzdCIsInJvbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE3NDcxNDY5MTQsImV4cCI6MTg3MDMwMTIyN30.vd724q2VdgQ_0UNs9IRqtEFXTrQrzOqsy8WQ-GMCrec
```


#### **2.4 TODO ID 기준 수정**
```http
PUT http://localhost:8080/todos/2
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlck5hbWUiOiJ0ZXN0IiwidXNlcklkIjoidGVzdCIsInJvbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE3NDcxNDY5MTQsImV4cCI6MTg3MDMwMTIyN30.vd724q2VdgQ_0UNs9IRqtEFXTrQrzOqsy8WQ-GMCrec

{
  "title": "title2",
  "description": "description1",
  "dueDate": "2025-05-12",
  "status": "대기"
}
```


#### **2.5 TODO ID 기준 삭제**
```http
DELETE http://localhost:8080/todos/5
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlck5hbWUiOiJ0ZXN0IiwidXNlcklkIjoidGVzdCIsInJvbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE3NDcxNDY5MTQsImV4cCI6MTg3MDMwMTIyN30.vd724q2VdgQ_0UNs9IRqtEFXTrQrzOqsy8WQ-GMCrec
```


#### **2.6 TODO 검색**
```http
GET http://localhost:8080/todos/search
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlck5hbWUiOiJ0ZXN0IiwidXNlcklkIjoidGVzdCIsInJvbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE3NDcxNDY5MTQsImV4cCI6MTg3MDMwMTIyN30.vd724q2VdgQ_0UNs9IRqtEFXTrQrzOqsy8WQ-GMCrec

{
  "searchText": "ti",             // 검색어 (타이틀/설명)
  "startDueDate": "2025-05-12",   // 시작 마감일
  "endDueDate": "2025-05-15",     // 종료 마감일
  "status": ""                    // TODO 상태
}
```


#### **인증**
모든 요청 중 토큰이 필요한 API는 반드시 `Authorization` 헤더에 JWT를 포함해야 합니다.

- **Authorization 헤더 형식**:
  ```
  Authorization: Bearer <JWT_TOKEN>
  ```

JWT 토큰은 로그인 요청(`/users/login`)을 통해 발급받을 수 있습니다.


---
### 3. JWT 발급 및 검증 흐름 다이어그램

### **WT 발급 및 검증 흐름**
#### 1. **JWT 발급 흐름**
1. 사용자가 `/users/login` API 엔드포인트로 **ID와 비밀번호**를 전송.
2. **Spring Security**는 `UserDetailsService`를 통해 사용자의 인증 정보를 조회.
3. 인증 성공 시, `JwtUtil`을 사용하여 사용자 정보를 기반으로 JWT 생성.
4. 생성된 JWT를 클라이언트에게 반환.

#### 2. **JWT 검증 흐름**
1. 클라이언트는 인증이 필요한 API 요청 시, HTTP 헤더 (`Authorization`)에 JWT를 포함하여 전송.
2. `JwtAuthFilter`는 요청의 HTTP 헤더에서 JWT를 추출.
3. 추출된 JWT를 `JwtUtil`을 통해 검증 (서명 및 유효기간 등 확인).
4. 검증 성공 시, **Spring Security Context**에 인증 사용자 정보를 저장.
5. 최종적으로 요청 처리.



```text
                                   +---------------------------+
                                   |        클라이언트          |
                                   +-----------+---------------+
                                               |
                                    1. 로그인 요청 (/users/login)
                                               |
                                               v
                           +-------------------+--------------------+
                           |   Spring Security (Authentication)    |
                           +-------------------+--------------------+
                                               |
                             인증 성공                    인증 실패
                               |                            |
                    2. JwtUtil로 JWT 생성          예외 처리 (Unauthorized)
                               |
                               v
                  +---------------------------+
                  | 클라이언트에 JWT 반환     |
                  +---------------------------+
                               |
               +---------------+---------------+
               | 인증 필요한 요청              |
               v                               v
    (JWT 포함) HTTP 요청                인증 필요 없는 요청
                               |
                               v
               +---------------------------+
               |        JwtAuthFilter      |
               +---------------------------+
                               |
                JWT 추출 및 JwtUtil을 통해 검증
                               |
                 검증 성공             검증 실패
                   |                        |
        Spring Security Context       예외 처리 (Unauthorized/Forbidden)
              저장                           |
                   |                        |
                요청 처리               에러 반환
```
### 흐름에서 사용된 컴포넌트

1. **`UserDetailsService`**
    - 사용자 인증 정보 조회를 담당.

2. **`JwtUtil`**
    - JWT 생성 및 검증 로직을 제공.

3. **`JwtAuthFilter`**
    - HTTP 요청에서 JWT를 검증하는 필터.

4. **`SecurityConfig`**
    - Spring Security 설정 (필터 체인, 권한 정책 등).
