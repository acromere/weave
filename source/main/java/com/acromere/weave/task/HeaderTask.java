package com.acromere.weave.task;

import com.acromere.weave.Task;
import com.acromere.weave.TaskResult;
import com.acromere.weave.TaskStatus;
import com.acromere.weave.UpdateTask;

import java.util.List;

public class HeaderTask extends Task {

	private final String header;

	public HeaderTask( List<String> parameters ) {
		super( UpdateTask.HEADER, parameters );
		this.header = getParameters().get( 0 );
	}

	@Override
	public int getStepCount() {
		return 0;
	}

	@Override
	public TaskResult execute() throws Exception {
		setHeader( header );
		return new TaskResult( this, TaskStatus.SUCCESS, header == null ? "" : "\"" + header + "\"" );
	}

	@Override
	public TaskResult rollback() throws Exception {
		decrementProgress();
		return new TaskResult( this, TaskStatus.ROLLBACK, header == null ? "" : "\"" + header + "\"" );
	}

}
