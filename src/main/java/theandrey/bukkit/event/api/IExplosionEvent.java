package theandrey.bukkit.event.api;

import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

/**
 * Реализуется эвентами взрыва
 */
public interface IExplosionEvent {

	/**
	 * Список блоков, которые затронул взрыв
	 * @return
	 */
	public List<Block> getBlocks();

	/**
	 * Возвращает Entity, который вызвал взрыв
	 * @return null, если неизвестен
	 */
	public Entity getExplodedEntity();

	/**
	 * Возвращает блок, который вызвал взрыв
	 * @return null, если неизвестен
	 */
	public Block getExplodedBlock();

	/**
	 * Возвращает имя владельца взорвавшегося Entity или блока
	 * @return null, если неизвестен
	 */
	public String getOwnerName();

}
