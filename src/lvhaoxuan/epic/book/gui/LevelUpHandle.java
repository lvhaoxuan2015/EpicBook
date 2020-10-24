package lvhaoxuan.epic.book.gui;

import java.util.ArrayList;
import java.util.List;
import lvhaoxuan.epic.book.EpicBook;
import lvhaoxuan.epic.book.manager.PlayerBookManager;
import lvhaoxuan.epic.book.obj.Book;
import lvhaoxuan.epic.book.obj.BookLine;
import lvhaoxuan.llib.api.LLibAPI;
import lvhaoxuan.llib.api.VaultAPI;
import lvhaoxuan.llib.gui.Gui;
import lvhaoxuan.llib.gui.GuiButton;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LevelUpHandle {

    public static VaultAPI vApi = new VaultAPI();

    public static void open(Player p, Book book) {
        String name = p.getName();
        ItemStack background;
        try {
            background = new ItemStack(Material.getMaterial("STAINED_GLASS_PANE"), 1, (byte) 7);
            ItemMeta meta = background.getItemMeta();
            meta.setDisplayName(" ");
            background.setItemMeta(meta);
        } catch (Exception ex) {
            background = new ItemStack(Material.getMaterial("BLACK_STAINED_GLASS_PANE"));
            ItemMeta meta = background.getItemMeta();
            meta.setDisplayName(" ");
            background.setItemMeta(meta);
        }
        Gui gui = new Gui(book.displayName, 9, background);
        int bookLevel = PlayerBookManager.getBookLevel(name, book);
        if (bookLevel < book.bookLines.size()) {
            ItemStack bookItem = book.item.clone();
            bookItem.addUnsafeEnchantment(Enchantment.LUCK, 1);
            ItemMeta meta = bookItem.getItemMeta();
            meta.setDisplayName(book.displayName);
            meta.setLore(PlayerBookManager.getBookLore(name, book));
            bookItem.setItemMeta(meta);
            gui.set(0, bookItem);

            BookLine next = book.bookLines.get(bookLevel);
            ItemStack item1 = new ItemStack(Material.DIAMOND_SWORD);
            ItemMeta meta1 = item1.getItemMeta();
            meta1.setDisplayName("§6§l图鉴升级");
            List<String> lore1 = new ArrayList();
            lore1.add("§7升级花费:");
            if (next.money != 0) {
                lore1.add("§7 - §a金币: §e" + next.money);
            }
            if (next.points != 0) {
                lore1.add("§7 - §a点券: §e" + next.points);
            }
            if (!next.items.isEmpty()) {
                for (ItemStack item : next.items) {
                    ItemMeta meta3 = item.getItemMeta();
                    lore1.add("§7 - §a物品: §e" + (meta3.hasDisplayName() ? meta3.getDisplayName() : item.getType().name()) + " * " + item.getAmount());
                }
            }
            meta1.setLore(lore1);
            item1.setItemMeta(meta1);
            GuiButton button = new GuiButton(8, item1) {
                @Override
                public void click(InventoryClickEvent e) {
                    if (!vApi.hasMoney(name, next.money)) {
                        p.sendMessage("§4金币不足");
                        return;
                    }
                    if (EpicBook.pApi.look(name) < next.points) {
                        p.sendMessage("§4点券不足");
                        return;
                    }
                    if (!LLibAPI.inventoryHasItems(p.getInventory(), next.items)) {
                        p.sendMessage("§4物品不足");
                        return;
                    }
                    vApi.takeMoney(name, next.money);
                    EpicBook.pApi.take(name, (int) next.points);
                    LLibAPI.inventoryRemoveItems(p.getInventory(), next.items);
                    PlayerBookManager.addBookLevel(name, book);
                    open(p, book);
                }
            };
            gui.addButton(button);

            ItemStack item2 = new ItemStack(Material.PAPER);
            ItemMeta meta2 = item2.getItemMeta();
            meta2.setDisplayName("§6§l下级图鉴属性");
            meta2.setLore(next.attributes);
            item2.setItemMeta(meta2);
            gui.set(4, item2);
        } else {
            ItemStack bookItem = book.item.clone();
            ItemMeta meta = bookItem.getItemMeta();
            meta.setDisplayName(book.displayName);
            meta.setLore(PlayerBookManager.getBookLore(name, book));
            bookItem.setItemMeta(meta);
            gui.set(4, bookItem);
        }
        gui.open(p);
    }
}
