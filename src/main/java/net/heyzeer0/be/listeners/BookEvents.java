package net.heyzeer0.be.listeners;

import net.heyzeer0.be.configs.ConfigManager;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.meta.BookMeta;

public class BookEvents implements Listener {

    /**
     * Detects and register a command executor book!
     */
    @EventHandler
    public void onWrite(PlayerEditBookEvent e) {
        if (!e.isSigning() || !e.getPlayer().hasPermission("bookexecutor.create")) return;

        BookMeta m = e.getNewBookMeta();
        if (m.getTitle() == null || !m.getTitle().startsWith("exec: ")) return; // find the pattern

        // special effects
        if (ConfigManager.getPlayerConfig(e.getPlayer()).isEffectsEnabled()) {
            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f);
            e.getPlayer().getWorld().spawnParticle(Particle.SPELL_WITCH, 2f, 2f, 2f, 10, 2f, 2f, 2f);
        }

        // updating the item
        m.setTitle(m.getTitle().replaceFirst("exec: ", ""));
        m.setAuthor("BookExecutor");

        // saving
        ConfigManager.getPlayerConfig(e.getPlayer()).setStoredBook(m.getTitle(), m.getPages());

        e.setNewBookMeta(m);
    }

}
