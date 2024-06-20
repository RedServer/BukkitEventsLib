package theandrey.bukkit.event.util.asm;

import java.lang.reflect.Method;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import theandrey.bukkit.event.helper.ReflectionHelper;

/**
 * Фабрика ASMAccessor
 * @author TheAndrey
 */
public final class ASMAccessor {

	private ASMAccessor() {
	}

	/**
	 * Создаёт класс доступа к методам CraftBukkit
	 */
	public static Class<? extends CraftBukkitAccessor> makeAccessorClass() {
		try {
			String className = ASMAccessor.class.getName() + "_" + CraftBukkitAccessor.class.getSimpleName() + "Impl";
			ClassWriter cw = new ClassWriter(0);
			cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL | Opcodes.ACC_SUPER, className.replace('.', '/'), null, "java/lang/Object", new String[]{Type.getInternalName(CraftBukkitAccessor.class)});
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

			// org/bukkit/craftbukkit/V_*_*
			final String nmsPackage = ASMHelper.getCbPackage().replace(".", "/");

			// Методы
			method = ReflectionHelper.getMethodByName(CraftBukkitAccessor.class, "getBukkitEntity");
			mv = cw.visitMethod(Opcodes.ACC_PUBLIC, method.getName(), Type.getMethodDescriptor(method), null, null);
			mv.visitCode();
			mv.visitVarInsn(Opcodes.ALOAD, 1); // 1 параметр
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, Type.getInternalName(net.minecraft.entity.Entity.class), "getBukkitEntity", "()L" + nmsPackage + "/entity/CraftEntity;", false);
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitMaxs(2, 2);
			mv.visitEnd();

			method = ReflectionHelper.getMethodByName(CraftBukkitAccessor.class, "getBukkitWorld");
			mv = cw.visitMethod(Opcodes.ACC_PUBLIC, method.getName(), Type.getMethodDescriptor(method), null, null);
			mv.visitCode();
			mv.visitVarInsn(Opcodes.ALOAD, 1); // 1 параметр
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, Type.getInternalName(net.minecraft.world.World.class), "getWorld", "()L" + nmsPackage + "/CraftWorld;", false);
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitMaxs(2, 2);
			mv.visitEnd();

			method = ReflectionHelper.getMethodByName(CraftBukkitAccessor.class, "asCraftMirror");
			mv = cw.visitMethod(Opcodes.ACC_PUBLIC, method.getName(), Type.getMethodDescriptor(method), null, null);
			String craftItemClass = nmsPackage + "/inventory/CraftItemStack";
			mv.visitCode();
			mv.visitVarInsn(Opcodes.ALOAD, 1); // 1 параметр
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, craftItemClass, "asCraftMirror", "(" + Type.getType(net.minecraft.item.ItemStack.class) + ")L" + craftItemClass + ";", false);
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitMaxs(2, 2);
			mv.visitEnd();

			method = ReflectionHelper.getMethodByName(CraftBukkitAccessor.class, "getBlockState");
			mv = cw.visitMethod(Opcodes.ACC_PUBLIC, method.getName(), Type.getMethodDescriptor(method), null, null);
			String craftBlockStateClass = nmsPackage + "/block/CraftBlockState";
			mv.visitCode();
			mv.visitVarInsn(Opcodes.ALOAD, 1); // 1 параметр
			mv.visitVarInsn(Opcodes.ILOAD, 2); // 2 параметр (int)
			mv.visitVarInsn(Opcodes.ILOAD, 3); // 3 параметр (int)
			mv.visitVarInsn(Opcodes.ILOAD, 4); // 4 параметр (int)
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, craftBlockStateClass, "getBlockState", "(" + Type.getType(net.minecraft.world.World.class) + "III)L" + craftBlockStateClass + ";", false);
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitMaxs(5, 5);
			mv.visitEnd();

			method = ReflectionHelper.getMethodByName(CraftBukkitAccessor.class, "spawnEntityInWorld");
			mv = cw.visitMethod(Opcodes.ACC_PUBLIC, method.getName(), Type.getMethodDescriptor(method), null, null);
			mv.visitCode();
			mv.visitVarInsn(Opcodes.ALOAD, 1); // param: world
			mv.visitVarInsn(Opcodes.ALOAD, 2); // param: entity
			mv.visitVarInsn(Opcodes.ALOAD, 3); // param: reason
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, Type.getInternalName(net.minecraft.world.World.class), "addEntity", "(Lnet/minecraft/entity/Entity;Lorg/bukkit/event/entity/CreatureSpawnEvent$SpawnReason;)Z", false);
			mv.visitInsn(Opcodes.IRETURN);
			mv.visitMaxs(4, 4);
			mv.visitEnd();

			method = ReflectionHelper.getMethodByName(CraftBukkitAccessor.class, "getWorldHandle");
			mv = cw.visitMethod(Opcodes.ACC_PUBLIC, method.getName(), Type.getMethodDescriptor(method), null, null);
			mv.visitCode();
			mv.visitVarInsn(Opcodes.ALOAD, 1); // 1 параметр
			mv.visitTypeInsn(Opcodes.CHECKCAST, nmsPackage + "/CraftWorld");
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, nmsPackage + "/CraftWorld", "getHandle", "()Lnet/minecraft/world/WorldServer;", false);
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitMaxs(2, 2);
			mv.visitEnd();

			method = ReflectionHelper.getMethodByName(CraftBukkitAccessor.class, "getEntityHandle");
			mv = cw.visitMethod(Opcodes.ACC_PUBLIC, method.getName(), Type.getMethodDescriptor(method), null, null);
			mv.visitCode();
			mv.visitVarInsn(Opcodes.ALOAD, 1); // 1 параметр
			mv.visitTypeInsn(Opcodes.CHECKCAST, nmsPackage + "/entity/CraftEntity");
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, nmsPackage + "/entity/CraftEntity", "getHandle", "()Lnet/minecraft/entity/Entity;", false);
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitMaxs(2, 2);
			mv.visitEnd();

			method = ReflectionHelper.getMethodByName(CraftBukkitAccessor.class, "setEntityDamage");
			mv = cw.visitMethod(Opcodes.ACC_PUBLIC, method.getName(), Type.getMethodDescriptor(method), null, null);
			mv.visitCode();
			mv.visitVarInsn(Opcodes.ALOAD, 1); // 1 параметр
			mv.visitFieldInsn(Opcodes.PUTSTATIC, nmsPackage + "/event/CraftEventFactory", "entityDamage", "Lnet/minecraft/entity/Entity;");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(2, 2);
			mv.visitEnd();

			method = ReflectionHelper.getMethodByName(CraftBukkitAccessor.class, "setBlockDamage");
			mv = cw.visitMethod(Opcodes.ACC_PUBLIC, method.getName(), Type.getMethodDescriptor(method), null, null);
			mv.visitCode();
			mv.visitVarInsn(Opcodes.ALOAD, 1); // 1 параметр
			mv.visitFieldInsn(Opcodes.PUTSTATIC, nmsPackage + "/event/CraftEventFactory", "blockDamage", "Lorg/bukkit/block/Block;");
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(2, 2);
			mv.visitEnd();

			cw.visitEnd();

			@SuppressWarnings("unchecked")
			Class<? extends CraftBukkitAccessor> clazz = (Class<? extends CraftBukkitAccessor>)ASMHelper.defineClass(ASMHelper.class.getClassLoader(), className, cw.toByteArray());
			return clazz;
		} catch (ReflectiveOperationException ex) {
			throw new RuntimeException("Error creating accessor", ex);
		}
	}

}
