package com.zerek.featherchatmonitor.listeners;

import com.earth2me.essentials.Essentials;
import com.zerek.featherchatmonitor.FeatherChatMonitor;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
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

        // Distinguish coloring system ---------------------------------------------------------------------------------

        if (event.getPlayer().hasPermission("feather.chatmonitor.distinguish") && PlainTextComponentSerializer.plainText().serialize(event.message()).startsWith(plugin.getDistinguishTag())) {

            event.message(event.message().replaceText(b -> b.matchLiteral(plugin.getDistinguishTag()).replacement(Component.text(""))));

            if (event.getPlayer().hasPermission("group.administrator")) event.message(event.message().color(TextColor.fromCSSHexString(plugin.getDistinguishColors().get("administrator"))));

            else if (event.getPlayer().hasPermission("group.moderator")) event.message(event.message().color(TextColor.fromCSSHexString(plugin.getDistinguishColors().get("moderator"))));

            else if (event.getPlayer().hasPermission("group.assistant")) event.message(event.message().color(TextColor.fromCSSHexString(plugin.getDistinguishColors().get("assistant"))));

            else if (event.getPlayer().hasPermission("group.donor")) event.message(event.message().color(TextColor.fromCSSHexString(plugin.getDistinguishColors().get("donor"))));
        }

        // Anti chat spam system ---------------------------------------------------------------------------------------

        if (!event.getPlayer().hasPermission("feather.chatmonitor.spam.bypass") && plugin.getSpamManager().needsKick(event.getPlayer().getName())){

            plugin.getServer().getScheduler().runTask(plugin, () -> event.getPlayer().kick(MiniMessage.miniMessage().deserialize(kickMessage)));
        }

        if (!event.getPlayer().hasPermission("feather.chatmonitor.spam.bypass") && plugin.getSpamManager().isSpam(event.getPlayer().getName())) {

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

            // Player Ping system --------------------------------------------------------------------------------------

            List <Player> playersToPing = plugin.getServer().getOnlinePlayers().stream()
                    .filter(player -> Arrays.stream(finalMessage.split(" ")).collect(Collectors.toList()).contains(player.getName().toLowerCase()))
                    .collect(Collectors.toList());

            playersToPing.forEach(p -> {

                if (!ess.getUser(p).isIgnoredPlayer(ess.getUser(event.getPlayer()))) p.playSound(p.getLocation(), sound, volume, pitch);
            });
        }
    }
}
