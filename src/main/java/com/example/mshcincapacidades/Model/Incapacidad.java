package com.example.mshcincapacidades.Model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

//@Document(value="incapacidades_nssa")
@Document(value="incapacidades")
@Data
public class Incapacidad {
    
    @Id
    private String id;
    private String cve_idee_fecha_atencion;
    private String delegacion_expedidora;
    private String clave_unidad_expedidora;
    private String unidad_expedidora;
    private String cve_idee;
    private String num_nss;
    private String agregado_medico;
    private Date fecha_expedicion;
    private String folio;
    private Date fecha_inicio;
    private Date fecha_termino;
    private Integer dias_acumulados;
    private String dias_autorizados;
    private String dias_probables_recuperacion;
    private String ramo_seguro;
    private String tipo_incapacidad;
    private String ocupacion;
    private String nombre_patron;
    private String diagnostico;
    private String clave_presupuestal_origen;
    private String origen_dato;
    private String fecha_proceso;
    private String yearmonth;
}
