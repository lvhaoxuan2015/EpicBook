package lvhaoxuan.epic.book;

import java.util.ArrayList;
import java.util.List;
import lvhaoxuan.epic.book.attribute.SyncAttribute;
import lvhaoxuan.epic.book.commander.Commander;
import lvhaoxuan.epic.book.loader.Loader;
import lvhaoxuan.epic.book.obj.Book;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class EpicBook extends JavaPlugin {

    public static EpicBook ins;
    public static String title;
    public static List<Book> books = new ArrayList<>();
    public static PlayerPointsAPI pApi;
    public static double resetCostPoint;
    public static double resetCostMoney;

    @Override
    public void onEnable() {
        ins = this;
        this.getServer().getConsoleSender().sendMessage("§7[§e" + this.getName() + "§7]§a作者lvhaoxuan(隔壁老吕)|QQ3295134931");
        if (this.getServer().getPluginManager().getPlugin("PlayerPoints") != null) {
            pApi = ((PlayerPoints) this.getServer().getPluginManager().getPlugin("PlayerPoints")).getAPI();
            this.getServer().getConsoleSender().sendMessage("§7[§e" + this.getName() + "§7]§a检测到PlayerPoints插件，模块加载");
        }
        this.getServer().getPluginCommand("eb").setExecutor(new Commander());
        SyncAttribute.init();
        loadFile();
        Bukkit.getScheduler().runTaskTimerAsynchronously(EpicBook.ins, new SyncAttribute(), 0, 2);
    }

    public static void loadFile() {
        Loader.loadConfig();
        Loader.loadBooks();
        Loader.loadPlayers();
    }

    @Override
    public void onDisable() {
        Loader.savePlayers();
    }
}
