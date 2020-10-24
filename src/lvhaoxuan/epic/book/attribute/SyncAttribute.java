package lvhaoxuan.epic.book.attribute;

import github.saukiya.sxattribute.SXAttribute;
import github.saukiya.sxattribute.data.attribute.SXAttributeData;
import java.util.ArrayList;
import java.util.List;
import lvhaoxuan.epic.book.EpicBook;
import lvhaoxuan.epic.book.manager.PlayerBookManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.serverct.ersha.jd.AttributeAPI;
import org.serverct.ersha.jd.attribute.AttributeData;

public class SyncAttribute implements Runnable {

    public static boolean apEnable = false;
    public static boolean sxEnable = false;

    public static void init() {
        if (EpicBook.ins.getServer().getPluginManager().getPlugin("AttributePlus") != null) {
            EpicBook.ins.getServer().getConsoleSender().sendMessage("§7[§e" + EpicBook.ins.getName() + "§7]§a检测到AttributePlus插件，属性模块加载");
            apEnable = true;
        }
        if (EpicBook.ins.getServer().getPluginManager().getPlugin("SX-Attribute") != null) {
            EpicBook.ins.getServer().getConsoleSender().sendMessage("§7[§e" + EpicBook.ins.getName() + "§7]§a检测到SX-Attribute插件，属性模块加载");
            sxEnable = true;
        }
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (apEnable) {
                AttributeAPI.addAttribute((Player) player, "EpicBook", PlayerBookManager.getTotalAttribute(player.getName()), false);
            }
            if (sxEnable) {
                SXAttributeData basic = SXAttribute.getApi().getAPIStats(player.getUniqueId());
                SXAttributeData data = SXAttribute.getApi().getLoreData(null, null, PlayerBookManager.getTotalAttribute(player.getName()));
                SXAttribute.getApi().setEntityAPIData(SyncAttribute.class, player.getUniqueId(), basic.add(data));
            }
        }
    }

    public static List<String> getAttributes(Player player) {
        List<String> ret = new ArrayList<>();
        if (apEnable) {
            AttributeData data = AttributeAPI.getAttrData(player);
            return data.getApiAttributeList("EpicBook");
        }
        if (sxEnable) {
        }
        return ret;
    }
}
