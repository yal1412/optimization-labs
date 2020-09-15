package ru.sberbank.demo1;

import org.springframework.stereotype.Service;
import ru.sberbank.demo1.Item;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemFetcher {

    public Map<LocalDate, List<Item>> marketItems(LocalDate valueDate, LocalDate compareToDate) {
        return getLocalDateListMap(valueDate, compareToDate, 200);
    }

    public Map<LocalDate, List<Item>> internetItems(LocalDate valueDate, LocalDate compareToDate) {
        return getLocalDateListMap(valueDate, compareToDate, 200);
    }

    private Map<LocalDate, List<Item>> getLocalDateListMap(LocalDate valueDate, LocalDate compareToDate, int values) {
        Map<LocalDate, List<Item>> itemsByDate = new HashMap<>();
        for (LocalDate date = valueDate; compareToDate.isAfter(date); date = date.plusDays(1)) {
            List<Item> items = new ArrayList<>();
            for (int i = 0; i < values; i++) {
                if (i % 100 == 0) {
                    items.add(new Item("Apple", "store", i, 0));
                } else {
                    items.add(new Item("name", "store", i, i * 10));
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            itemsByDate.put(date, items);
        }
        return itemsByDate;
    }
}
