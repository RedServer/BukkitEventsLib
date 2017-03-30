package theandrey.bukkit.event.util.asm;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.bukkit.Bukkit;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import theandrey.bukkit.event.util.ReflectionHelper;

/**
 * Фабрика ASMAccessor
 * @author TheAndrey
 */
public final class ASMAccessor {

	private static final ASMAccessor INSTANCE = new ASMAccessor();
	private static final ASMClassLoader CLASS_LOADER = new ASMClassLoader();

	private ASMAccessor() {
	}

	public static ASMAccessor instance() {
		return INSTANCE;
	}

	public CraftBukkitAccessor createAccessor() {
		try {
			String className = ASMAccessor.class.getName() + "_" + CraftBukkitAccessor.class.getSimpleName() + "Impl";
			ClassWriter cw = new ClassWriter(0);
			cw.visit(Opcodes.V1_7, Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL | Opcodes.ACC_SUPER, className.replace('.', '/'), null, "java/lang/Object", new String[]{Type.getInternalName(CraftBukkitAccessor.class)});
			cw.visitSource(".dynamic", null);

			Method method;
			MethodVisitor mv;

			// Конструктор
			mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
			mv.visitCode();
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(1, 1);
			mv.visitEnd();

			String nmsVersion = getNmsVersion();

			// Методы
			method = ReflectionHelper.getMethodByName(CraftBukkitAccessor.class, "getBukkitEntity");
			mv = cw.visitMethod(Opcodes.ACC_PUBLIC, method.getName(), Type.getMethodDescriptor(method), null, null);
			mv.visitCode();
			mv.visitVarInsn(Opcodes.ALOAD, 1); // 1 параметр
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, Type.getInternalName(net.minecraft.entity.Entity.class), "getBukkitEntity", "()Lorg/bukkit/craftbukkit/" + nmsVersion + "/entity/CraftEntity;", false);
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitMaxs(2, 2);
			mv.visitEnd();

			method = ReflectionHelper.getMethodByName(CraftBukkitAccessor.class, "getBukkitWorld");
			mv = cw.visitMethod(Opcodes.ACC_PUBLIC, method.getName(), Type.getMethodDescriptor(method), null, null);
			mv.visitCode();
			mv.visitVarInsn(Opcodes.ALOAD, 1); // 1 параметр
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, Type.getInternalName(net.minecraft.world.World.class), "getWorld", "()Lorg/bukkit/craftbukkit/" + nmsVersion + "/CraftWorld;", false);
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitMaxs(2, 2);
			mv.visitEnd();

			method = ReflectionHelper.getMethodByName(CraftBukkitAccessor.class, "asCraftMirror");
			mv = cw.visitMethod(Opcodes.ACC_PUBLIC, method.getName(), Type.getMethodDescriptor(method), null, null);
			String craftItemClass = "org/bukkit/craftbukkit/" + nmsVersion + "/inventory/CraftItemStack";
			mv.visitCode();
			mv.visitVarInsn(Opcodes.ALOAD, 1); // 1 параметр
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, craftItemClass, "asCraftMirror", "(" + Type.getType(net.minecraft.item.ItemStack.class) + ")L" + craftItemClass + ";", false);
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitMaxs(2, 2);
			mv.visitEnd();

			method = ReflectionHelper.getMethodByName(CraftBukkitAccessor.class, "getBlockState");
			mv = cw.visitMethod(Opcodes.ACC_PUBLIC, method.getName(), Type.getMethodDescriptor(method), null, null);
			String craftBlockStateClass = "org/bukkit/craftbukkit/" + nmsVersion + "/block/CraftBlockState";
			mv.visitCode();
			mv.visitVarInsn(Opcodes.ALOAD, 1); // 1 параметр
			mv.visitVarInsn(Opcodes.ILOAD, 2); // 2 параметр (int)
			mv.visitVarInsn(Opcodes.ILOAD, 3); // 3 параметр (int)
			mv.visitVarInsn(Opcodes.ILOAD, 4); // 4 параметр (int)
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, craftBlockStateClass, "getBlockState", "(" + Type.getType(net.minecraft.world.World.class) + "III)L" + craftBlockStateClass + ";", false);
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitMaxs(5, 5);
			mv.visitEnd();

			cw.visitEnd();
			//saveDump(className, cw.toByteArray());
			Class<?> clazz = CLASS_LOADER.defineClass(className, cw.toByteArray());
			return (CraftBukkitAccessor)clazz.newInstance();
		} catch (ReflectiveOperationException ex) {
			throw new RuntimeException("Error creating accessor", ex);
		}
	}

	/**
	 * Получить версию пакета CraftBukkit
	 * @return v1_4_R1 или другая
	 */
	public static String getNmsVersion() {
		String packageName = Bukkit.getServer().getClass().getPackage().getName();
		return packageName.substring(packageName.lastIndexOf('.') + 1);
	}

	private static void saveDump(String classname, byte[] data) {
		try {
			Path path = Paths.get("classdump", classname + ".class");
			Files.createDirectories(path.getParent());
			Files.write(path, data);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
