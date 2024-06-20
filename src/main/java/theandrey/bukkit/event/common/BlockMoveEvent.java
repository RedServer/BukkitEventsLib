package theandrey.bukkit.event.common;

import java.util.Objects;
import javax.annotation.Nullable;
import org.bukkit.block.Block;
import theandrey.bukkit.event.BlockStateData;

/**
 * Базовый ивент перемещения блока
 */
public abstract class BlockMoveEvent extends CancellableEvent {

	protected final Action action;
	protected final Block sourceBlock;
	protected final Block destinationBlock;
	protected final BlockStateData placedData;

	/**
	 * @param action Действие (режим)
	 * @param sourceBlock Исходный блок (для Action = {@link Action#TAKE}, {@link Action#MOVE}, {@link Action#SWAP})
	 * @param destBlock Новое местоположение (для Action = {@link Action#PLACE}, {@link Action#MOVE}, {@link Action#SWAP})
	 * @param placedData Тип устанавливаемого блока (для Action = {@link Action#PLACE})
	 */
	public BlockMoveEvent(Action action, @Nullable Block sourceBlock, @Nullable Block destBlock, @Nullable BlockStateData placedData) {
		this.action = Objects.requireNonNull(action, "action is null");
		this.sourceBlock = sourceBlock;
		this.destinationBlock = destBlock;
		this.placedData = placedData;
	}

	/**
	 * Действие (вид перемещения)
	 */
	public Action getAction() {
		return action;
	}

	/**
	 * Исходная позиция блока A
	 * @return Доступен только для {@link Action#TAKE}, {@link Action#MOVE}, {@link Action#SWAP}
	 */
	@Nullable
	public Block getSourceBlock() {
		return sourceBlock;
	}

	/**
	 * Новая позиция блока B
	 * @return Доступен только для {@link Action#PLACE}, {@link Action#MOVE}, {@link Action#SWAP}
	 */
	@Nullable
	public Block getDestinationBlock() {
		return destinationBlock;
	}

	/**
	 * Material и meta размещаемого блока
	 * @return Доступен только для {@link Action#PLACE}
	 */
	@Nullable
	public BlockStateData getPlacedData() {
		return placedData;
	}

	public enum Action {
		/** Взятие блока и сохранение его состояния (обычно в предмет) */
		TAKE,
		/** Установка ранее сохранённого блока */
		PLACE,
		/** Прямое перемещение блока из точки A -> B */
		MOVE,
		/** Обмен блоков местами A <-> B */
		SWAP
	}
}
