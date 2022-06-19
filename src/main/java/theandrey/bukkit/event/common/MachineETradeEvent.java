package theandrey.bukkit.event.common;

import java.util.Objects;
import javax.annotation.Nonnull;
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
	private final String merchant;

	/**
	 * @param machine Блок автомата
	 * @param type Тип механизма (из мода)
	 * @param item Предмет
	 * @param sum Сумма сделки
	 * @param player Игрок
	 * @param merchant Продавец (игрок)
	 */
	public MachineETradeEvent(@Nonnull Block machine, @Nonnull IMachineType type, @Nonnull ItemStack item, double sum, @Nonnull Player player, @Nonnull String merchant) {
		this.machine = Objects.requireNonNull(machine, "machine");
		this.machineType = Objects.requireNonNull(type, "type");
		this.item = Objects.requireNonNull(item, "item");
		this.sum = sum;
		this.player = Objects.requireNonNull(player, "player");
		this.merchant = Objects.requireNonNull(merchant, "merchant");
	}

	@Nonnull
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
	@Nonnull
	public ItemStack getItem() {
		return item;
	}

	/**
	 * Игрок участвующий в сделке
	 */
	@Nonnull
	public Player getPlayer() {
		return player;
	}

	/**
	 * Продавец (обычно это игрок)
	 */
	@Nonnull
	public String getMerchant() {
		return merchant;
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