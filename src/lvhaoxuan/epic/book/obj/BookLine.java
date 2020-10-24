package lvhaoxuan.epic.book.obj;

import java.util.List;
import org.bukkit.inventory.ItemStack;

public class BookLine {

    public List<String> attributes;
    public List<ItemStack> items;
    public double points;
    public double money;

    public BookLine(List<String> attributes, List<ItemStack> items, double points, double money) {
        this.attributes = attributes;
        this.items = items;
        this.points = points;
        this.money = money;
    }
}
