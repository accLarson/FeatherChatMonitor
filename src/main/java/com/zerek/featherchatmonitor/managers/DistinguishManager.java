package com.zerek.featherchatmonitor.managers;

import com.zerek.featherchatmonitor.FeatherChatMonitor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.HashMap;
import java.util.Map;

public class DistinguishManager {

    private final FeatherChatMonitor plugin;

    private final Map<Permission,String> distinguishColorsMap = new HashMap<>();

    private String distinguishTag;

    public DistinguishManager(FeatherChatMonitor plugin) {

        this.plugin = plugin;

        this.init();
    }

    private void init() {

        plugin.getConfig().getConfigurationSection("distinguish.colors").getKeys(false).forEach(role -> {

            distinguishColorsMap.put(
                    new Permission("feather.chatmonitor.distinguish." + role, PermissionDefault.FALSE),
                    plugin.getConfig().getString("distinguish.colors." + role));
        });

        distinguishTag = plugin.getConfig().getString("distinguish.tag");
    }

    public Component distinguishMessage(Component message, Permission p) {

        message = message.replaceText(b -> b.matchLiteral(this.getDistinguishTag()).replacement(Component.text("")));

        return message.color(TextColor.fromCSSHexString(this.distinguishColorsMap.get(p)));
    }

    public Map<Permission, String> getDistinguishColorsMap() {
        return distinguishColorsMap;
    }

    public String getDistinguishTag() {
        return distinguishTag;
    }
}
