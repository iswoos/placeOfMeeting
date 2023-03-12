-- foreign key constraint fails 에러
-- 한 테이블에서 다른 테이블을 참조하고 있다면, 해당 에러가 발생하면서 데이터가 삭제되지 않는다.
-- 해당 이슈를 해결하기 위해 아래와 같은 비활성화, 활성화 쿼리를 사용한다

SET FOREIGN_KEY_CHECKS=0; --foreign key 제약조건으로 인해 삭제안될 때는 위와 같이 비활성화
TRUNCATE TABLE member;
TRUNCATE TABLE post;
TRUNCATE TABLE comment;
SET FOREIGN_KEY_CHECKS=1; -- 제약조건 다시 활성화
