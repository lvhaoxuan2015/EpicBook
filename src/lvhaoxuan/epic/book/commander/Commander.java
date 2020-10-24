package lvhaoxuan.epic.book.commander;

import lvhaoxuan.epic.book.EpicBook;
import lvhaoxuan.epic.book.gui.ShowHandle;
import lvhaoxuan.epic.book.loader.Loader;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

public class Commander implements CommandExecutor {

    public static String head = "§c[§6" + EpicBook.ins.getName() + EpicBook.ins.getDescription().getVersion() + "§c]§e";

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("open")) {
            ShowHandle.open((Player) sender);
        } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            Loader.savePlayers();
            EpicBook.loadFile();
            sender.sendMessage(head + "重载成功");
        }
        return true;
    }
}
