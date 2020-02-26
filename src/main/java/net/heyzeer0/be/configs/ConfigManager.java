package net.heyzeer0.be.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.heyzeer0.be.Main;
import net.heyzeer0.be.configs.objects.PlayerConfig;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class ConfigManager {

    private static File PLAYER_FOLDER = new File(Main.getPlugin().getDataFolder(), "players");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static HashMap<UUID, PlayerConfig> cachedPlayerConfigs = new HashMap<>();

    public static PlayerConfig getPlayerConfig(Player p) {
        return getPlayerConfig(p.getUniqueId());
    }

    public static PlayerConfig getPlayerConfig(UUID uuid) {
        if (cachedPlayerConfigs.containsKey(uuid)) return cachedPlayerConfigs.get(uuid);

        File f = new File(PLAYER_FOLDER, uuid.toString() + ".json");
        if (!f.exists()) return new PlayerConfig(uuid);

        try {
            FileReader reader = new FileReader(f);
            PlayerConfig output = GSON.fromJson(reader, PlayerConfig.class);

            cachedPlayerConfigs.put(uuid, output);
            reader.close();
            return output;
        }catch (Exception ex) {
            ex.printStackTrace();
            return new PlayerConfig(uuid);
        }
    }

    public static void savePlayerConfig(PlayerConfig config) throws IOException {
        File f = new File(PLAYER_FOLDER, config.getUuid().toString() + ".json");
        PLAYER_FOLDER.mkdirs();

        if (!f.exists()) f.createNewFile();

        FileWriter writer = new FileWriter(f);
        GSON.toJson(config, writer);
        writer.close();
    }

}
