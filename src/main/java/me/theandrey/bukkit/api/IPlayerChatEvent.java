package me.theandrey.bukkit.api;

/**
 * Реализуется ивентами чата
 */
public interface IPlayerChatEvent extends IPlayerEvent {

	/**
	 * Получить текст сообщения
	 */
	String getMessage();

	/**
	 * Изменить текст сообщения
	 * @param message Новое сообщение
	 */
	void setMessage(String message);

}
