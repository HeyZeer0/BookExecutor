package net.heyzeer0.be.configs.objects;

import net.heyzeer0.be.configs.ConfigManager;

import java.util.*;

public class PlayerConfig {

    UUID uuid;

    boolean effectsEnabled;
    HashMap<String, List<String>> bookStorage;

    public PlayerConfig(UUID uuid, boolean enableEffects, HashMap<String, List<String>> bookStorage) {
        this.uuid = uuid;
        this.effectsEnabled = enableEffects;
        this.bookStorage = bookStorage;
    }

    public PlayerConfig(UUID uuid) {
        this(uuid, true, new HashMap<>());
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean isEffectsEnabled() {
        return effectsEnabled;
    }

    public List<String> getBook(String name) {
        return bookStorage.getOrDefault(name, null);
    }

    public boolean hasBook(String name) {
        return bookStorage.containsKey(name);
    }

    public Set<String> listStoredBooks() {
        return bookStorage.keySet();
    }

    public void setStoredBook(String name, List<String> pages) {
        bookStorage.put(name, pages);

        save();
    }

    public void setEffectsEnabled(boolean effectsEnabled) {
        this.effectsEnabled = effectsEnabled;

        save();
    }

    private void save() {
        try {
            ConfigManager.savePlayerConfig(this);
        }catch (Exception ex) {

            ex.printStackTrace();
        }
    }

}
