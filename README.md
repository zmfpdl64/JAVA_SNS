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
3. 게시글 삭제 기능 구현
   - [x] TDD 작성
   - [x] 게시글 삭제 성공
   - [x] **[예외처리]** 생성자 삭제자 불일치
   - [x] **[예외처리]** 토큰 만료
   - [x] **[예외처리]** 게시글 존재 x
   - [x] **[예외처리]** 로그인 x
   - [x] 테스트 성공

# 피드 목록 기능

1. 게시글 전체 목록
   - [x] TDD 작성
   - [x] 게시글 목록 조회
     

2. 게시글 내가 작성한 목록
   - [x] TDD 작성
   - [x] 내 게시글 목록 조회
   - [x] **[예외처리]** 로그인 X
   - [x] **[예외처리]** 토큰 만료
   - [x] 테스트 성공 

# 좋아요 기능

1. 좋아요 버튼 눌렀을 때
   - [x] TDD 작성
   - [x] 좋아요 버튼 누르면 LikeEntity생성
   - [x] **[예외처리]** 이미 좋아요 X
   - [x] **[예외처리]** 로그인 X
   - [x] **[예외처리]** 게시글 존재 X
   - [x] 테스트 성공
   - [x] 실제 사용

2. 좋아요 갯수 조회
   - [x] TDD 작성
   - [x] 특정 게시글 좋아요 갯수 반환
   - [x] **[예외처리]** 게시글 존재 X
   - [x] **[예외처리]** 로그인 X
   - [x] 테스트 성공
   - [x] 실제 사용
# 댓글 기능

1. 댓글 작성 했을 때
   - [x] TDD 작성
   - [x] 댓글 생성
   - [x] **[예외처리]** 유저 이름 존재 X
   - [x] **[예외처리]** 게시글 존재 X
   - [x] 테스트 성공
   - [x] 실제 사용

2. 댓글 조회
   - [x] TDD 작성
   - [x] 댓글 조회
   - [x] **[예외처리]** 유저 이름 존재 X
   - [x] **[예외처리]** 게시글 존재 X
   - [x] 테스트 성공
   - [x] 포스트맨 테스트
3. 내 댓글 조회
   - [x] TDD 작성
   - [x] 내 댓글 조회
   - [x] **[예외처리]** 유저 존재X
   - [x] 테스트 성공
   - [x] 포스트맨 테스트

# 알람 기능

1. 댓글 생성시 알람 생성
   - [x] TDD 작성
   - [x] 댓글작성 알람생성
   - [x] **[예외처리]** 게시글 작성자, 댓글 작성자 일치 
   - [x] **[예외처리]** 게시글 존재X
   - [x] **[예외처리]** 로그인 X
   - [x] 테스트 성공
   - [x] 포스트맨 테스트

2. 좋아요시 알람 생성
   - [x] TDD 작성
   - [x] 좋아요 알람생성
   - [x] **[예외처리]** 게시글 작성자, 좋아요 작성자 일치
   - [x] **[예외처리]** 게시글 존재X
   - [x] **[예외처리]** 로그인 X
   - [x] 테스트 성공
   - [x] 포스트맨 테스트

3. 내 알람 조회