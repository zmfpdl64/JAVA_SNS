[My Blog write to Redis](https://zmfpdl64.tistory.com/3)<br>
[My Blog write to WithMockUser](https://zmfpdl64.tistory.com/4)

## 👨‍🔧BackEnd 기술 스택
 - Jdk 17
 - Springboot version: '3.0.5'
 - Spring Security
 - Spring Redis
 - Mysql
 - H2
 
## 📰 ERD
![image](https://github.com/zmfpdl64/JAVA_SNS/assets/69797420/9e237548-cce4-4c99-9e98-a847f60ee8c9)



# 로그인 회원 가입 기능

1. 회원 가입 기능 구현
   - [x] TDD 작성 
   - [x] 정상적인 회원 가입
   - [x] [**예외 발생**] 기존 ID 존재 
   - [x] 기능 구현s
   - [x] TDD 성공
```mermaid
sequenceDiagram
    autonumber
    client ->> server : 회원가입 요청
    alt 아이디가 중복된 경우
    server -->> client : reason code와 함께 실패 반환
    else 성공한 경우 
    server ->> client : 성공 반환
    end
```
2. 로그인 기능 구현
   - [x] TDD 작성
     - [x] 서비스 로직 작성
     - [x] 컨트롤러 로직 작성
   - [x] 정상적인 로그인
   - [x] **[예외처리]** 저장되어있는 ID 존재하지 않음
   - [x] **[예외처리]** 비밀번호가 일치하지 않음
   - [x] TDD 성공
```mermaid
sequenceDiagram
    autonumber
    client ->> server : 로그인 요청
    alt 아이디가 존재하지 않을 경우
    server -->> client : reason code와 함께 실패 반환
    else 비밀번호가 존재하지 않을 경우
    server -->> client : reason code와 함께 실패 반환
    else 성공한 경우 
    server ->> client : 성공 반환
    end
```
# 게시글 기능

1. 게시글 작성 기능 구현
   - [x] 토큰 유효성 검증
   - [x] 토큰 유효하지 않을경우 **[예외처리]**
   - [X] TDD 작성
   - [x] 인증이 된 사용자 게시글 작성
   - [x] **[예외처리]** 인증안된 사용자 게시글 작성 
   - [x] TDD 성공
```mermaid
sequenceDiagram
    autonumber
    client ->> server : 포스트 작성 요청
    alt 아이디가 존재하지 않을 경우
    server ->> client : reason code와 함께 실패 반환
    else 비밀번호가 존재하지 않을 경우
    server -->> client : reason code와 함께 실패 반환
    else 성공한 경우 
    server ->> db : 포스트 저장 요청
    db -->> server : 저장 성공 반환
    server ->> client : 성공 반환
    end
```
2. 게시글 수정 기능 구현
   - [x] TDD 작성
   - [x] 게시글 수정 성공
   - [x] **[예외처리]** 유저 이름 못찾음
   - [x] **[예외처리]** 토큰 만료
   - [x] **[예외처리]** 생성자와 수정자의 이름 불일치
   - [x] **[예외처리]** 게시글 못찾음
   - [x] 테스트 성공
```mermaid
sequenceDiagram
    autonumber
    client ->> server : 포스트 수정 요청
    alt 유저 이름 못찾음
    server ->> client : reason code와 함께 실패 반환
    else 토큰 만료
    server -->> client : reason code와 함께 실패 반환
    else 생성자와 수정자의 이름 불일치
    server -->> client : reason code와 함께 실패 반환
    else 게시글 못찾음
    server -> db : 게시글 요청
    db -->> server : 게시글 존재 X 에러
    else 성공한 경우
    server -> db : 게시글 요청
    db -->> server : 게시글 반환
    server ->> db : 게시글 저장 요청
    db -->> server : 저장 성공 반환
    server ->> client : 성공 반환
    end
```
3. 게시글 삭제 기능 구현
   - [x] TDD 작성
   - [x] 게시글 삭제 성공
   - [x] **[예외처리]** 생성자 삭제자 불일치
   - [x] **[예외처리]** 토큰 만료
   - [x] **[예외처리]** 게시글 존재 x
   - [x] **[예외처리]** 로그인 x
   - [x] 테스트 성공
```mermaid
sequenceDiagram
    autonumber
    client ->> server : 포스트 삭제 요청
    alt 생성자 삭제자 불일치
    server ->> client : reason code와 함께 실패 반환
    else 토큰 만료
    server -->> client : reason code와 함께 실패 반환
    else 게시글 존재 x
    server -> db : 게시글 요청
    db -->> server : 게시글 존재 X 에러
    else 로그인X
    server -->> client : reason code와 함께 실패 반환
    else 성공한 경우 
    server ->> db : 게시글 요청
    db -->> server: 게시글 반환
    server ->> db : 게시글 삭제 요청
    db --> server : 삭제 성공
    server ->> client : 성공 반환
    end
```
# 피드 목록 기능

1. 게시글 전체 목록
   - [x] TDD 작성
   - [x] 게시글 목록 조회
```mermaid
sequenceDiagram
    autonumber
    client ->> server : 게시글들 요청
    alt 성공한 경우 
    server ->> db : 게시글들 요청
    db -->> server: 게시글 반환
    server ->> client : 성공 반환
    end
```
2. 게시글 내가 작성한 목록
   - [x] TDD 작성
   - [x] 내 게시글 목록 조회
   - [x] **[예외처리]** 로그인 X
   - [x] **[예외처리]** 토큰 만료
   - [x] 테스트 성공 
```mermaid
sequenceDiagram
    autonumber
    client ->> server : 포스트 삭제 요청
    alt 로그인 X
    server ->> client : reason code와 함께 실패 반환
    else 토큰 만료
    server -->> client : reason code와 함께 실패 반환
    else 성공한 경우 
    server ->> db : 멤버 찾기
    db -->> server : 멤버 반환
    server ->> db: 유저 관련 게시글 요청
    db -->> server : 유저 관련 게시글 반환
    server ->> client : 성공 반환
    end
```
# 좋아요 기능

1. 좋아요 버튼 눌렀을 때
   - [x] TDD 작성
   - [x] 좋아요 버튼 누르면 LikeEntity생성
   - [x] **[예외처리]** 이미 좋아요 X
   - [x] **[예외처리]** 로그인 X
   - [x] **[예외처리]** 게시글 존재 X
   - [x] 테스트 성공
   - [x] 실제 사용
```mermaid
sequenceDiagram
    autonumber
    client ->> server : 좋아요 요청
    alt 이미 좋아요
    server ->> db : 좋아요 조회 요청
    db -->> server : error 반환 
    server -->> client : reason code와 함께 실패 반환
    else 로그인X
    server --> client : reason code와 함께 실패 반환
    else 토큰 만료
    server -->> client : reason code와 함께 실패 반환
    else 게시글 존재X
    server ->> db : 게시글 요청
    db -->> server : error 반환
    server -->> client : reason code와 함께 실패 반환
    else 성공한 경우 
    server ->> db : 멤버 찾기
    db -->> server : 멤버 반환
    server ->> db: 유저 관련 게시글 요청
    db -->> server : 유저 관련 게시글 반환
    server ->> db: 좋아요 저장 요청
    db -->> server: 성공 반환
    server ->> client : 성공 반환
    end
```
2. 좋아요 갯수 조회
   - [x] TDD 작성
   - [x] 특정 게시글 좋아요 갯수 반환
   - [x] **[예외처리]** 게시글 존재 X
   - [x] **[예외처리]** 로그인 X
   - [x] 테스트 성공
   - [x] 실제 사용
```mermaid
sequenceDiagram
    autonumber
    client ->> server : 좋아요 요청
    alt 게시글 존재X
    server ->> db : 게시글 요청
    db -->> server : error 반환
    server -->> client : reason code와 함께 실패 반환
    else 로그인X
    server --> client : reason code와 함께 실패 반환
    else 성공한 경우 
    server ->> db: 게시글 좋아요 갯수 요청
    db -->> server : 게시글 좋아요 갯수 반환
    server ->> client : 성공 반환
    end
```
# 댓글 기능

1. 댓글 작성 했을 때
   - [x] TDD 작성
   - [x] 댓글 생성
   - [x] **[예외처리]** 유저 이름 존재 X
   - [x] **[예외처리]** 게시글 존재 X
   - [x] 테스트 성공
   - [x] 실제 사용
```mermaid
sequenceDiagram
    autonumber
    client ->> server : 댓글 작성 요청
    alt 유저 이름 존재X
    server ->> db : 유저 조회 요청
    db -->> server : error 반환
    server -->> client : reason code와 함께 실패 반환
    else 게시글 존재X
    server --> db : 게시글 조회 요청
    db -->> server : error 반환
    server --> client : reason code와 함께 실패 반환
    else 성공한 경우 
    server ->> db: 댓글 저장 요청
    db -->> server : 댓글 반환
    server ->> client : 성공 반환
    end
```
2. 댓글 조회
   - [x] TDD 작성
   - [x] 댓글 조회
   - [x] **[예외처리]** 유저 이름 존재 X
   - [x] **[예외처리]** 게시글 존재 X
   - [x] 테스트 성공
   - [x] 포스트맨 테스트
```mermaid
sequenceDiagram
    autonumber
    client ->> server : 댓글 조회 요청
    alt 유저 이름 존재X
    server ->> db : 유저 조회 요청
    db -->> server : error 반환
    server -->> client : reason code와 함께 실패 반환
    else 게시글 존재X
    server --> db : 게시글 조회 요청
    db -->> server : error 반환
    server --> client : reason code와 함께 실패 반환
    else 성공한 경우 
    server ->> db: 댓글 조회 요청
    db -->> server : 댓글 반환
    server ->> client : 성공 반환
    end
```
3. 내 댓글 조회
   - [x] TDD 작성
   - [x] 내 댓글 조회
   - [x] **[예외처리]** 유저 존재X
   - [x] 테스트 성공
   - [x] 포스트맨 테스트
```mermaid
sequenceDiagram
    autonumber
    client ->> server : 내 댓글 조회 요청
    alt 유저 존재X
    server ->> db : 유저 조회 요청
    db -->> server : error 반환
    server -->> client : reason code와 함께 실패 반환
    else 성공한 경우 
    server ->> db : 유저 조회 요청
    db -->> server: 유저 반환
    server ->> db: 댓글 조회 요청
    db -->> server : 댓글 반환
    server ->> client : 성공 반환
    end
```
# 알람 기능

1. 댓글 생성시 알람 생성
   - [x] TDD 작성
   - [x] 댓글작성 알람생성
   - [x] **[예외처리]** 게시글 작성자, 댓글 작성자 일치 
   - [x] **[예외처리]** 게시글 존재X
   - [x] **[예외처리]** 로그인 X
   - [x] 테스트 성공
   - [x] 포스트맨 테스트
```mermaid
sequenceDiagram
    autonumber
    client ->> server : 댓글 알람 생성
    alt 알람 생성 안함
    server ->> db : 댓글 생성
    db -->> server : 성공 반환
    server -->> client : 성공 반환
    else 게시글 존재X
    server ->> db : 게시글 요청
    db --> server : error 반환
    server --> client : error code와 함께 반환
    else 로그인X
    server --> client : error code와 함께 반환
    else 성공한 경우 
    server ->> db : 유저 조회 요청
    db -->> server: 유저 반환
    server ->> db: 댓글 조회 요청
    db -->> server : 댓글 반환
    server ->> client : 성공 반환
    end
```
2. 좋아요시 알람 생성
   - [x] TDD 작성
   - [x] 좋아요 알람생성
   - [x] **[예외처리]** 게시글 작성자, 좋아요 작성자 일치
   - [x] **[예외처리]** 게시글 존재X
   - [x] **[예외처리]** 로그인 X
   - [x] 테스트 성공
   - [x] 포스트맨 테스트
```mermaid
sequenceDiagram
    autonumber
    client ->> server : 좋아요 알람 생성
    alt 알람 생성 안함
    server ->> db : 댓글 생성
    db -->> server : 성공 반환
    server -->> client : 성공 반환
    else 게시글 존재X
    server ->> db : 게시글 요청
    db --> server : error 반환
    server --> client : error code와 함께 반환
    else 성공한 경우 
    server ->> db : 유저 조회 요청
    db -->> server: 유저 반환
    server ->> db: 댓글 조회 요청
    db -->> server : 댓글 반환
    server ->> client : 성공 반환
    end
```
3. 내 알람 조회
   - [x] TDD 작성
   - [x] 내 알람 목록 조회
     - [x] "Username이 좋아요를 눌렀습니다."
     - [x] "Username이 댓글을 달았습니다."
   - [x] **[예외처리]** 로그인X
   - [x] 테스트 성공
   - [x] 포스트맨 테스트
```mermaid
sequenceDiagram
    autonumber
    client ->> server : 내 알람 조회 요청
    alt 로그인X
    server --> client : error code와 함께 반환
    else 성공한 경우 
    server ->> db : 유저 조회 요청
    db -->> server: 유저 반환
    server ->> db : 유저와 관련된 알람 조회 요청
    db -->> server : 알람들 반환
    server ->> client : 성공 반환
    end
```
# 코드 최적화

현재 문제점
1. 매 조회 마다 유저 Entity를 중복 조회한다. (회원인증, 동작수행)
2. 매 API 호출마다 Member 조회
3. 댓글, 좋아요를 했을 때 알람까지 생성이 되어야 정상적으로 동작이 수행됨
4. 알람을 표시하려면 리로드를 해야지만 알람이 표시됨
5. Query문 최적화

해결 방안
1. 코드의 비 최적화
2. 수많은 DB IO
3. 기능간의 강한 결합

# 문제점 1, 2 인증 최적화

1. 매 조회 마다 유저 Entity를 중복 조회
   - [x] Controller단에서 getName()이 아닌 Member로 캐스팅해서 전달
   - [x] SafeCasting Class로 캐스팅 하기

2. 테스트 케이스 리펙토링
   - [x] @WithMockUser -> @WithCustomMember
   - [x] @WithAnonymouse -> @WithCustomAnonymouse

### 문제점: 기존에는 Security에서 주어지는 어노테이션을 이용했었지만 사용하는 인증 객체가 달라서 오류가 발생했다.

Security: User.class
My: Member.class

### 해결방법: Member.class 전용으로 인증정보를 생성되는 팩토리 및 어노테이션을 만들자

1. WithCustomMember 어노테이션 만들기
   - [x] @Retention 어노테이션을 이용해 실행 범위 정하기
   - [x] @WithSeucrityContext 어노테이션을 이용해 Factory 정하기

2. WithCustomMember의 동작을 하는 Factory 생성하기
   - [x] WithSecurityContextFactory 구현
   - [x] SecurityContext 클래스로 context 생성
   - [x] UsernamePasswordAuthenticationToken 클래스 이용하여 인증정보 생성
   - [x] context에 인증정보 적용
   - [x] 반환
   - [x] 테스트 성공

3. WithCustomAnonymouse 어노테이션 만들기
   - [x] @Retention 어노테이션을 이용해 실행 범위 정하기
   - [x] @WithSeucrityContext 어노테이션을 이용해 Factory 정하기

4. WIthCustomAnonymouse 동작을 하는 Factory 생성
   - [x] WithSecurityContextFactory 구현
   - [x] SecurityContext 클래스로 context 생성
   - [x] AnonymouseAuthenticationToken 클래스 이용하여 인증정보 생성
   - [x] context에 인증정보 적용
   - [x] 반환
   - [x] 테스트 성공

# 쿼리 최적화

1. delete 메소드 사용하지 않고 update 문으로 직접 쿼리 작성
   - [x] 게시글 삭제 메소드
2. 게시글 삭제시 댓글, 좋아요 연관 삭제 기능
   - [x] 게시글 삭제
   - [x] 연관된 알람 삭제
   - [x] 연관된 댓글 삭제
   - [x] 연관된 좋아요 삭제
3. 테스트 작성
   - [x] 변경 테스트 성공
   - [x] 포스트맨 테스트

# 캐싱 서버 Redis

1. 자주 호출 되는 Member 클래스를 캐싱하는 redis서버 적용
   - [x] redis build.gradle 추가  
   - [x] redis configuratiㅎon 빈 등록하기 
   - [x] Member Cache DAO 생성하기
     - [x] 멤버 생성
     - [x] 멤버 가져오기
   - [x] Member 서비스단에서 Caching OR DB조회
   - [x] 로그인시 redis에 캐싱
   - [x] 포스트맨 테스트
   - [x] 테스트 성공
