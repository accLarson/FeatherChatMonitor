package dev.zerek.featherchatmonitor.managers;

import dev.zerek.featherchatmonitor.FeatherChatMonitor;
import dev.zerek.featherchatmonitor.data.DistinguishGroup;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.HashMap;
import java.util.Map;

public class DistinguishManager {

    private final FeatherChatMonitor plugin;

    private final Map<Permission, DistinguishGroup> distinguishMap = new HashMap<>();

    private String tag;

    public DistinguishManager(FeatherChatMonitor plugin) {

        this.plugin = plugin;

        this.init();
    }

    private void init() {

        plugin.getConfig().getConfigurationSection("distinguish.groups").getKeys(false).forEach(group -> distinguishMap.put(
                new Permission("feather.chatmonitor.distinguish." + group, PermissionDefault.FALSE),
                new DistinguishGroup(plugin.getConfig().getInt("distinguish.groups." + group + ".priority"), plugin.getConfig().getString("distinguish.groups." + group+ ".color"))
                ));

        tag = plugin.getConfig().getString("distinguish.tag");
    }

    public Component distinguishMessage(Component message, Permission p) {

        message = message.replaceText(b -> b.matchLiteral(this.getTag()).replacement(Component.text("")));

        return message.color(TextColor.fromCSSHexString(this.distinguishMap.get(p).getColor()));
    }

    public Map<Permission, DistinguishGroup> getDistinguishMap() {
        return distinguishMap;
    }


    public String getTag() {
        return tag;
    }
}
