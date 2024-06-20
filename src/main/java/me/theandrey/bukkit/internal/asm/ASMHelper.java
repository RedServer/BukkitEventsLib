package me.theandrey.bukkit.internal.asm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public final class ASMHelper {

	private ASMHelper() {
	}

	/**
	 * Читает класс из байт-кода
	 */
	public static ClassNode readClass(byte[] bytes) {
		if (bytes == null) throw new IllegalArgumentException("bytes is null");
		ClassNode node = new ClassNode();
		ClassReader reader = new ClassReader(bytes);
		reader.accept(node, 0);
		return node;
	}

	/**
	 * Преобразует класс обратно в байт-код
	 * @param node Класс
	 * @param options Флаги {@link ClassWriter#COMPUTE_MAXS}, {@link ClassWriter#COMPUTE_FRAMES}
	 * @return Байт-код
	 */
	public static byte[] writeClass(ClassNode node, int options) {
		if (node == null) throw new IllegalArgumentException("node is null");
		ClassWriter writer = new ClassWriter(options);
		node.accept(writer);
		return writer.toByteArray();
	}

	/**
	 * Ищет метод
	 * @param node Класс
	 * @param name Имя метода
	 * @param desc Сигнатура
	 * @return Метод
	 * @throws NoSuchMethodError Если метод не найден
	 */
	public static MethodNode findMethod(ClassNode node, String name, String desc) {
		if (node == null) throw new IllegalArgumentException("node is null");
		if (name == null) throw new IllegalArgumentException("name is null");

		for (MethodNode method : node.methods) {
			if (method.name.equals(name) && (desc == null || method.desc.equals(desc))) {
				return method;
			}
		}

		throw new NoSuchMethodError(node.name + "." + name + desc);
	}

	/**
	 * Ищет поле
	 * @param node Класс
	 * @param name Имя поля
	 * @throws NoSuchMethodError Если поле не найдено
	 */
	public static FieldNode findField(ClassNode node, String name) {
		if (node == null) throw new IllegalArgumentException("node is null");
		if (name == null) throw new IllegalArgumentException("name is null");

		for (FieldNode field : node.fields) {
			if (field.name.equals(name)) {
				return field;
			}
		}

		throw new NoSuchFieldError(node.name + "." + name);
	}

	/**
	 * Заменяет точки в имени класса на слеши
	 */
	public static String toInternalName(String name) {
		return name.replace('.', '/');
	}

	/**
	 * Возвращает тип объекта класса для описания в сигнатурах
	 * @param name Полное имя класса
	 */
	public static Type getObjectType(String name) {
		return Type.getObjectType(toInternalName(name));
	}

	/**
	 * Сохраняет дамп класса
	 * @param name Имя класса
	 * @param bytes Байты
	 */
	public static void saveDump(String name, byte[] bytes) {
		try {
			Path path = Paths.get("classdump", name.replace("/", ".") + ".class");
			Files.createDirectories(path.getParent());
			Files.write(path, bytes);
		} catch (IOException e) {
			LogManager.getLogger(ASMHelper.class).error("Unable to save class: " + name, e);
		}
	}

	/**
	 * Возвращает имя пакета CraftBukkit
	 */
	public static String getCraftPackage() {
		return Objects.requireNonNull(Bukkit.getServer(), "Server is not created")
			.getClass()
			.getPackage()
			.getName();
	}
}
