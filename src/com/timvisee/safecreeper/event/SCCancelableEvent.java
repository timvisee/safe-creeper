package com.timvisee.safecreeper.event;

public class SCCancelableEvent extends SCEvent {

	private boolean isCancelled = false;

	/**
	 * Check if the event is cancelled
	 * @return boolean True if cancelled
	 */
	public boolean isCancelled() {
		return this.isCancelled;
	}
	
	/**
	 * Set if the event should be cancelled
	 * @param cancelled True to cancel the event
	 */
	public void setCancelled(boolean cancelled) {
		this.isCancelled = cancelled;
	}
}
