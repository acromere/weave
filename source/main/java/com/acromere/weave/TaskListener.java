package com.acromere.weave;

public interface TaskListener {

	void updateHeader( String header );

	void updateMessage( String message );

	void updateProgress( int step );

}
