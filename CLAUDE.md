@../../CLAUDE.md
@../../docs/ssot/org-principles.md
@../../docs/ssot/repo-architecture.md

# auth-api (fabrics-auth-api)

회원가입, 로그인, 계정 연결, 세션을 관리하는 fabrics 공통 회원 서버다. 외부 제공자
(Google, Apple, Kakao, Naver, GitHub)에게 로그인을 위임하고 자체 이메일·비밀번호
로그인도 지원한다. 조직 공통 원칙은 위 `@import`로 상속하며, 이 파일은 프로젝트
고유 사항만 기술한다.

## 참조 문서
- 결정 사항과 진행 상황: `../../docs/auth-api-todo.md`
- 첫 소비자(app-pocket)가 요구하는 인증·세션 설계: `../../docs/app-pocket-todo.md` "인증과 세션" 절
- 앱 정보 헤더(`X-App-Id`, `X-App-Version`, `X-App-Platform`): `../../docs/architecture/app-pocket-bridge.md` 7절

## 스택
| 영역 | 선택 |
|---|---|
| 언어 | Kotlin, Java 21 toolchain |
| 프레임워크 | Spring Boot, Spring Security OAuth2 Client, Spring MVC |
| 데이터 | PostgreSQL, Spring Data JPA, Flyway |
| 빌드 | Gradle Kotlin DSL, Gradle Wrapper (`./gradlew`) |
| 로컬 DB | Spring Boot Docker Compose Support (`compose.yaml`, `postgres:18`) |
| 테스트 | JUnit 5, Testcontainers PostgreSQL |
| 버전 | Spring Initializr 생성 값을 따른다 |

- 기본 패키지는 `com.fabrics.auth`다
- 실행: `./gradlew bootRun` (Docker 실행 필요). Testcontainers 기반 실행은 `./gradlew bootTestRun`
- 스키마 변경은 Flyway 마이그레이션(`src/main/resources/db/migration`)으로만 한다

## 역할 경계
- OIDC 제공자(OP)가 아니다. 외부 제공자에 대해서는 OAuth 2.0/OIDC client이고,
  fabrics 서비스에는 자체 세션·토큰 API를 제공한다
- 회원 공통 정보(이메일, 닉네임, 프로필 이미지)만 보관한다. 서비스 고유 데이터는 각 서비스가 갖는다
- 소비자와의 계약(엔드포인트, 토큰 형식, 오류 코드)을 바꾸는 변경은 `../../docs/`의
  관련 문서를 함께 갱신한다

## 보안 규칙
- 제공자 client secret, 서명 키, DB 비밀번호 등 비밀값은 저장소에 커밋하지 않는다.
  환경 변수로 주입한다. `compose.yaml`의 DB 값은 로컬 전용이다
- 소셜 계정은 `(제공자, 제공자 고유 ID)`로 식별한다. 이메일로 계정을 찾거나 자동 연결하지 않는다
- 프로토콜 처리(OAuth 흐름, state·PKCE 검증, 토큰 서명)는 Spring Security와 검증된
  라이브러리를 쓰고 직접 구현하지 않는다
- 토큰, 비밀번호, 인가 코드는 로그에 남기지 않는다

## todo 관리
작업이 진행되면 루트 `../../docs/auth-api-todo.md`를 실시간으로 갱신한다.
구조와 릴리스 시 처리는 `../../docs/TODO.md`의 "프로젝트 todo 파일" 절을 따른다.
시행착오는 기록하지 않고 최종 결정만 남긴다.
