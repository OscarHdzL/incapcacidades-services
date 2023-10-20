package com.example.mshcincapacidades.Model;

import lombok.Data;

@Data
public class ModelDiagnosticoRequest {
    String num_nss;
    String agregado_medico;    
    //String diagnostico;    
    String delegacion_expedidora;    
    String unidad_expedidora;    
    String start;
    String end;
}
