package com.squareup.otto;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;



/**
 * @author suj1th
 *
 */
public class AsynchronousBus extends Bus {
	
	
	ExecutorService executorService = new ExceptionHandlerThreadPool();

	 /**
	   * Dispatches {@code event} to the handler in {@code wrapper}.
	   *
	   * @param event event to dispatch.
	   * @param wrapper wrapper that will call the handler.
	   */
	  protected void dispatch(final Object event, final EventHandler wrapper){
		  executorService.execute(new Runnable() {
			
			@Override
			public void run() {
					try {
						wrapper.handleEvent(event);
					} catch (InvocationTargetException e) {
						AsynchronousBus.throwRuntimeException("Exception in Dispatch Thread", e);
					}
				
				
			}
		});
	  }
	

	  /**
	   * Throw a {@link RuntimeException} with given message and cause lifted from an {@link
	   * InvocationTargetException}. If the specified {@link InvocationTargetException} does not have a
	   * cause, neither will the {@link RuntimeException}.
	   */
	  private static void throwRuntimeException(String msg, InvocationTargetException e) {
	    Throwable cause = e.getCause();
	    if (cause != null) {
	      throw new RuntimeException(msg + ": " + cause.getMessage(), cause);
	    } else {
	      throw new RuntimeException(msg + ": " + e.getMessage(), e);
	    }
	  }
}
