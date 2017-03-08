package theandrey.bukkit.util.asm;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.bukkit.Bukkit;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import theandrey.bukkit.util.ReflectionHelper;

/**
 * Фабрика ASMAccessor
 * @author TheAndrey
 */
public final class ASMAccessor {

	private static final ASMAccessor INSTANCE = new ASMAccessor();
	private static final String NMS_PACKAGE_VERSION = getNmsVersion();
	private static final ASMClassLoader CLASS_LOADER = new ASMClassLoader();

	private ASMAccessor() {
	}

	public static ASMAccessor instance() {
		return INSTANCE;
	}

	public EntityAccessor createEntityAccessor() {
		try {
			String implName = getAccessorClassName(EntityAccessor.class);
			ClassWriter cw = createAccessorClass(implName, EntityAccessor.class);

			// Метод
			Method method = ReflectionHelper.getMethodByName(EntityAccessor.class, "getBukkitEntity");
			MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, method.getName(), Type.getMethodDescriptor(method), null, null);
			mv.visitCode();
			mv.visitVarInsn(Opcodes.ALOAD, 1); // 1 параметр
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, Type.getInternalName(net.minecraft.entity.Entity.class), "getBukkitEntity", "()Lorg/bukkit/craftbukkit/" + NMS_PACKAGE_VERSION + "/entity/CraftEntity;", false);
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitMaxs(2, 2);
			mv.visitEnd();

			cw.visitEnd();
			saveDump(implName, cw.toByteArray());
			Class<?> clazz = CLASS_LOADER.defineClass(implName, cw.toByteArray());
			return (EntityAccessor)clazz.newInstance();
		} catch (ReflectiveOperationException ex) {
			throw new RuntimeException("Error creating accessor", ex);
		}
	}

	public WorldAccessor createWorldAccessor() {
		try {
			String implName = getAccessorClassName(WorldAccessor.class);
			ClassWriter cw = createAccessorClass(implName, WorldAccessor.class);

			// Метод
			Method method = ReflectionHelper.getMethodByName(WorldAccessor.class, "getBukkitWorld");
			MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, method.getName(), Type.getMethodDescriptor(method), null, null);
			mv.visitCode();
			mv.visitVarInsn(Opcodes.ALOAD, 1); // 1 параметр
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, Type.getInternalName(net.minecraft.world.World.class), "getWorld", "()Lorg/bukkit/craftbukkit/" + NMS_PACKAGE_VERSION + "/CraftWorld;", false);
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitMaxs(2, 2);
			mv.visitEnd();

			cw.visitEnd();
			saveDump(implName, cw.toByteArray());
			Class<?> clazz = CLASS_LOADER.defineClass(implName, cw.toByteArray());
			return (WorldAccessor)clazz.newInstance();
		} catch (ReflectiveOperationException ex) {
			throw new RuntimeException("Error creating accessor", ex);
		}
	}

	public CraftItemStackAccessor createCraftItemStackAccessor() {
		try {
			String implName = getAccessorClassName(CraftItemStackAccessor.class);
			ClassWriter cw = createAccessorClass(implName, CraftItemStackAccessor.class);

			// Метод
			Method method = ReflectionHelper.getMethodByName(CraftItemStackAccessor.class, "asCraftMirror");
			MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, method.getName(), Type.getMethodDescriptor(method), null, null);
			String craftItemClass = "org/bukkit/craftbukkit/" + NMS_PACKAGE_VERSION + "/inventory/CraftItemStack";
			mv.visitCode();
			mv.visitVarInsn(Opcodes.ALOAD, 1); // 1 параметр
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, craftItemClass, "asCraftMirror", "(" + Type.getType(net.minecraft.item.ItemStack.class) + ")L" + craftItemClass + ";", false);
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitMaxs(2, 2);
			mv.visitEnd();

			cw.visitEnd();
			saveDump(implName, cw.toByteArray());
			Class<?> clazz = CLASS_LOADER.defineClass(implName, cw.toByteArray());
			return (CraftItemStackAccessor)clazz.newInstance();
		} catch (ReflectiveOperationException ex) {
			throw new RuntimeException("Error creating accessor", ex);
		}
	}

	private ClassWriter createAccessorClass(String name, Class<?> iface) {
		ClassWriter cw = new ClassWriter(0);
		cw.visit(Opcodes.V1_7, Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER, name.replace('.', '/'), null, "java/lang/Object", new String[]{Type.getInternalName(iface)});
		cw.visitSource(".dynamic", null);

		// Конструктор
		MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
		mv.visitCode();
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(1, 1);
		mv.visitEnd();

		return cw;
	}

	private static String getAccessorClassName(Class<?> iface) {
		return ASMAccessor.class.getName() + "_" + iface.getSimpleName() + "Impl";
	}

	private static String getNmsVersion() {
		String packageName = Bukkit.getServer().getClass().getPackage().getName();
		return packageName.substring(packageName.lastIndexOf('.') + 1);
	}

	private static void saveDump(String classname, byte[] data) {
		try {
			Files.write(Paths.get("asm/" + classname + ".class"), data);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
