package theandrey.bukkit.event.common;

import java.util.Objects;
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
	 * @param sourceBlock Исходный блок (для Action = TAKE,MOVE,SWAP)
	 * @param destBlock Новое местоположение (для Action = PLACE,MOVE,SWAP)
	 * @param placedData Тип устанавливаемого блока (для Action = PLACE)
	 */
	public BlockMoveEvent(Action action, Block sourceBlock, Block destBlock, BlockStateData placedData) {
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
	 * @return Доступа только для TAKE|MOVE|SWAP, иначе - null
	 */
	public Block getSourceBlock() {
		return sourceBlock;
	}

	/**
	 * Новая позиция блока B
	 * @return Доступа только для PLACE|MOVE|SWAP, иначе - null
	 */
	public Block getDestinationBlock() {
		return destinationBlock;
	}

	/**
	 * Material и meta размещаемого блока
	 * @return Доступен только для PLACE
	 */
	public BlockStateData getPlacedData() {
		return placedData;
	}

	public enum Action {
		/**
		 * Взятие блока и сохранение его состояния (обычно в предмет)
		 */
		TAKE,
		/**
		 * Установка ранее сохранённого блока
		 */
		PLACE,
		/**
		 * Прямое переменение блока из точки A -> B
		 */
		MOVE,
		/**
		 * Обмен блоков местами A <-> B
		 */
		SWAP
	}
}
