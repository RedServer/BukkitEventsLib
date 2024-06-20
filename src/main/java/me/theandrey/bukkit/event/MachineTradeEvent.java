package me.theandrey.bukkit.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import me.theandrey.bukkit.api.IMachineType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * Ивент покупки предмета в торговом автомате. Отмена ивента запрещает операцию купли-продажи.
 * @author TheAndrey
 */
public class MachineTradeEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private final Block machine;
	private final IMachineType machineType;
	private final ItemStack offerItem;
	private final ItemStack wantItem;
	private final List<Player> players = new ArrayList<>();
	protected boolean cancelled = false;

	/**
	 * @param machine Блок автомата
	 * @param type Тип механизма (из мода)
	 * @param want Предмет, который забирает у игрока
	 * @param offer Предмет, который отдаёт игроку
	 * @param players Список участвующих игроков
	 */
	public MachineTradeEvent(Block machine, IMachineType type, ItemStack want, ItemStack offer, Collection<Player> players) {
		this.machine = Objects.requireNonNull(machine, "machine");
		this.machineType = Objects.requireNonNull(type, "type");
		this.wantItem = Objects.requireNonNull(want, "want");
		this.offerItem = Objects.requireNonNull(offer, "offer");
		this.players.addAll(players);
	}

	public Block getMachineBlock() {
		return machine;
	}

	/**
	 * Предмет, который отдаёт игрок
	 */
	public ItemStack getWantItem() {
		return wantItem;
	}

	/**
	 * Предмет, который покупает игрок
	 */
	public ItemStack getOfferItem() {
		return offerItem;
	}

	/**
	 * Список игроков которые сейчас взаимодействуют с автоматом.
	 * Может быть пуст. Неизменяемый.
	 */
	public List<Player> getPlayers() {
		return Collections.unmodifiableList(players);
	}

	/**
	 * Тип механизма из мода (enum)
	 */
	public IMachineType getMachineType() {
		return machineType;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}
}
