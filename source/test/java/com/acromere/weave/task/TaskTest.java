package com.acromere.weave.task;

import com.acromere.log.Log;
import com.acromere.util.LogFlag;
import com.acromere.util.Parameters;
import com.acromere.weave.Weave;
import com.acromere.weave.TaskResult;
import com.acromere.weave.TaskStatus;
import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.api.Assertions.assertThat;

abstract class TaskTest {

	protected Weave program;

	@BeforeEach
	public void setup() {
		this.program = new Weave();
		Log.configureLogging( program, Parameters.parse( LogFlag.LOG_LEVEL, "none" ) );
	}

	void assertTaskResult( TaskResult result, TaskStatus status ) {
		assertTaskResult( result, status, null );
	}

	void assertTaskResult( TaskResult result, TaskStatus status, String message ) {
		assertThat( result.getStatus() ).withFailMessage( result.getMessage() ).isEqualTo( status );
		if( message != null ) assertThat( result.getMessage() ).isEqualTo( message );
	}

}
