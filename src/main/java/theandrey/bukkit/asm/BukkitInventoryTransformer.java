package theandrey.bukkit.asm;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import theandrey.bukkit.event.util.asm.ASMHelper;

/**
 * Реализует недостающие методы Bukkit в {@link net.minecraft.inventory.IInventory}
 */
public class BukkitInventoryTransformer implements IClassTransformer {

	@Override
	public byte[] transform(String origName, String name, byte[] bytes) {
		try {
			switch (name) {
				case "thaumcraft.common.entities.InventoryMob":
					return patchThaumcraftEntityInventory(name, bytes, "net.minecraft.entity.Entity");
				case "thaumcraft.common.entities.golems.InventoryTrunk":
					return patchThaumcraftEntityInventory(name, bytes, "thaumcraft.common.entities.golems.EntityTravelingTrunk");
			}

		} catch (Throwable e) {
			LoadingPlugin.LOGGER.error("An error occurred while transforming class '" + name + "'", e);
		}

		return bytes;
	}

	private static byte[] patchThaumcraftEntityInventory(String name, byte[] bytes, String entClass) {
		LoadingPlugin.LOGGER.debug("Implementing Bukkit methods on '{}'", name);

		ClassNode node = ASMHelper.readClass(bytes);
		addTransactionField(node);
		addTransactionMethods(node);
		addGetContentsMethod(node, "inventory");
		addSetMaxStackSize(node, "stacklimit");
		addGetHolderEntity(node, "ent", ASMHelper.objType(entClass));

		return ASMHelper.writeClass(node, ClassWriter.COMPUTE_FRAMES);
	}

	private static void addTransactionField(ClassNode node) {
		node.visitField(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, "bukkitTransaction", "Ljava/util/List;", "Ljava/util/List<Lorg/bukkit/entity/HumanEntity;>;", null);

		for (MethodNode method : node.methods) {
			if (method.name.equals("<init>") && method.desc.endsWith(")V")) {
				ListIterator<AbstractInsnNode> it = method.instructions.iterator();

				while (it.hasNext()) {
					AbstractInsnNode insn = it.next();

					if (insn.getOpcode() == Opcodes.INVOKESPECIAL) {
						MethodInsnNode call = (MethodInsnNode)insn;
						if (call.name.equals("<init>") && call.desc.endsWith(")V") && !call.owner.equals(node.name)) {
							method.instructions.insertBefore(call.getPrevious(), transactionFieldInit(node)); // TODO: Пихает инструкцию перед super()
							break;
						}
					}
				}
			}
		}
	}

	private static InsnList transactionFieldInit(ClassNode node) {
		InsnList list = new InsnList();
		list.add(new VarInsnNode(Opcodes.ALOAD, 0));
		list.add(new TypeInsnNode(Opcodes.NEW, Type.getInternalName(ArrayList.class)));
		list.add(new InsnNode(Opcodes.DUP));
		list.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, Type.getInternalName(ArrayList.class), "<init>", Type.getMethodDescriptor(Type.VOID_TYPE), false));
		list.add(new FieldInsnNode(Opcodes.PUTFIELD, node.name, "bukkitTransaction", Type.getDescriptor(List.class)));
		return list;
	}

	private static void addTransactionMethods(ClassNode node) {
		final Type craftHuman = ASMHelper.objType(ASMHelper.getCbPackage() + ".entity.CraftHumanEntity");

		MethodVisitor mv = node.visitMethod(Opcodes.ACC_PUBLIC, "onOpen", Type.getMethodDescriptor(Type.VOID_TYPE, craftHuman), null, null);
		mv.visitCode();
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, node.name, "bukkitTransaction", Type.getDescriptor(List.class));
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, Type.getInternalName(List.class), "add", Type.getMethodDescriptor(Type.BOOLEAN_TYPE, Type.getType(Object.class)), true);
		mv.visitInsn(Opcodes.POP);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(1, 2);
		mv.visitEnd();

		mv = node.visitMethod(Opcodes.ACC_PUBLIC, "onClose", Type.getMethodDescriptor(Type.VOID_TYPE, craftHuman), null, null);
		mv.visitCode();
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, node.name, "bukkitTransaction", Type.getDescriptor(List.class));
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, Type.getInternalName(List.class), "remove", Type.getMethodDescriptor(Type.BOOLEAN_TYPE, Type.getType(Object.class)), true);
		mv.visitInsn(Opcodes.POP);
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(1, 2);
		mv.visitEnd();

		mv = node.visitMethod(Opcodes.ACC_PUBLIC, "getViewers", "()Ljava/util/List;", "()Ljava/util/List<Lorg/bukkit/entity/HumanEntity;>;", null);
		mv.visitCode();
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, node.name, "bukkitTransaction", Type.getDescriptor(List.class));
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(1, 1);
		mv.visitEnd();
	}

	/**
	 * Добавляет метод <b>getContents()</b>
	 * @param field Имя поля с массивом {@link net.minecraft.item.ItemStack}
	 */
	private static void addGetContentsMethod(ClassNode node, String field) {
		MethodVisitor mv = node.visitMethod(Opcodes.ACC_PUBLIC, "getContents", "()[Lnet/minecraft/item/ItemStack;", null, null);
		mv.visitCode();
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, node.name, field, "[Lnet/minecraft/item/ItemStack;");
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(1, 1);
		mv.visitEnd();
	}

	/**
	 * Добавляет метод <b>setMaxStackSize(int)</b>
	 * @param field Имя <code>int</code> поля отвечающего за лимит
	 */
	private static void addSetMaxStackSize(ClassNode node, String field) {
		MethodVisitor mv = node.visitMethod(Opcodes.ACC_PUBLIC, "setMaxStackSize", Type.getMethodDescriptor(Type.VOID_TYPE, Type.INT_TYPE), null, null);
		mv.visitCode();
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, node.name, field, Type.INT_TYPE.getDescriptor());
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(1, 2);
		mv.visitEnd();
	}

	/**
	 * Добавляет метод <b>getOwner()</b>
	 * @param field Имя поля с {@link net.minecraft.entity.Entity} или <code>null</code> если объект сам представляет из себя
	 * @param entDesc Описание типа поля
	 */
	private static void addGetHolderEntity(ClassNode node, String field, Type entDesc) {
		final Type invHolder = ASMHelper.objType("org.bukkit.inventory.InventoryHolder");
		final Type craftEntity = ASMHelper.objType(ASMHelper.getCbPackage() + ".entity.CraftEntity");

		MethodVisitor mv = node.visitMethod(Opcodes.ACC_PUBLIC, "getOwner", Type.getMethodDescriptor(invHolder), null, null);
		mv.visitCode();
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		if (field != null) mv.visitFieldInsn(Opcodes.GETFIELD, node.name, field, entDesc.getDescriptor());
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, entDesc.getInternalName(), "getBukkitEntity",
				Type.getMethodDescriptor(craftEntity), false);
		mv.visitVarInsn(Opcodes.ASTORE, 1);
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitTypeInsn(Opcodes.INSTANCEOF, invHolder.getInternalName());
		Label label;
		mv.visitJumpInsn(Opcodes.IFEQ, label = new Label());
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitTypeInsn(Opcodes.CHECKCAST, invHolder.getInternalName());
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitLabel(label);
		mv.visitInsn(Opcodes.ACONST_NULL);
		mv.visitInsn(Opcodes.ARETURN);
		mv.visitMaxs(2, 2);
		mv.visitEnd();
	}

}
