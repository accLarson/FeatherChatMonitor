package com.zerek.featherchatmonitor.listeners;

import com.earth2me.essentials.Essentials;
import com.zerek.featherchatmonitor.FeatherChatMonitor;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AsyncChatListener implements Listener {

    private final FeatherChatMonitor plugin;
    private final Sound sound;
    private final float volume, pitch;
    private final String spamMessage, kickMessage;
    private final Essentials ess;


    public AsyncChatListener(FeatherChatMonitor plugin) {
        this.plugin = plugin;
        sound = Sound.valueOf(plugin.getConfig().getString("ping.sound"));
        volume = (float) plugin.getConfig().getDouble("ping.volume");
        pitch = (float) plugin.getConfig().getDouble("ping.pitch");
        spamMessage = plugin.getConfig().getString("spam-limit.warnings.warning");
        kickMessage = plugin.getConfig().getString("spam-limit.warnings.kick-message");
        ess = (Essentials) plugin.getServer().getPluginManager().getPlugin("Essentials");
    }

    @EventHandler
    public void onAsyncChatEvent(AsyncChatEvent event){
        if (!event.getPlayer().hasPermission("feather.spam.bypass") && plugin.getSpamManager().needsKick(event.getPlayer().getName())){
            plugin.getServer().getScheduler().runTask(plugin, () -> event.getPlayer().kick(MiniMessage.miniMessage().deserialize(kickMessage)));
        }
        if (!event.getPlayer().hasPermission("feather.spam.bypass") && plugin.getSpamManager().isSpam(event.getPlayer().getName())) {
            event.setCancelled(true);
            plugin.getSpamManager().addWarning(event.getPlayer().getName());
            event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize(spamMessage));
            plugin.getLogger().info("Spam detected from: " + event.getPlayer().getName() + "| Message suppressed: " + PlainTextComponentSerializer.plainText().serialize(event.message()));
        }
        else {
            plugin.getSpamManager().addMessage(event.getPlayer().getName());
            String message = PlainTextComponentSerializer.plainText().serialize(event.message());
            message = message.replace(".","");
            message = message.replace(",","");
            message = message.replace("\"","");
            message = message.replace("!","");
            message = message.replace("?","");
            message = message.replace("(","");
            message = message.replace(")","");
            message = message.toLowerCase();
            String finalMessage = message;

            List <Player> playersToPing = plugin.getServer().getOnlinePlayers().stream()
                    .filter(player -> Arrays.stream(finalMessage.split(" ")).collect(Collectors.toList()).contains(player.getName().toLowerCase()))
                    .collect(Collectors.toList());

            playersToPing.forEach(p -> {
                if (!ess.getUser(p).isIgnoredPlayer(ess.getUser(event.getPlayer()))) p.playSound(p.getLocation(), sound, volume, pitch);
            });
        }
    }
}
