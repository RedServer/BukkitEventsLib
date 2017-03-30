package theandrey.bukkit.event.util.asm;

public final class ASMClassLoader extends ClassLoader {

	ASMClassLoader() {
		super(ASMClassLoader.class.getClassLoader());
	}

	Class<?> defineClass(String name, byte[] data) {
		return defineClass(name, data, 0, data.length);
	}

}
