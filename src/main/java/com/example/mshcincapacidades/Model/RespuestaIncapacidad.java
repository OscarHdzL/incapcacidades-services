package com.example.mshcincapacidades.Model;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class RespuestaIncapacidad {
    @Id
    public String _id;
    public Integer diasAcumulados;
}
