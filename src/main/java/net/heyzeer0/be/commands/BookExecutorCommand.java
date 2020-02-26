package net.heyzeer0.be.commands;

import net.heyzeer0.be.configs.ConfigManager;
import net.heyzeer0.be.configs.objects.PlayerConfig;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BookExecutorCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof ConsoleCommandSender) return true;

        Player p = (Player) sender;
        if (args.length == 0) {
            sender.sendMessage(ChatColor.DARK_RED + "Use: " + ChatColor.RED + "/be <toggleEffects/get/list>");
            return true;
        }

        if (args[0].equalsIgnoreCase("toggleEffects")) {
            PlayerConfig config = ConfigManager.getPlayerConfig(p);
            config.setEffectsEnabled(!config.isEffectsEnabled());

            if (config.isEffectsEnabled()) {
                p.sendMessage(ChatColor.GREEN + "Successfully enabled visual effects.");
                return true;
            }

            p.sendMessage(ChatColor.GREEN + "Successfully disabled visual effects.");
            return true;
        }

        if (args[0].equalsIgnoreCase("list")) {
            PlayerConfig config = ConfigManager.getPlayerConfig(p);

            if (config.listStoredBooks().isEmpty()) {
                p.sendMessage(ChatColor.RED + "You've never created an executable book before!");
                return true;
            }

            p.sendMessage(ChatColor.YELLOW + "Listing all available books:");


            for (String book : config.listStoredBooks()) {
                TextComponent clickToLoad = new TextComponent("Click here to get " + book + " book!");
                clickToLoad.setColor(net.md_5.bungee.api.ChatColor.GREEN);

                TextComponent component = new TextComponent(" > " + book);
                component.setColor(net.md_5.bungee.api.ChatColor.GREEN);
                component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/be get " + book));
                component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder().append("Click here to get " + book + " book!")
                                .color(net.md_5.bungee.api.ChatColor.GREEN)
                        .create()
                ));

                p.spigot().sendMessage(component);
            }

            return true;
        }

        if (args[0].equalsIgnoreCase("get")) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.DARK_RED + "Use: " + ChatColor.RED + "/be get <name>");
            }

            StringJoiner bookNameBuilder = new StringJoiner(" ");
            for (int i = 1; i < args.length; i++) {
                bookNameBuilder.add(args[i]);
            }
            String book = bookNameBuilder.toString();

            PlayerConfig config = ConfigManager.getPlayerConfig(p);

            if (!config.hasBook(book)) {
                p.sendMessage(ChatColor.RED + "The provided book doesn't exists!");
                return true;
            }

            ItemStack itemBook = new ItemStack(Material.WRITTEN_BOOK, 1);
            BookMeta meta = (BookMeta) itemBook.getItemMeta();

            meta.setTitle(book);
            meta.setPages(config.getBook(book));
            meta.setAuthor("BookExecutor");

            itemBook.setItemMeta(meta);

            p.getInventory().addItem(itemBook);
            p.sendMessage(ChatColor.GREEN + "Book added to your inventory successfully!");
            return true;
        }

        sender.sendMessage(ChatColor.DARK_RED + "Use: " + ChatColor.RED + "/be <toggleEffects/get/list>");
        return true;
    }

    private static final String[] completes = new String[] {"toggleEffects", "get", "list"};

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length <= 1) {
            return Stream.of(completes).filter(
                    c -> (args.length != 1 || c.startsWith(args[0]))
            ).collect(Collectors.toList());
        }

        if (args[0].equalsIgnoreCase("get")) {
            PlayerConfig config = ConfigManager.getPlayerConfig((Player)commandSender);
            return config.listStoredBooks().stream().filter(
                    c -> (args.length != 2 || c.startsWith(args[1]))
            ).collect(Collectors.toList());
        }

        return null;
    }
}
