package theandrey.bukkit.event.common;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

/**
 * Базовый класс ивента, реализующий возможность отмены
 * @author TheAndrey
 */
public abstract class CancellableEvent extends Event implements Cancellable {

	protected boolean cancelled = false;

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}

}
