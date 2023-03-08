package com.example.placemeeting;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class PlacemeetingApplicationTests {

    /*
    - Junit이란.
    1. Java에서 독립된 단위 테스트를 지원해주는 프레임워크
    2. Assert(검증)을 이용해서 결과를 기댓값과 실제 값을 비교
    3. @Test 어노테이션마다 독립적으로 테스트를 진행 및 확인할 수 있다.

    - 단위 테스트와 통합 테스트
    단위(Unit) 테스트
    > 하나의 모듈을 기준으로 독립적으로 진행되는 가장 작은 단위 테스트(하나의 기능 혹은 메서드)

    통합(Intergration)테스트
    > 모듈을 통합하는 과정에서 모듈 간의 호환성을 확인하는 테스트(Unit이 하나였다면 반대로 여러 개의 계층이 테스트에 참여한 것임)

    */


    @Test
    void contextLoads() {
    }

}
