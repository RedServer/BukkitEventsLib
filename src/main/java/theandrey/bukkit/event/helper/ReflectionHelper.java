package theandrey.bukkit.event.helper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.annotation.Nullable;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import org.apache.commons.lang3.StringUtils;

/**
 * @author TheAndrey
 */
public final class ReflectionHelper {

	/**
	 * Найти метод в классе по названию.
	 * @param clazz Класс
	 * @param name Имя метода. Чувствительно к регистру.
	 * @return Первый подходящий по названию метод (так как не учитывает сигнатуру).
	 * @throws NoSuchMethodException Если указанный метод не найден.
	 */
	public static Method getMethodByName(Class<?> clazz, String name) throws NoSuchMethodException {
		if (clazz == null) throw new IllegalArgumentException("clazz is null!");
		if (name == null || name.isEmpty()) throw new IllegalArgumentException("name is null or empty!");

		for (Method method : clazz.getDeclaredMethods()) {
			if (method.getName().equals(name)) return method;
		}
		throw new NoSuchMethodException(name);
	}

	/**
	 * Finds a field with the specified name in the given class and makes it accessible.
	 * Note: for performance, store the returned value and avoid calling this repeatedly.
	 * <p>
	 * Throws an exception if the field is not found.
	 * @param clazz The class to find the field on.
	 * @param fieldName The name of the field to find (used in developer environments, i.e. "maxStackSize").
	 * @param fieldObfName The obfuscated name of the field to find (used in obfuscated environments, i.e. "maxStackSize").
	 * If the name you are looking for is on a class that is never obfuscated, this should be null.
	 * @return The field with the specified name in the given class.
	 */
	public static Field findField(Class<?> clazz, String fieldName, @Nullable String fieldObfName) throws NoSuchFieldException {
		Preconditions.checkNotNull(clazz);
		Preconditions.checkArgument(StringUtils.isNotEmpty(fieldName), "Field name cannot be empty");

		String nameToFind = FMLLaunchHandler.isDeobfuscatedEnvironment() ? fieldName : MoreObjects.firstNonNull(fieldObfName, fieldName);

		try {
			Field field = clazz.getDeclaredField(nameToFind);
			field.setAccessible(true);
			return field;
		} catch (Exception e) {
			throw new NoSuchFieldException(e.getMessage());
		}
	}
}
