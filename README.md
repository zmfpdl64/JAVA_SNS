SNS 프로젝트 요구사항 설계도

개발 순서

테스트 - 도메인 - 레포지토리 - 서비스 - 컨트롤러


## 구현 순서

# 로그인 회원 가입 기능

1. 회원 가입 기능 구현
   - [x] TDD 작성 
   - [x] 정상적인 회원 가입
   - [x] [**예외 발생**] 기존 ID 존재 
   - [x] 기능 구현s
   - [x] TDD 성공
2. 로그인 기능 구현
   - [x] TDD 작성
     - [x] 서비스 로직 작성
     - [x] 컨트롤러 로직 작성
   - [x] 정상적인 로그인
   - [x] **[예외처리]** 저장되어있는 ID 존재하지 않음
   - [x] **[예외처리]** 비밀번호가 일치하지 않음
   - [x] TDD 성공

# 게시글 기능

1. 게시글 작성 기능 구현
   - [x] 토큰 유효성 검증
   - [x] 토큰 유효하지 않을경우 **[예외처리]**
   - [X] TDD 작성
   - [x] 인증이 된 사용자 게시글 작성
   - [x] **[예외처리]** 인증안된 사용자 게시글 작성 
   - [x] TDD 성공
2. 게시글 수정 기능 구현
   - [x] TDD 작성
   - [x] 게시글 수정 성공
   - [x] **[예외처리]** 유저 이름 못찾음
   - [x] **[예외처리]** 토큰 만료
   - [x] **[예외처리]** 생성자와 수정자의 이름 불일치
   - [x] **[예외처리]** 게시글 못찾음
   - [x] 테스트 성공

# 피드 목록 기능

# 좋아요 기능

# 댓글 기능

# 알람 기능



3. 게시글 작성 기능
4. 게시글 수정 기능
5. 게시글 삭제 기능