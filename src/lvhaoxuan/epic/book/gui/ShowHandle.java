package lvhaoxuan.epic.book.gui;

import java.util.ArrayList;
import java.util.List;
import lvhaoxuan.epic.book.EpicBook;
import lvhaoxuan.epic.book.attribute.SyncAttribute;
import static lvhaoxuan.epic.book.gui.LevelUpHandle.vApi;
import lvhaoxuan.epic.book.manager.PlayerBookManager;
import lvhaoxuan.epic.book.manager.PlayerBookManager.Returner;
import lvhaoxuan.epic.book.obj.Book;
import lvhaoxuan.llib.gui.PageGui;
import lvhaoxuan.llib.gui.GuiButton;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ShowHandle {

    public static void open(Player player) {
        String name = player.getName();
        ItemStack buttonItemLeft;
        ItemStack buttonItemRight;
        try {
            buttonItemLeft = new ItemStack(Material.getMaterial("STAINED_GLASS_PANE"), 1, (byte) 5);
            ItemMeta meta1 = buttonItemLeft.getItemMeta();
            meta1.setDisplayName("§a§l上一页");
            buttonItemLeft.setItemMeta(meta1);
            buttonItemRight = new ItemStack(Material.getMaterial("STAINED_GLASS_PANE"), 1, (byte) 14);
            ItemMeta meta2 = buttonItemRight.getItemMeta();
            meta2.setDisplayName("§4§l下一页");
            buttonItemRight.setItemMeta(meta2);
        } catch (Exception ex) {
            buttonItemLeft = new ItemStack(Material.getMaterial("GREEN_STAINED_GLASS_PANE"));
            ItemMeta meta1 = buttonItemLeft.getItemMeta();
            meta1.setDisplayName("§a§l上一页");
            buttonItemLeft.setItemMeta(meta1);
            buttonItemRight = new ItemStack(Material.getMaterial("RED_STAINED_GLASS_PANE"));
            ItemMeta meta2 = buttonItemRight.getItemMeta();
            meta2.setDisplayName("§4§l下一页");
            buttonItemRight.setItemMeta(meta2);
        }
        int pageSize = getPageSize(EpicBook.books.size());
        PageGui gui = new PageGui(EpicBook.title, pageSize, EpicBook.books, new GuiButton(pageSize - 9, buttonItemLeft), new GuiButton(pageSize - 1, buttonItemRight), 0, pageSize - 9) {

            @Override
            public ItemStack handleItem(Object obj) {
                Book book = (Book) obj;
                ItemStack bookItem = book.item.clone();
                bookItem.addUnsafeEnchantment(Enchantment.LUCK, 1);
                ItemMeta meta = bookItem.getItemMeta();
                meta.setDisplayName(book.displayName);
                meta.setLore(PlayerBookManager.getBookLore(name, book));
                bookItem.setItemMeta(meta);
                return bookItem;
            }

            @Override
            public void onClick(Object obj, InventoryClickEvent e) {
                LevelUpHandle.open(player, (Book) obj);
            }
        };
        ItemStack item = new ItemStack(Material.ANVIL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6§l还原");
        List<String> lore = new ArrayList();
        lore.add("§7还原得到升级图鉴所得物品");
        meta.setLore(lore);
        item.setItemMeta(meta);
        GuiButton button1 = new GuiButton(pageSize - 5, item) {
            @Override
            public void click(InventoryClickEvent e) {
                if (!vApi.hasMoney(name, EpicBook.resetCostMoney)) {
                    player.sendMessage("§4金币不足");
                    return;
                }
                if (EpicBook.pApi.look(name) < EpicBook.resetCostPoint) {
                    player.sendMessage("§4点券不足");
                    return;
                }
                vApi.takeMoney(name, EpicBook.resetCostMoney);
                EpicBook.pApi.take(name, (int) EpicBook.resetCostPoint);
                Returner returner = PlayerBookManager.reset(name);
                vApi.giveMoney(name, returner.money);
                EpicBook.pApi.give(name, (int) returner.points);
                for (ItemStack item : returner.items) {
                    player.getInventory().addItem(item);
                }
                open(player);
            }
        };
        gui.addButton(button1);
        ItemStack item1 = new ItemStack(Material.PAPER);
        ItemMeta meta1 = item1.getItemMeta();
        meta1.setDisplayName("§6§l图鉴属性");
        meta1.setLore(SyncAttribute.getAttributes(player));
        item1.setItemMeta(meta1);
        GuiButton button2 = new GuiButton(pageSize - 3, item1);
        gui.addButton(button2);
        gui.open(player);
    }

    public static int getPageSize(int fakeSize) {
        if (fakeSize <= 9) {
            return 18;
        }
        if (fakeSize <= 18) {
            return 27;
        }
        if (fakeSize <= 27) {
            return 36;
        }
        if (fakeSize <= 36) {
            return 45;
        }
        return 54;
    }
}
