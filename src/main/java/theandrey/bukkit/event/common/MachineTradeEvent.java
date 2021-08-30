package theandrey.bukkit.event.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import theandrey.bukkit.event.api.IMachineType;

/**
 * Ивент покупки предмета в торговом автомате. Отмена ивента запрещает операцию купли-продажи.
 * @author TheAndrey
 */
public class MachineTradeEvent extends CancellableEvent {

	private static final HandlerList handlers = new HandlerList();

	private final Block machine;
	private final IMachineType machineType;
	private final ItemStack offerItem;
	private final ItemStack wantItem;
	private final List<Player> players = new ArrayList<>();

	/**
	 * @param machine Блок автомата
	 * @param type Тип механизма (из мода)
	 * @param want Предмет, который забирает у игрока
	 * @param offer Предмет, который отдаёт игроку
	 * @param players Список участвующих игроков
	 */
	public MachineTradeEvent(@Nonnull Block machine, @Nonnull IMachineType type, @Nonnull ItemStack want, @Nonnull ItemStack offer, @Nonnull Collection<Player> players) {
		this.machine = Objects.requireNonNull(machine, "machine");
		this.machineType = Objects.requireNonNull(type, "type");
		this.wantItem = Objects.requireNonNull(want, "want");
		this.offerItem = Objects.requireNonNull(offer, "offer");
		this.players.addAll(players);
	}

	@Nonnull
	public Block getMachineBlock() {
		return machine;
	}

	/**
	 * Предмет, который отдаёт игрок
	 */
	@Nonnull
	public ItemStack getWantItem() {
		return wantItem;
	}

	/**
	 * Предмет, который покупает игрок
	 */
	@Nonnull
	public ItemStack getOfferItem() {
		return offerItem;
	}

	/**
	 * Список игроков которые сейчас взаимодействуют с автоматом.
	 * Может быть пуст. Неизменяемый.
	 */
	@Nonnull
	public List<Player> getPlayers() {
		return Collections.unmodifiableList(players);
	}

	/**
	 * Тип механизма из мода (enum)
	 */
	@Nonnull
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
}