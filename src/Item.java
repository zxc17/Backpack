public class Item {
    String name;
    int weight;
    int cost;

    public Item(String name, int weight, int cost) {
        this.name = name;
        if (weight <= 0 || cost <= 0)
            throw new IllegalArgumentException(String.format(
                    "Значения должны быть больше нуля. weight=%d, cost=%d", weight, cost));
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
