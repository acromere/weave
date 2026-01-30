package com.acromere.weave.task;

import com.acromere.weave.Task;
import com.acromere.weave.TaskResult;
import com.acromere.weave.TaskStatus;
import com.acromere.weave.UpdateTask;

import java.util.List;

public class FailTask extends Task {

	private final String message;

	public FailTask( List<String> parameters ) {
		super( UpdateTask.FAIL, parameters );
		this.message = getParameters().get( 0 );
	}

	@Override
	public int getStepCount() {
		return 1;
	}

	@Override
	public TaskResult execute() throws Exception {
		return new TaskResult( this, TaskStatus.FAILURE, message == null ? "" : "\"" + message + "\"" );
	}

}
