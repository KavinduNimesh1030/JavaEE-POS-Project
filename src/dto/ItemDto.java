package dto;

public class ItemDto {
    String id;
    String name;
    int qty;
    double salary;

    public ItemDto() {
    }

    public ItemDto(String id, String name, int qty, double salary) {
        this.id = id;
        this.name = name;
        this.qty = qty;
        this.salary = salary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return salary;
    }

    public void setPrice(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "ItemDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", qty=" + qty +
                ", salary=" + salary +
                '}';
    }
}
