// AdminRoulette.java
package com.example.adminroulette;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.UUID;

public class AdminRoulette extends JavaPlugin {
    private FileConfiguration config;
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();
    private static final long COOLDOWN_TIME = 30 * 1000; // 30�루���뵥λ��

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();
        getCommand("admin").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("�벻Ҫ�ڷ�������̨ʹ�ô�ָ���Ϊ����Ҳû��awa");
            return true;
        }

        Player player = (Player) sender;
        if (!player.isOp()) {
            player.sendMessage("��c��op��Ҳ���ʹ�ô�ָ��Ŷ");
            return true;
        }

        UUID uuid = player.getUniqueId();
        if (cooldowns.containsKey(uuid)) {
            long remaining = (cooldowns.get(uuid) + COOLDOWN_TIME - System.currentTimeMillis()) / 1000;
            if (remaining > 0) {
                player.sendMessage("��eʹ��Ƶ�ʹ��죡��ȴʱ��ʣ�� " + remaining + " ��");
                return true;
            }
        }

        String adminName = config.getString("server-owner");
        Player admin = Bukkit.getPlayer(adminName);
        
        if (admin == null || !admin.isOnline()) {
            player.sendMessage("��c�������˵�ǰ������Ŷ");
            return true;
        }

        if (Math.random() < 0.5) {
            admin.setHealth(0);
            Bukkit.broadcastMessage("��4" + player.getName() + " ������������ܺ�");
        } else {
            player.setHealth(0);
            Bukkit.broadcastMessage("��6" + player.getName() + " ��������������Ǻܺ���");
        }

        cooldowns.put(uuid, System.currentTimeMillis());
        return true;
    }
}