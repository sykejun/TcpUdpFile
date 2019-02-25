package com.kejun.trans.core.transmission.TcpMessage;


import com.kejun.trans.core.transmission.Transmission;
import com.kejun.trans.model.Equipment;

public class TcpMessage {
    private String       token;
    private Equipment    equipment;
    private Transmission transmission;
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Transmission getTransmission() {
        return transmission;
    }

    public void setTransmission(Transmission transmission) {
        this.transmission = transmission;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }
}
