package theandrey.bukkit.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.minecraft.world.World;
import theandrey.bukkit.util.asm.ASMAccessor;

public final class OrebfuscatorManager {

	private static Method updateNearbyBlocksMethod;

	static {
		try {
			updateNearbyBlocksMethod = Class.forName("org.bukkit.craftbukkit." + ASMAccessor.getNmsVersion() + ".OrebfuscatorManager").getMethod("updateNearbyBlocks", World.class, int.class, int.class, int.class);
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException ex) {
			throw new RuntimeException("[OrebfuscatorManager] Произошла ошибка при инициализации методов", ex);
		}
	}

	public static void updateNearbyBlocks(World world, int x, int y, int z) {
		try {
			updateNearbyBlocksMethod.invoke(null, world, x, y, z);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			throw new RuntimeException(ex);
		}
	}

}
