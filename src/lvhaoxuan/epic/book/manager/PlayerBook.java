package lvhaoxuan.epic.book.manager;

import java.util.HashMap;
import lvhaoxuan.epic.book.obj.Book;

public class PlayerBook {

    public HashMap<String, Integer> bookLevelMap = new HashMap<>();

    public void upBookLevel(Book book) {
        int maxLevel = book.bookLines.size();
        int level = getBookLevel(book);
        if (level < maxLevel) {
            bookLevelMap.put(book.name, level + 1);
        }
    }

    public int getBookLevel(String name) {
        return bookLevelMap.getOrDefault(name, 0);
    }

    public int getBookLevel(Book book) {
        return getBookLevel(book.name);
    }
}
