import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Backpack {
    private final String NEGATIVE_CAPACITY = "Вместимость должна быть больше нуля. capacity=%d";
    private final String TOO_HEAVY = "Вес предметов превышает вместимость. weight=%d, capacity=%d";
    private final int capacity;     // Вместимость рюкзака.
    private final int cost;         // Суммарная стоимость предметов в рюкзаке.
    private List<Item> items;       // Список предметов в рюкзаке.

    /**
     * Конструктор проверяет возможность уложить данный набор предметов в новый рюкзак, и создает рюкзак с предметами.
     *
     * @param capacity Вместимость нового рюкзака.
     * @param items    Предметы, которые необходимо положить в рюкзак.
     */
    public Backpack(int capacity, List<Item> items) {
        if (capacity <= 0) throw new IllegalArgumentException(String.format(NEGATIVE_CAPACITY, capacity));
        int weight = 0; // Вес всех предметов списка
        int cost = 0;   // Стоимость всех предметов списка
        for (Item item : items) {
            weight += item.weight;
            cost += item.cost;
        }
        if (weight > capacity) throw new IllegalArgumentException(String.format(TOO_HEAVY, weight, capacity));
        this.capacity = capacity;
        this.cost = cost;
        this.items = items;
    }

    /**
     * Конструктор создает новый пустой рюкзак указанной вместимости.
     *
     * @param capacity Вместимость нового рюкзака.
     */
    public Backpack(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException(String.format(NEGATIVE_CAPACITY, capacity));
        this.capacity = capacity;
        this.cost = 0;
        items = new ArrayList<>();
    }

    public static void main(String[] args) {
        Backpack backpack = new Backpack(10);
        List<Item> newItems = new ArrayList<>();
        newItems.add(new Item("a", 1, 1));
        newItems.add(new Item("b", 2, 2));
        newItems.add(new Item("c", 3, 3));
        newItems.add(new Item("d", 2, 3));
        newItems.add(new Item("e", 5, 5));
        newItems.add(new Item("f", 6, 7));
        newItems.add(new Item("g", 2, 4));
        newItems.add(new Item("h", 4, 6));

        backpack.loadBackpack(newItems);
        System.out.println(backpack.items);
    }

    /**
     * Загрузка рюкзака.
     * Метод выбирает из полученного списка предметы, которые помещаются в рюкзак
     * (ограничение по весу) и имеют наибольшую стоимость.
     * Результат сохраняется в поле items.
     *
     * @param newItems Список предметов для заполнения рюкзака.
     */
    public void loadBackpack(List<Item> newItems) {
        // partialLoad - набор рюкзаков вместимостью от минимальной до необходимой.
        // Ключ - допустимый вес рюкзака,
        // значение - рюкзак с самыми дорогими предметами для данного веса на текущей итерации.
        // prevPartialLoad - аналогичная мапа, хранящая значения с предыдущей итерации.
        Map<Integer, Backpack> prevPartialLoad = new HashMap<>();
        Map<Integer, Backpack> partialLoad = new HashMap<>();
        for (int w = 1; w <= capacity; w++) {
            prevPartialLoad.put(w, new Backpack(w));  // Стартовые значения, рюкзак не заполнен.
        }
        for (Item item : newItems) {
            for (int w = 1; w <= capacity; w++) {
                if (item.weight == w) { // Вес предмета точно равен вместимости рюкзака.
                    if (item.cost > prevPartialLoad.get(w).cost) {
                        // Если он дороже проверенных ранее - заменяем.
                        partialLoad.put(w, new Backpack(w, List.of(item)));
                    } else {
                        // Иначе - сохраняем список предметов с предыдущего шага итерации.
                        partialLoad.put(w, prevPartialLoad.get(w));
                    }
                } else if (item.weight < w) { // Предмет помещается в рюкзак с запасом.
                    // Вычисляем новую стоимость.
                    // Новый предмет плюс самые дорогие с нужным добавочным весом, полученные на предыдущей итерации.
                    int newCost = item.cost + prevPartialLoad.get(w - item.weight).cost;
                    // Если она больше - кладем данный набор в рюкзак.
                    if (newCost > prevPartialLoad.get(w).cost) {
                        // В качестве добавочных предметов берем самые дорогие с нужным добавочным весом,
                        // полученные на предыдущей итерации.
                        List<Item> newList = new ArrayList<>(prevPartialLoad.get(w - item.weight).items);
                        // Добавляем новый предмет.
                        newList.add(item);
                        // И сохраняем.
                        partialLoad.put(w, new Backpack(w, newList));
                    } else {
                        // Новая стоимость меньше - сохраняем список предметов с предыдущего шага итерации.
                        partialLoad.put(w, prevPartialLoad.get(w));
                    }
                } else {
                    // Новый предмет не помещается в рюкзак, сохраняем список предметов с предыдущего шага итерации.
                    partialLoad.put(w, prevPartialLoad.get(w));
                }
            }
            prevPartialLoad = partialLoad;
            partialLoad = new HashMap<>();
        }
        items = prevPartialLoad.get(capacity).items;
    }

    public static class Item {
        private final String NEGATIVE_VALUES = "Значения должны быть больше нуля. weight=%d, cost=%d";
        String name;
        int weight;
        int cost;

        public Item(String name, int weight, int cost) {
            if (weight <= 0 || cost <= 0)
                throw new IllegalArgumentException(String.format(NEGATIVE_VALUES, weight, cost));
            this.name = name;
            this.weight = weight;
            this.cost = cost;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "name='" + name + '\'' +
                    ", weight=" + weight +
                    ", cost=" + cost +
                    '}';
        }
    }

}
