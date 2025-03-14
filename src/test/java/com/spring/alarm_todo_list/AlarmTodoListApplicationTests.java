package com.spring.alarm_todo_list;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import({QuerydslConfig.class})
class AlarmTodoListApplicationTests {

	@Test
	void contextLoads() {
	}

}
