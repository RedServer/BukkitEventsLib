package theandrey.bukkit.event.common;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import theandrey.bukkit.event.api.IMachineType;
import theandrey.bukkit.event.api.IPlayerEvent;

/**
 * Ивент продажи предмета в торговом автомате за игровую валюту. Отмена ивента запрещает операцию.
 * @author TheAndrey
 */
public class MachineMoneyTradeEvent extends CancellableEvent implements IPlayerEvent {

	private static final HandlerList handlers = new HandlerList();

	private final Block machine;
	private final IMachineType machineType;
	private final ItemStack item;
	private final boolean sell;
	private final double amount;
	private final Player player;
	private String message;

	/**
	 * @param machine Блок автомата
	 * @param type Тип механизма (из мода)
	 * @param item Предмет, который покупает/продаёт
	 * @param sell Игрок продаёт предмет за валюту, иначе покупает в автомате
	 * @param amount Сумма сделки
	 * @param player Список участвующих игроков
	 */
	public MachineMoneyTradeEvent(@Nonnull Block machine, @Nonnull IMachineType type, @Nonnull ItemStack item, boolean sell, double amount, @Nonnull Player player) {
		if (amount < 0) throw new IllegalArgumentException("Invalid amount: " + amount);

		this.machine = Objects.requireNonNull(machine, "machine");
		this.machineType = Objects.requireNonNull(type, "type");
		this.item = Objects.requireNonNull(item, "item");
		this.sell = sell;
		this.amount = amount;
		this.player = Objects.requireNonNull(player, "player");
	}

	@Nonnull
	public Block getMachineBlock() {
		return machine;
	}

	/**
	 * Предмет, который покупает или отдаёт игрок
	 */
	@Nonnull
	public ItemStack getItem() {
		return item;
	}

	/**
	 * Игрок, который совершает покупку/продажу
	 */
	@Override
	@Nonnull
	public Player getPlayer() {
		return player;
	}

	/**
	 * Сумма сделки (с учётом кол-ва предмета)
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * Игрок продаёт предмет за валюту или покупает
	 */
	public boolean isSell() {
		return sell;
	}

	/**
	 * Получить текст сообщения, если было установлено. Обычно используется для хранения текста ошибки.
	 */
	@Nullable
	public String getMessage() {
		return message;
	}

	/**
	 * Устанавливает текст сообщения
	 */
	public void setMessage(@Nullable String message) {
		this.message = message;
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