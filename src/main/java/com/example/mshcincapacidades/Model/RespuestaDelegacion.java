package com.example.mshcincapacidades.Model;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class RespuestaDelegacion {
    @Id
    public String _id;
    public String delegacion_expedidora;
}
