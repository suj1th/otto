package com.squareup.otto;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExceptionHandlerThreadPool extends ThreadPoolExecutor {

	private static final int _ONE = 1;
	private static final int _EIGHT = 8;
	private static final BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();


	public ExceptionHandlerThreadPool(){
		super(_ONE, _EIGHT, _ONE, TimeUnit.MINUTES, workQueue);
	}

	protected void afterExecute(Runnable r, Throwable t){
		super.afterExecute(r, t);

		if(t==null && r instanceof Future<?>){
			Future<?> future = (Future<?>) r;
			try {
				future.get();
			} catch ( ExecutionException e) {
				System.out.println("Exception in Retrieving result from Future Dispatch Thread : " + e);
				Thread.currentThread().interrupt();
			} catch ( InterruptedException e) {
				System.out.println("Exception in Retrieving result from Future Dispatch Thread : " + e);
				Thread.currentThread().interrupt();
			}
		}

		if (t != null){
			System.out.println("Exception in Dispatch Thread : " + t);
		}
	}

}


