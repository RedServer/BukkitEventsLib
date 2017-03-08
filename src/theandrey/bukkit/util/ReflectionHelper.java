package theandrey.bukkit.util;

import java.lang.reflect.Method;

/**
 * @author TheAndrey
 */
public final class ReflectionHelper {

	/**
	 * Найти метод в классе по названию.
	 * @param clazz Класс
	 * @param name Имя метода. Чуствительно к регистру.
	 * @return Первый подходящий по названию метод (так как не учитывает сигнатуру).
	 * @throws NoSuchMethodException Если указанный метод не найден.
	 */
	public static Method getMethodByName(Class<?> clazz, String name) throws NoSuchMethodException {
		if(clazz == null) throw new IllegalArgumentException("clazz is null!");
		if(name == null || name.isEmpty()) throw new IllegalArgumentException("name is null or empty!");

		for(Method method : clazz.getDeclaredMethods()) {
			if(method.getName().equals(name)) return method;
		}
		throw new NoSuchMethodException(name);
	}

}
