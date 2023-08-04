package com.bbm.employeeservice.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MissionStatus {

    ABERTO("Aberto"),
    PENDENTE("Pendente"),
    CANCELADO("Cancelado"),
    CONCLUIDO("Concluído");

    private String name;

    MissionStatus(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
