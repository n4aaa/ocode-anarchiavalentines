package pl.ocode.anarchiavalentines.util;

import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ProtocolUtil {
    public static void broadcastPlayerPacket(ProtocolManager manager, PacketContainer packet, Player player){
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!(p.getWorld().equals(player.getWorld()) && p.getLocation().distance(player.getLocation()) < Bukkit.getViewDistance() * 16 && !p.equals(player))) {
                continue;
            }

            manager.sendServerPacket(p, packet);
        }
    }
}
