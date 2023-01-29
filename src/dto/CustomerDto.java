package dto;

public class CustomerDto {
    String id;
    String name;
    String address;
    Double price;

    public CustomerDto() {
    }

    public CustomerDto(String id, String name, String address, Double price) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.price = price;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getSalary() {
        return price;
    }

    public void setSalary(Double salary) {
        this.price = salary;
    }

    @Override
    public String toString() {
        return "CustomerDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", salary=" + price +
                '}';
    }
}
