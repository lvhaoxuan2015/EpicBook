package lvhaoxuan.epic.book.obj;

import java.util.List;
import org.bukkit.inventory.ItemStack;

public class Book {

    public String name;
    public ItemStack item;
    public String displayName;
    public List<BookLine> bookLines;

    public Book(String name, ItemStack item, String displayName, List<BookLine> bookLines) {
        this.name = name;
        this.item = item;
        this.displayName = displayName;
        this.bookLines = bookLines;
    }
}
