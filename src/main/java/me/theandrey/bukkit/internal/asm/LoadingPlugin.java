package me.theandrey.bukkit.internal.asm;

import java.util.Map;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.SortingIndex(1001)
@IFMLLoadingPlugin.TransformerExclusions("me.theandrey.bukkit.internal.asm.")
public final class LoadingPlugin implements IFMLLoadingPlugin {

	public static final Logger LOGGER = LogManager.getLogger();

	@Override
	public String[] getASMTransformerClass() {
		return new String[]{
			"me.theandrey.bukkit.internal.asm.BukkitInventoryTransformer",
			"me.theandrey.bukkit.internal.asm.CraftBukkitAccessorGenerator"
		};
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		// NO-OP
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}
