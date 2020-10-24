package lvhaoxuan.epic.book.api;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.items.ItemManager;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.inventory.ItemStack;

public class API {

    public static List<ItemStack> asItems(List<String> items) {
        List<ItemStack> ret = new ArrayList<>();
        for (String line : items) {
            if (line.contains("mm:")) {
                line = line.replace("mm:", "");
                String[] sub = line.split(" ");
                ItemManager im = MythicMobs.inst().getItemManager();
                ItemStack item_ = im.getItemStack(sub[0]);
                item_.setAmount(Integer.parseInt(sub[1]));
                ret.add(item_);
            } else {
                String[] sub = line.split(" ");
                ItemStack item_;
                if (sub[0].contains(":")) {
                    String[] sub_ = sub[0].split(":");
                    item_ = new ItemStack(Integer.parseInt(sub_[0]), 1, (short) Integer.parseInt(sub_[1]));
                } else {
                    item_ = new ItemStack(Integer.parseInt(sub[0]));
                }
                item_.setAmount(Integer.parseInt(sub[1]));
                ret.add(item_);
            }
        }
        return ret;
    }
}
