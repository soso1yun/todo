### AI Assistant prompt 및 결과

1. API 아이디어 질문 프롬프트
```text
Todo 관련된 API 를 만들고 있어 아직 구상된 내용은 없고 API 항목만 구성 되어 있어
어떤걸 만들면 좋을지 추천해줘 아래는 API 항목이야

○ POST `/todos`
○ GET `/todos`
○ GET /todos/{id}
○ PUT /todos/{id}
○ DELETE /todos/{id}
○ GET /todos/search
```


2. 회원 테스트 코드 작성 프롬프트
```text
테스트 코드를 작성해야해 아래는 조건이야

- 회원가입->로그인->JWT 인증 흐름 테스트
- 유효하지 않은 ID로 접근시 404 확인
- 단위 테스트 또는 통합테스트 작성
- 테스트 코드 커버리지 80% 이상

내 컨트롤러와 서비스를 확인해서 작성해줘

```

3. 날짜 정규식 요청 
```text
YYYY-MM-dd 정규식 만들어줘
```

4. 정규식 문의
```text
정규식을 만들건대 ^(대기|긴급|진행중|완료)$ 이렇게 하면 4개 단어만 사용 가능해?
```

5. valid 문의
```text
`jakarta.validation.Valid` 에서 값이 있으면 정규식 기준으로 체크하고 아니라면 null 또는  "" 을 받을수 있게 할수 있어?
```

6. todo 테스트 코드 작성 프롬프트 
```text

// TodoService 파일 첨부
// todo.html 파일 첨부
이정보들 기반으로 통합 테스트 작성해줘

```


7. READ.MD 작성 프롬프트
```text
1. JWT 발급 및 검증 흐름 다이어그램 내소스를 확인해서 만들어줘 (SecurityConfig)
2. http 파일들을 참고해서 API 명세 요약해서 마크다운 형태로 작성해줘
3. 빌드 및 환경 정보 gradle 파일 참고해서 만들어줘
```