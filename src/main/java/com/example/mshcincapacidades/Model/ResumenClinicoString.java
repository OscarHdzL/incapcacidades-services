package com.example.mshcincapacidades.Model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Document(value="resumen_clinico_string")
@Data
public class ResumenClinicoString {
     @Id
    private String id;
    public Date fecha_consulta;
    public String origen;
    public String cve_idee_fecha_atencion;
    public String cve_idee;
    public String cx_curp;
    @Field(name = "cc_nss")
    public String nss;
    @Field(name = "cc_agregado_med")
    public String agregadoMedico;
    public String cx_nombre;
    public String cx_ap_paterno;
    public String cx_ap_materno;
    public String edad;
    public Date fecha_nacimiento;
    public String delegacion;
    public String sexo;
    public String ocupacion;
    public String cie10;
    public String diagnostico;
    public String consecutivo_diagnostico;
    public String ocasion_servicio_diag;
    public String tipo_diagnostico;
    public String complemento_dx;
    public String cie9;
    public String procedimientos_cie9;
    public String consecutivo_procedimiento;
    public String ocasion_servicio_proced;
    public String tipo_procedimiento;
    public String complemento_px;
    public String sinonimo;
    public String especialidad;
    public String motivo_no_realizar_exploracion;
    public String exploracion_fisica;
    public String peso;
    public String talla;
    public String temperatura;
    public String ta_sistolica;
    public String ta_diastolica;
    public String saturacion;
    public String imc;
    public String frec_cardiaca;
    public String frec_respiratoria;
    public String glucosa;
    public String hipertension_arterial;
    public String interrogatorio;
    public String ciclo_menstrual;
    public String meses_gestacion;
    public String fecha_ultima_menstruacion;
    public String fecha_probable_parto;
    public String numero_de_embarazos;
    public String numero_de_abortos;
    public String numero_cesareas;
    public String antecedentes_obstetricos;
    public String antecedentes_patologicos;
    public String lugar_accidente;
    public String indicaciones_adicionales;
    public String resumen_clinico;
    public String cc_presupuestal;
    public String unidad;
    public String matricula;
    public String cedula_profesional;
    public String nombre_medico;
    public String consultorio_atencion;
    public String des_turno;
    public String clave_presupuestal_origen;
    public String origen_dato;
    public String fecha_proceso;
    public String yearmonth;
}
