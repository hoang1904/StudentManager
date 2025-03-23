

package model;

import java.io.Serializable;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private int age;
    private String id_faculty;

    public Student() {
    }

    public Student(int id, String name, int age, String id_faculty) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.id_faculty = id_faculty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getIdFaculty() {
        return id_faculty;
    }

    public void setIdFaculty(String id_faculty) {
        this.id_faculty = id_faculty;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", id_faculty='" + id_faculty + '\'' +
                '}';
    }
}



