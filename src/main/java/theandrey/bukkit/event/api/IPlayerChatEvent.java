package theandrey.bukkit.event.api;

/**
 * Реализуется эвентами чата
 */
public interface IPlayerChatEvent extends IPlayerEvent {

	/**
	 * Получить текст сообщения
	 * @return
	 */
	public String getMessage();

	/**
	 * Изменить текст сообщения
	 * @param newmessage Новое сообщение
	 */
	public void setMessage(String newmessage);

}
