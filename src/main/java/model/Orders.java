package model;


import org.hibernate.annotations.GenericGenerator;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "orders")
public class Orders {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @ManyToOne
    @JoinColumn(name = "stockId")
    private Stock stock;

    @ManyToOne
    @JoinColumn(name = "vehicleId")
    private Vehicle vehicle;

    @NotNull
    @Column(name = "date")
    private Date date = new Date();

    @NotNull
    @Size(min = 1, message = "Не может быть пустым")
    @Column(name = "destination")
    private String destination = "";

    @Column(name = "comment")
    private String comment = "";

    @Column(name = "price")
    private double price = 0.0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NotNull
    public Stock getStock() {
        return stock;
    }

    public void setStock(@NotNull Stock stock) {
        this.stock = stock;
    }

    @NotNull
    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(@NotNull Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @NotNull
    public Date getDate() {
        return date;
    }

    public void setDate(@NotNull Date date) {
        this.date = date;
    }

    @NotNull
    public String getDestination() {
        return destination;
    }

    public void setDestination(@NotNull String destination) {
        this.destination = destination;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
