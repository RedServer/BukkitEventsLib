package theandrey.bukkit.event.common;

import java.util.Objects;
import java.util.UUID;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import theandrey.bukkit.event.api.IMachineType;

/**
 * Ивент покупки предмета/продажи в торговом автомате за игровую валюту.
 * Отмена ивента запрещает операцию купли-продажи.
 * @author TheAndrey
 */
public class MachineETradeEvent extends CancellableEvent {

	private static final HandlerList handlers = new HandlerList();

	private final Block machine;
	private final IMachineType machineType;
	private final ItemStack item;
	private final double sum;
	private final Player player;
	private final UUID merchant;

	/**
	 * @param machine Блок автомата
	 * @param type Тип механизма (из мода)
	 * @param item Предмет
	 * @param sum Сумма сделки
	 * @param player Игрок
	 * @param merchant ID продавца (игрока)
	 */
	public MachineETradeEvent(Block machine, IMachineType type, ItemStack item, double sum, Player player, UUID merchant) {
		this.machine = Objects.requireNonNull(machine, "machine");
		this.machineType = Objects.requireNonNull(type, "type");
		this.item = Objects.requireNonNull(item, "item");
		this.sum = sum;
		this.player = Objects.requireNonNull(player, "player");
		this.merchant = Objects.requireNonNull(merchant, "merchant");
	}

	public Block getMachineBlock() {
		return machine;
	}

	/**
	 * Сумма сделки
	 * @return Положительное значение при продаже предмета игроку или отрицательное в случае выплаты за приобретённый товар
	 */
	public double getSum() {
		return sum;
	}

	/**
	 * Предмет, который покупает или продаёт игрок
	 */
	public ItemStack getItem() {
		return item;
	}

	/**
	 * Игрок участвующий в сделке
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * ID продавца (обычно это игрок)
	 */
	public UUID getMerchant() {
		return merchant;
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
}
