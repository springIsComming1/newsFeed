# Newsfeed SNS Project 

### 개요
본 프로젝트는 사용자가 프로필을 관리하고, 뉴스피드에 게시물을 작성 및 조회하며, 친구를 추가하고 상호작용할 수 있는 SNS 플랫폼입니다.

<br>

### API 명세서


### API 명세서


<br>

### 필수기능

#### 1. 프로필 관리

- **프로필 조회 기능**

  - 다른 사용자의 프로필을 조회할 수 있으며, 민감한 정보는 표시되지 않습니다.
  - 민감한 정보의 범위는 팀 내에서 결정합니다.

- **프로필 수정 기능**
  - 로그인한 사용자는 자신의 프로필 정보를 수정할 수 있습니다.

- **비밀번호 수정 조건**

  - 현재 비밀번호를 입력하여 본인 확인 후 변경 가능
  - 현재 비밀번호와 동일한 비밀번호로 변경 불가

- **예외 처리**

  - 현재 비밀번호가 일치하지 않는 경우
  - 비밀번호 형식이 올바르지 않은 경우
  - 현재 비밀번호와 동일한 비밀번호로 수정하는 경우

#### 2. 뉴스피드 게시물 관리

- **게시물 작성, 조회, 수정, 삭제 기능**

  - 작성자 본인만 게시물을 수정 및 삭제할 수 있습니다.

- **예외 처리**

  - 작성자가 아닌 사용자가 게시물 수정 또는 삭제를 시도하는 경우

- **뉴스피드 조회 기능**

  - 기본 정렬은 생성일자 기준 내림차순 정렬
  - 10개씩 페이지네이션하여 조회 가능

#### 3. 사용자 인증

- **회원가입 기능**

  - 사용자 아이디는 이메일 형식이어야 합니다.

- **비밀번호 조건**

  - `Bcrypt`를 사용하여 비밀번호 인코딩
  - 영문 대소문자, 숫자, 특수문자를 최소 1글자씩 포함해야 함
  - 최소 8글자 이상

- **예외 처리**

  - 중복된 사용자 아이디로 가입하는 경우
  - 이메일 형식 또는 비밀번호 형식이 올바르지 않은 경우

- **회원탈퇴 기능**

  - 비밀번호 확인 후 탈퇴 가능
  - 탈퇴한 아이디는 재사용 및 복구 불가

- **예외 처리**

  - 아이디와 비밀번호가 일치하지 않는 경우
  - 이미 탈퇴한 아이디인 경우

#### 4. 친구 관리

- 특정 사용자를 친구로 추가 및 삭제 가능

- 친구의 최신 게시물을 뉴스피드에서 최신순으로 조회 가능

- **주의 사항**

  - 친구 기능은 상대방의 수락이 필요함
  - 친구 기능이 어려울 경우, 팔로우 방식으로 구현 가능

<br>

### 도전기능

#### 1. 댓글 기능

- **댓글 작성, 조회, 수정, 삭제 기능**

  - 댓글 작성자는 자신의 댓글을 수정 및 삭제할 수 있음
  - 게시글 작성자는 해당 게시글의 댓글을 삭제할 수 있음

#### 2. 좋아요 기능

- **게시물 및 댓글 좋아요/좋아요 취소 기능**

  - 본인이 작성한 게시물과 댓글에는 좋아요를 남길 수 없음
  - 같은 게시물에는 사용자당 한 번만 좋아요 가능

<br>

### 기술 스택

- **백엔드**: Spring Boot, JPA

- **데이터베이스**: MySQL

- **비밀번호 암호화**: Bcrypt
