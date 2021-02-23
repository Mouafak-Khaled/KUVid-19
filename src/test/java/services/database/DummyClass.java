package services.database;

public class DummyClass {

    String name;
    int age;

    public DummyClass(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public DummyClass() {
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
