package com.example.mshcincapacidades.Model;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class RespuestaUnidad {
    @Id
    public String _id;
    public String unidad_expedidora;
}
