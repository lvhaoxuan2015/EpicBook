package lvhaoxuan.epic.book.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lvhaoxuan.epic.book.EpicBook;
import lvhaoxuan.epic.book.obj.Book;
import lvhaoxuan.epic.book.obj.BookLine;
import lvhaoxuan.llib.api.LLibAPI;
import org.bukkit.inventory.ItemStack;

public class PlayerBookManager {

    public static HashMap<String, PlayerBook> bookMap = new HashMap<>();

    public static void check(String name) {
        if (!bookMap.containsKey(name)) {
            bookMap.put(name, new PlayerBook());
        }
    }

    public static void addBookLevel(String name, Book book) {
        PlayerBook playerBook = get(name);
        playerBook.upBookLevel(book);
    }

    public static int getBookLevel(String name, Book book) {
        PlayerBook playerBook = get(name);
        return playerBook.getBookLevel(book.name);
    }

    public static List<String> getAttributeLore(String name, Book book) {
        int level = getBookLevel(name, book);
        if (book.bookLines.size() > 0) {
            if (level > 0) {
                BookLine bookLine = book.bookLines.get(level - 1);
                List<String> lore = new ArrayList<>();
                for (String line : bookLine.attributes) {
                    lore.add(line);
                }
                return lore;
            }
        }
        return new ArrayList<>();
    }

    public static List<String> getBookLore(String name, Book book) {
        int level = getBookLevel(name, book);
        if (book.bookLines.size() > 0) {
            if (level > 0) {
                BookLine bookLine = book.bookLines.get(level - 1);
                List<String> lore = new ArrayList<>();
                for (String line : bookLine.attributes) {
                    lore.add("§a§l已激活:§r " + line);
                }
                return lore;
            } else {
                BookLine bookLine = book.bookLines.get(0);
                List<String> lore = new ArrayList<>();
                for (String line : bookLine.attributes) {
                    lore.add("§8§l未激活:§r §m" + LLibAPI.removeColor(line));
                }
                return lore;
            }
        }
        return new ArrayList<>();
    }

    public static List<String> getTotalAttribute(String name) {
        List<String> ret = new ArrayList<>();
        for (Book book : EpicBook.books) {
            ret.addAll(getAttributeLore(name, book));
        }
        return ret;
    }

    public static Returner reset(String name) {
        PlayerBook playerBook = get(name);
        Returner returner = new Returner();
        for (Book book : EpicBook.books) {
            int level = getBookLevel(name, book);
            for (int i = 0; i < level; i++) {
                BookLine line = book.bookLines.get(i);
                returner.money += line.money;
                returner.points += line.points;
                returner.items.addAll(line.items);
            }
            playerBook.bookLevelMap.remove(book.name);
        }
        return returner;
    }

    public static PlayerBook get(String name) {
        check(name);
        return bookMap.get(name);
    }

    public static class Returner {

        public double money = 0;
        public double points = 0;
        public List<ItemStack> items = new ArrayList<>();
    }
}
