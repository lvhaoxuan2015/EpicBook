package lvhaoxuan.epic.book.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lvhaoxuan.epic.book.EpicBook;
import lvhaoxuan.epic.book.api.API;
import lvhaoxuan.epic.book.manager.PlayerBook;
import lvhaoxuan.epic.book.manager.PlayerBookManager;
import lvhaoxuan.epic.book.obj.Book;
import lvhaoxuan.epic.book.obj.BookLine;
import lvhaoxuan.llib.loader.LoaderUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class Loader {

    public static void loadBooks() {
        EpicBook.books.clear();
        File folder = EpicBook.ins.getDataFolder();
        if (!folder.exists()) {
            folder.mkdir();
        }
        File file = new File(folder, "books");
        if (!file.exists()) {
            file.mkdir();
        }
        for (File bookFile : file.listFiles()) {
            EpicBook.books.add(loadBook(bookFile));
        }
    }

    public static Book loadBook(File book) {
        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(book), "UTF-8");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(reader);
            String name = config.getString("Name");
            ItemStack item = LoaderUtil.readItemStack(config, "Item");
            String displayName = config.getString("DisplayName");
            List<BookLine> bookLines = new ArrayList<>();
            ConfigurationSection section = config.getConfigurationSection("BookLines");
            for (String key : section.getKeys(false)) {
                List<String> attributes = config.getStringList("BookLines." + key + ".Attributes");
                List<ItemStack> items = API.asItems(config.getStringList("BookLines." + key + ".Items"));
                double points = config.getDouble("BookLines." + key + ".Points");
                double money = config.getDouble("BookLines." + key + ".Money");
                bookLines.add(new BookLine(attributes, items, points, money));
            }
            EpicBook.ins.getServer().getConsoleSender().sendMessage("§a载入图鉴: " + name);
            return new Book(name, item, displayName, bookLines);
        } catch (UnsupportedEncodingException | FileNotFoundException ex) {
        }
        return null;
    }

    public static void loadConfig() {
        if (!EpicBook.ins.getDataFolder().exists()) {
            EpicBook.ins.getDataFolder().mkdir();
        }
        File file = new File(EpicBook.ins.getDataFolder(), "config.yml");
        if (!file.exists()) {
            EpicBook.ins.saveResource("config.yml", true);
        }
        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "UTF-8");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(reader);
            EpicBook.title = config.getString("Title");
            EpicBook.resetCostMoney = config.getDouble("ResetCostMoney");
            EpicBook.resetCostPoint = config.getDouble("ResetCostPoint");
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
        }
    }

    public static void loadPlayers() {
        File file = new File(EpicBook.ins.getDataFolder(), "players.yml");
        if (!file.exists()) {
            EpicBook.ins.saveResource("players.yml", true);
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String name : config.getKeys(false)) {
            PlayerBook playerBook = new PlayerBook();
            ConfigurationSection section = config.getConfigurationSection(name);
            for (String key : section.getKeys(false)) {
                playerBook.bookLevelMap.put(key, config.getInt(name + "." + key));
            }
            PlayerBookManager.bookMap.put(name, playerBook);
        }
    }

    public static void savePlayers() {
        File file = new File(EpicBook.ins.getDataFolder(), "players.yml");
        if (!file.exists()) {
            EpicBook.ins.saveResource("players.yml", true);
        }
        YamlConfiguration config = new YamlConfiguration();
        for (String name : PlayerBookManager.bookMap.keySet()) {
            for (Map.Entry<String, Integer> entry : PlayerBookManager.get(name).bookLevelMap.entrySet()) {
                config.set(name + "." + entry.getKey(), entry.getValue());
            }
        }
        try {
            config.save(new File(EpicBook.ins.getDataFolder(), "players.yml"));
        } catch (IOException ex) {
        }
    }
}
