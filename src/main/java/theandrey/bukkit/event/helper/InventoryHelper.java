package theandrey.bukkit.event.helper;

import net.minecraft.inventory.IInventory;

public final class InventoryHelper {

	private InventoryHelper() {
	}

	/**
	 * Очищает инвентарь. Устанавливает null для всех слотов
	 */
	public static void wipeInventory(IInventory inventory) {
		if (inventory == null) throw new IllegalArgumentException("inventory is null");

		for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
			inventory.setInventorySlotContents(slot, null);
		}
	}
}
