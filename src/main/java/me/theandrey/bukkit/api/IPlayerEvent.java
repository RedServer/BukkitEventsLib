package me.theandrey.bukkit.api;

import org.bukkit.entity.Player;

/**
 * Реализуется ивентами, которые вызываются игроком
 */
public interface IPlayerEvent {

	Player getPlayer();

}
