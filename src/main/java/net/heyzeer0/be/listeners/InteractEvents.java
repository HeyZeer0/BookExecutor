package net.heyzeer0.be.listeners;

import net.heyzeer0.be.Main;
import net.heyzeer0.be.configs.ConfigManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class InteractEvents implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (e.getPlayer().isSneaking() || !e.getPlayer().hasPermission("bookexecutor.execute")) return; // avoid executing if sneaking or without permission
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getItem() == null || e.getItem().getType() != Material.WRITTEN_BOOK) return;

        ItemStack book = e.getItem();
        BookMeta meta = (BookMeta) book.getItemMeta();
        if (meta == null || meta.getAuthor() == null) return;
        if (!meta.getAuthor().equalsIgnoreCase("BookExecutor")) return; // check if it's a command book

        StringBuilder command = new StringBuilder();
        for (String page : meta.getPages()) {
            command.append(page);
        }

        String finalCommand = command.toString();
        if (!finalCommand.startsWith("/")) finalCommand = "/" + finalCommand;

        // special effects!!
        if (ConfigManager.getPlayerConfig(e.getPlayer()).isEffectsEnabled()) {
            e.getPlayer().getWorld().strikeLightningEffect(e.getPlayer().getLocation());
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 5));
        }

        // dispatching command
        // the reason for using chat instead of dispatchCommand is because plugins like WorldEdit
        // doesn't detects it somehow!
        e.getPlayer().chat(finalCommand);

        e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,
                new ComponentBuilder("Command Dispatched")
                        .color(ChatColor.GREEN)
                        .create());

        e.setCancelled(true);
    }

}
