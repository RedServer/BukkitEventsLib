package me.theandrey.bukkit.internal.asm;

import java.lang.reflect.Method;
import me.theandrey.bukkit.internal.CraftBukkitAccessor;
import me.theandrey.bukkit.internal.ReflectionHelper;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * Генерирует класс-реализацию {@link CraftBukkitAccessor} по требованию
 * @author TheAndrey
 */
public class CraftBukkitAccessorGenerator implements IClassTransformer {

	public static final String IMPLEMENTATION_CLASS = "me.theandrey.bukkit.internal.CraftBukkitAccessorImpl";

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if (name.equals(IMPLEMENTATION_CLASS)) {
			try {
				return makeAccessorClass();
			} catch (Exception e) {
				throw new RuntimeException("Unable to make accessor class", e);
			}
		}

		return basicClass;
	}

	/**
	 * Создаёт класс доступа к методам CraftBukkit
	 */
	private byte[] makeAccessorClass() throws Exception {
		ClassWriter cw = new ClassWriter(0);
		cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL | Opcodes.ACC_SUPER, IMPLEMENTATION_CLASS.replace('.', '/'), null, Type.getInternalName(Object.class), new String[]{Type.getInternalName(CraftBukkitAccessor.class)});
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
		final String nmsPackage = ASMHelper.getCraftPackage().replace(".", "/");

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

		return cw.toByteArray();
	}

}
