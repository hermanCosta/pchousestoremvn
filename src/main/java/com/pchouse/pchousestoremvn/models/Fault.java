package com.pchouse.pchousestoremvn.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "FAULT")
public class Fault implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_FAULT")
    private long idFault;

    @NotNull
    @Size(max = 255)  // Descrição pode ter até 255 caracteres
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    public Fault() {
    }

    public Fault(String description) {
        this.description = description;
    }

    public long getIdFault() {
        return idFault;
    }

    public void setIdFault(long idFault) {
        this.idFault = idFault;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
//    @Override
//    public String toString() {
//        return "Fault{" +
//                "idFault=" + idFault +
//                ", description='" + description + '\'' +
//                '}';
//    }
}
