package model;

import org.hibernate.annotations.GenericGenerator;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @NotNull
    @Size(min = 1, message = "Не может быть пустым")
    @Column(name = "model")
    private String model = "";

    @NotNull
    @Size(min = 1, message = "Не может быть пустым")
    @Column(name = "number")
    private String number = "";

    @NotNull
    @Size(min = 1, message = "Не может быть пустым")
    @Column(name = "performer")
    private String performer = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NotNull
    public String getPerformer() {
        return performer;
    }

    public void setPerformer(@NotNull String performer) {
        this.performer = performer;
    }

    @NotNull
    public String getModel() {
        return model;
    }

    public void setModel(@NotNull String model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return model+" "+number;
    }

    @NotNull
    public String getNumber() {
        return number;
    }

    public void setNumber(@NotNull String number) {
        this.number = number;
    }
}
