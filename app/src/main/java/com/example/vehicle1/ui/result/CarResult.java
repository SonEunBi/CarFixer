package com.example.vehicle1.ui.result;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class CarResult {

    private String gate1;
    private String gate2;
    private String gate1_message;
    private String gate2_message;

    private String location;
    private String severity;
    private String final_result;
    private String gate1_result;
    private String gate2_result;
    @Override
    public String toString() {
        return "CarResult{" +
                "gate1='" + gate1 + '\'' +
                ", gate2='" + gate2 + '\'' +
                ", gate1_message='" + gate1_message + '\'' +
                ", gate2_message='" + gate2_message + '\'' +
                ", location='" + location + '\'' +
                ", severity='" + severity + '\'' +
                ", final_result='" + final_result + '\'' +
                ", gate1_result='" + gate1_result + '\'' +
                ", gate2_result='" + gate2_result + '\'' +
                '}';
    }

    public String getGate1_result() {
        return gate1_result;
    }

    public void setGate1_result(String gate1_result) {
        this.gate1_result = gate1_result;
    }

    public String getGate2_result() {
        return gate2_result;
    }

    public void setGate2_result(String gate2_result) {
        this.gate2_result = gate2_result;
    }

    public String getGate1() {
        return gate1;
    }

    public void setGate1(String gate1) {
        this.gate1 = gate1;
    }

    public String getGate2() {
        return gate2;
    }

    public void setGate2(String gate2) {
        this.gate2 = gate2;
    }

    public String getGate1_message() {
        return gate1_message;
    }

    public void setGate1_message(String gate1_message) {
        this.gate1_message = gate1_message;
    }

    public String getGate2_message() {
        return gate2_message;
    }

    public void setGate2_message(String gate2_message) {
        this.gate2_message = gate2_message;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getFinal_result() {
        return final_result;
    }

    public void setFinal_result(String final_result) {
        this.final_result = final_result;
    }


}