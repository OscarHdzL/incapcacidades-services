package com.example.mshcincapacidades.Service;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.previousOperation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.bson.BsonNull;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.aggregation.UnionWithOperation;

import org.springframework.data.mongodb.core.query.Criteria;

import org.springframework.stereotype.Service;

import com.example.mshcincapacidades.Model.DelegacionRequest;
import com.example.mshcincapacidades.Model.DiagnosticoRequest;
import com.example.mshcincapacidades.Model.Incapacidad;
import com.example.mshcincapacidades.Model.IncapacidadNSSA;
import com.example.mshcincapacidades.Model.IncapacidadesRequest;
import com.example.mshcincapacidades.Model.ModelDelegacionRequest;
import com.example.mshcincapacidades.Model.ModelDiagnosticoRequest;
import com.example.mshcincapacidades.Model.ModelUnidadesRequest;
import com.example.mshcincapacidades.Model.RespuestaDelegacion;
import com.example.mshcincapacidades.Model.RespuestaDiagnostico;
import com.example.mshcincapacidades.Model.RespuestaIncapacidad;
import com.example.mshcincapacidades.Model.RespuestaUnidad;
import com.example.mshcincapacidades.Model.ResumenClinicoString;
import com.example.mshcincapacidades.Model.UnidadRequest;
import com.example.mshcincapacidades.Model.DTO.IncapacidadDTO;
import com.example.mshcincapacidades.Repository.IncapacidadNSSARepository;
import com.example.mshcincapacidades.Repository.IncapacidadRepository;
import com.example.mshcincapacidades.Repository.ResumenClinicoStringRepository;
import com.example.mshcincapacidades.utils.CustomAggregationOperation;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncapacidadNSSAService {

        @Autowired
        private MongoTemplate mt;

        @Autowired
        private final IncapacidadNSSARepository incapacidadNSSARepository;

        @Autowired
        private final IncapacidadRepository incapacidadRepository;

        @Autowired
        private final ResumenClinicoStringRepository resumenClinicoStringRepository;

        /*
         * public Optional<IncapacidadNSSA> findIncapacidadByFolio(String folio){
         * 
         * return incapacidadNSSARepository.findIncapacidadNSSAByFolio(folio);
         * }
         */


         public List<RespuestaDiagnostico> findDiagnosticos(DiagnosticoRequest request) throws ParseException{


                List<RespuestaDiagnostico> diagnosticos = new ArrayList<>();

                if(request.getModel().getStart() != null && request.getModel().getEnd() != null && request.getModel().getDelegacion_expedidora() == null && request.getModel().getUnidad_expedidora() == null){

                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date fromDate = dateFormat.parse(request.getModel().getStart());
                        Date toDate = dateFormat.parse(request.getModel().getEnd());

                        Calendar c1 = Calendar.getInstance();
                        c1.setTime(toDate);
                        c1.add(Calendar.DATE, 1);
                        toDate = c1.getTime();

                        diagnosticos = incapacidadRepository.findDiagnosticosByNssAgregadoFechas(request.getModel().getNum_nss(), request.getModel().getAgregado_medico(), fromDate, toDate);        

                }else if(request.getModel().getStart() != null && request.getModel().getEnd() != null && request.getModel().getDelegacion_expedidora() != null && request.getModel().getUnidad_expedidora() == null){

                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date fromDate = dateFormat.parse(request.getModel().getStart());
                        Date toDate = dateFormat.parse(request.getModel().getEnd());

                        Calendar c1 = Calendar.getInstance();
                        c1.setTime(toDate);
                        c1.add(Calendar.DATE, 1);
                        toDate = c1.getTime();

                        diagnosticos = incapacidadRepository.findDiagnosticosByNssAgregadoFechasDelegacion(request.getModel().getNum_nss(), request.getModel().getAgregado_medico(), fromDate, toDate, request.getModel().getDelegacion_expedidora());        
                        

                }else if(request.getModel().getStart() != null && request.getModel().getEnd() != null && request.getModel().getDelegacion_expedidora() == null && request.getModel().getUnidad_expedidora() != null){
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date fromDate = dateFormat.parse(request.getModel().getStart());
                        Date toDate = dateFormat.parse(request.getModel().getEnd());

                        Calendar c1 = Calendar.getInstance();
                        c1.setTime(toDate);
                        c1.add(Calendar.DATE, 1);
                        toDate = c1.getTime();

                        diagnosticos = incapacidadRepository.findDiagnosticosByNssAgregadoFechasUnidad(request.getModel().getNum_nss(), request.getModel().getAgregado_medico(), fromDate, toDate, request.getModel().getUnidad_expedidora());        
                        
                }else if(request.getModel().getStart() != null && request.getModel().getEnd() != null && request.getModel().getDelegacion_expedidora() != null && request.getModel().getUnidad_expedidora() != null){
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date fromDate = dateFormat.parse(request.getModel().getStart());
                        Date toDate = dateFormat.parse(request.getModel().getEnd());

                        Calendar c1 = Calendar.getInstance();
                        c1.setTime(toDate);
                        c1.add(Calendar.DATE, 1);
                        toDate = c1.getTime();

                        diagnosticos = incapacidadRepository.findDiagnosticosByNssAgregadoFechasDelegacionUnidad(request.getModel().getNum_nss(), request.getModel().getAgregado_medico(), fromDate, toDate, request.getModel().getDelegacion_expedidora(),request.getModel().getUnidad_expedidora());        

                }else if(request.getModel().getStart() == null && request.getModel().getEnd() == null && request.getModel().getDelegacion_expedidora() != null && request.getModel().getUnidad_expedidora() == null){
                        
                        diagnosticos = incapacidadRepository.findDiagnosticosByNssAgregadoDelegacion(request.getModel().getNum_nss(), request.getModel().getAgregado_medico(), request.getModel().getDelegacion_expedidora());        

                }else if(request.getModel().getStart() == null && request.getModel().getEnd() == null && request.getModel().getDelegacion_expedidora() == null && request.getModel().getUnidad_expedidora() != null){
                        diagnosticos = incapacidadRepository.findDiagnosticosByNssAgregadoUnidad(request.getModel().getNum_nss(), request.getModel().getAgregado_medico(), request.getModel().getUnidad_expedidora());        
                        
                }else if(request.getModel().getStart() == null && request.getModel().getEnd() == null && request.getModel().getDelegacion_expedidora() == null && request.getModel().getUnidad_expedidora() == null){
                        diagnosticos = incapacidadRepository.findDiagnosticosByNssAgregado(request.getModel().getNum_nss(), request.getModel().getAgregado_medico());        
                }
                return diagnosticos;
         }

         public List<RespuestaUnidad> findUnidades(UnidadRequest request) throws ParseException{


                List<RespuestaUnidad> unidades = new ArrayList<>();

                if(request.getModel().getStart() != null && request.getModel().getEnd() != null && request.getModel().getDelegacion_expedidora() == null){

                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date fromDate = dateFormat.parse(request.getModel().getStart());
                        Date toDate = dateFormat.parse(request.getModel().getEnd());

                        Calendar c1 = Calendar.getInstance();
                        c1.setTime(toDate);
                        c1.add(Calendar.DATE, 1);
                        toDate = c1.getTime();

                        unidades = incapacidadRepository.findUnidadesByNssAgregadoFechas(request.getModel().getNum_nss(), request.getModel().getAgregado_medico(), fromDate, toDate);        

                }else if(request.getModel().getStart() != null && request.getModel().getEnd() != null && request.getModel().getDelegacion_expedidora() != null){

                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date fromDate = dateFormat.parse(request.getModel().getStart());
                        Date toDate = dateFormat.parse(request.getModel().getEnd());

                        Calendar c1 = Calendar.getInstance();
                        c1.setTime(toDate);
                        c1.add(Calendar.DATE, 1);
                        toDate = c1.getTime();

                        unidades = incapacidadRepository.findUnidadesByNssAgregadoFechasDelegacion(request.getModel().getNum_nss(), request.getModel().getAgregado_medico(), fromDate, toDate, request.getModel().getDelegacion_expedidora());        
                        

                }else if(request.getModel().getStart() == null && request.getModel().getEnd() == null && request.getModel().getDelegacion_expedidora() != null){

                        unidades = incapacidadRepository.findUnidadesByNssAgregadoDelegacion(request.getModel().getNum_nss(), request.getModel().getAgregado_medico(), request.getModel().getDelegacion_expedidora());        
                        

                }
                else if(request.getModel().getStart() == null && request.getModel().getEnd() == null && request.getModel().getDelegacion_expedidora() == null){
                        unidades = incapacidadRepository.findUnidadesByNssAgregado(request.getModel().getNum_nss(), request.getModel().getAgregado_medico());        
                }
                return unidades;
         }

         public List<RespuestaDelegacion> findDelegaciones(DelegacionRequest request) throws ParseException{


                List<RespuestaDelegacion> delegaciones = new ArrayList<>();

                delegaciones = incapacidadRepository.findDelegacionesByNssAgregado(request.getModel().getNum_nss(), request.getModel().getAgregado_medico());        
                
                return delegaciones;
         }

        public IncapacidadDTO findIncapacidadByFolioNSSAgregado(String folio, String cc_nss,
                        String cc_agregado_medico) {

                Optional<IncapacidadNSSA> datosIncapacidadNSSA = incapacidadNSSARepository
                                .findIncapacidadNSSAByFolio(folio);

                if (!datosIncapacidadNSSA.isEmpty()) {

                        IncapacidadDTO incapacidadDto = new IncapacidadDTO();
                        incapacidadDto.setId(
                                        !isBlankOrNull(datosIncapacidadNSSA.get().getId())
                                                        ? datosIncapacidadNSSA.get().getId()
                                                        : "");
                        incapacidadDto
                                        .setCve_idee_fecha_atencion(!isBlankOrNull(
                                                        datosIncapacidadNSSA.get().getCve_idee_fecha_atencion())
                                                                        ? datosIncapacidadNSSA.get()
                                                                                        .getCve_idee_fecha_atencion()
                                                                        : "");
                        incapacidadDto
                                        .setDelegacion_expedidora(!isBlankOrNull(
                                                        datosIncapacidadNSSA.get().getDelegacion_expedidora())
                                                                        ? datosIncapacidadNSSA.get()
                                                                                        .getDelegacion_expedidora()
                                                                        : "");
                        incapacidadDto
                                        .setClave_unidad_expedidora(!isBlankOrNull(
                                                        datosIncapacidadNSSA.get().getClave_unidad_expedidora())
                                                                        ? datosIncapacidadNSSA.get()
                                                                                        .getClave_unidad_expedidora()
                                                                        : "");
                        incapacidadDto.setUnidad_expedidora(
                                        !isBlankOrNull(datosIncapacidadNSSA.get().getUnidad_expedidora())
                                                        ? datosIncapacidadNSSA.get().getUnidad_expedidora()
                                                        : "");
                        incapacidadDto.setCve_idee(
                                        !isBlankOrNull(datosIncapacidadNSSA.get().getCve_idee())
                                                        ? datosIncapacidadNSSA.get().getCve_idee()
                                                        : "");
                        incapacidadDto.setNum_nss(
                                        !isBlankOrNull(datosIncapacidadNSSA.get().getNum_nss())
                                                        ? datosIncapacidadNSSA.get().getNum_nss()
                                                        : "");
                        incapacidadDto.setAgregado_medico(
                                        !isBlankOrNull(datosIncapacidadNSSA.get().getAgregado_medico())
                                                        ? datosIncapacidadNSSA.get().getAgregado_medico()
                                                        : "");
                        incapacidadDto.setFecha_expedicion(datosIncapacidadNSSA.get().getFecha_expedicion());
                        incapacidadDto.setFolio(
                                        !isBlankOrNull(datosIncapacidadNSSA.get().getFolio())
                                                        ? datosIncapacidadNSSA.get().getFolio()
                                                        : "");
                        incapacidadDto.setFecha_inicio(datosIncapacidadNSSA.get().getFecha_inicio());
                        incapacidadDto.setFecha_termino(datosIncapacidadNSSA.get().getFecha_termino());
                        incapacidadDto.setDias_acumulados(
                                        !isBlankOrNull(datosIncapacidadNSSA.get().getDias_acumulados())
                                                        ? datosIncapacidadNSSA.get().getDias_acumulados()
                                                        : "");
                        incapacidadDto.setDias_autorizados(
                                        !isBlankOrNull(datosIncapacidadNSSA.get().getDias_autorizados())
                                                        ? datosIncapacidadNSSA.get().getDias_autorizados()
                                                        : "");
                        incapacidadDto.setDias_probables_recuperacion(
                                        !isBlankOrNull(datosIncapacidadNSSA.get().getDias_probables_recuperacion())
                                                        ? datosIncapacidadNSSA.get().getDias_probables_recuperacion()
                                                        : "");
                        incapacidadDto.setRamo_seguro(!isBlankOrNull(datosIncapacidadNSSA.get().getRamo_seguro())
                                        ? datosIncapacidadNSSA.get().getRamo_seguro()
                                        : "");
                        incapacidadDto.setTipo_incapacidad(
                                        !isBlankOrNull(datosIncapacidadNSSA.get().getTipo_incapacidad())
                                                        ? datosIncapacidadNSSA.get().getTipo_incapacidad()
                                                        : "");
                        incapacidadDto.setOcupacion(!isBlankOrNull(datosIncapacidadNSSA.get().getOcupacion())
                                        ? datosIncapacidadNSSA.get().getOcupacion()
                                        : "");
                        incapacidadDto.setNombre_patron(!isBlankOrNull(datosIncapacidadNSSA.get().getNombre_patron())
                                        ? datosIncapacidadNSSA.get().getNombre_patron()
                                        : "");
                        incapacidadDto.setDiagnostico(!isBlankOrNull(datosIncapacidadNSSA.get().getDiagnostico())
                                        ? datosIncapacidadNSSA.get().getDiagnostico()
                                        : "");
                        incapacidadDto.setClave_presupuestal_origen(
                                        !isBlankOrNull(datosIncapacidadNSSA.get().getClave_presupuestal_origen())
                                                        ? datosIncapacidadNSSA.get().getClave_presupuestal_origen()
                                                        : "");
                        incapacidadDto.setOrigen_dato(!isBlankOrNull(datosIncapacidadNSSA.get().getOrigen_dato())
                                        ? datosIncapacidadNSSA.get().getOrigen_dato()
                                        : "");
                        incapacidadDto.setFecha_proceso(datosIncapacidadNSSA.get().getFecha_proceso());
                        incapacidadDto.setYearmonth(!isBlankOrNull(datosIncapacidadNSSA.get().getYearmonth())
                                        ? datosIncapacidadNSSA.get().getYearmonth()
                                        : "");

                        Optional<ResumenClinicoString> resumen = resumenClinicoStringRepository
                                        .findTop1ByNssAndAgregadoMedico(cc_nss, cc_agregado_medico);

                        if (!datosIncapacidadNSSA.isEmpty()) {
                                incapacidadDto.setConsultorio_atencion(
                                                !isBlankOrNull(resumen.get().getConsultorio_atencion())
                                                                ? resumen.get().getConsultorio_atencion()
                                                                : "");
                                incapacidadDto
                                                .setDes_turno(!isBlankOrNull(resumen.get().getDes_turno())
                                                                ? resumen.get().getDes_turno()
                                                                : "");
                        }

                        return incapacidadDto;

                } else {

                        Optional<Incapacidad> datosIncapacidad = incapacidadRepository.findIncapacidadByFolio(folio);

                        if (!datosIncapacidad.isEmpty()) {

                                IncapacidadDTO incapacidadDto = new IncapacidadDTO();
                                incapacidadDto
                                                .setId(!isBlankOrNull(datosIncapacidad.get().getId())
                                                                ? datosIncapacidad.get().getId()
                                                                : "");
                                incapacidadDto
                                                .setCve_idee_fecha_atencion(!isBlankOrNull(
                                                                datosIncapacidad.get().getCve_idee_fecha_atencion())
                                                                                ? datosIncapacidad.get()
                                                                                                .getCve_idee_fecha_atencion()
                                                                                : "");
                                incapacidadDto
                                                .setDelegacion_expedidora(!isBlankOrNull(
                                                                datosIncapacidad.get().getDelegacion_expedidora())
                                                                                ? datosIncapacidad.get()
                                                                                                .getDelegacion_expedidora()
                                                                                : "");
                                incapacidadDto
                                                .setClave_unidad_expedidora(!isBlankOrNull(
                                                                datosIncapacidad.get().getClave_unidad_expedidora())
                                                                                ? datosIncapacidad.get()
                                                                                                .getClave_unidad_expedidora()
                                                                                : "");
                                incapacidadDto.setUnidad_expedidora(
                                                !isBlankOrNull(datosIncapacidad.get().getUnidad_expedidora())
                                                                ? datosIncapacidad.get().getUnidad_expedidora()
                                                                : "");
                                incapacidadDto.setCve_idee(
                                                !isBlankOrNull(datosIncapacidad.get().getCve_idee())
                                                                ? datosIncapacidad.get().getCve_idee()
                                                                : "");
                                incapacidadDto.setNum_nss(
                                                !isBlankOrNull(datosIncapacidad.get().getNum_nss())
                                                                ? datosIncapacidad.get().getNum_nss()
                                                                : "");
                                incapacidadDto.setAgregado_medico(
                                                !isBlankOrNull(datosIncapacidad.get().getAgregado_medico())
                                                                ? datosIncapacidad.get().getAgregado_medico()
                                                                : "");
                                incapacidadDto.setFecha_expedicion(datosIncapacidad.get().getFecha_expedicion());
                                incapacidadDto.setFolio(
                                                !isBlankOrNull(datosIncapacidad.get().getFolio())
                                                                ? datosIncapacidad.get().getFolio()
                                                                : "");
                                incapacidadDto.setFecha_inicio(datosIncapacidad.get().getFecha_inicio());
                                incapacidadDto.setFecha_termino(datosIncapacidad.get().getFecha_termino());
                                /*
                                 * incapacidadDto.setDias_acumulados(
                                 * !isBlankOrNull(datosIncapacidad.get().getDias_acumulados())
                                 * ? datosIncapacidad.get().getDias_acumulados()
                                 * : "");
                                 */
                                incapacidadDto.setDias_autorizados(
                                                !isBlankOrNull(datosIncapacidad.get().getDias_autorizados())
                                                                ? datosIncapacidad.get().getDias_autorizados()
                                                                : "");
                                incapacidadDto.setDias_probables_recuperacion(
                                                !isBlankOrNull(datosIncapacidad.get().getDias_probables_recuperacion())
                                                                ? datosIncapacidad.get()
                                                                                .getDias_probables_recuperacion()
                                                                : "");
                                incapacidadDto.setRamo_seguro(!isBlankOrNull(datosIncapacidad.get().getRamo_seguro())
                                                ? datosIncapacidad.get().getRamo_seguro()
                                                : "");
                                incapacidadDto.setTipo_incapacidad(
                                                !isBlankOrNull(datosIncapacidad.get().getTipo_incapacidad())
                                                                ? datosIncapacidad.get().getTipo_incapacidad()
                                                                : "");
                                incapacidadDto.setOcupacion(
                                                !isBlankOrNull(datosIncapacidad.get().getOcupacion())
                                                                ? datosIncapacidad.get().getOcupacion()
                                                                : "");
                                incapacidadDto.setNombre_patron(
                                                !isBlankOrNull(datosIncapacidad.get().getNombre_patron())
                                                                ? datosIncapacidad.get().getNombre_patron()
                                                                : "");
                                incapacidadDto.setDiagnostico(!isBlankOrNull(datosIncapacidad.get().getDiagnostico())
                                                ? datosIncapacidad.get().getDiagnostico()
                                                : "");
                                incapacidadDto.setClave_presupuestal_origen(
                                                !isBlankOrNull(datosIncapacidad.get().getClave_presupuestal_origen())
                                                                ? datosIncapacidad.get().getClave_presupuestal_origen()
                                                                : "");
                                incapacidadDto.setOrigen_dato(!isBlankOrNull(datosIncapacidad.get().getOrigen_dato())
                                                ? datosIncapacidad.get().getOrigen_dato()
                                                : "");
                                incapacidadDto.setFecha_proceso(datosIncapacidad.get().getFecha_proceso());
                                incapacidadDto.setYearmonth(
                                                !isBlankOrNull(datosIncapacidad.get().getYearmonth())
                                                                ? datosIncapacidad.get().getYearmonth()
                                                                : "");

                                Optional<ResumenClinicoString> resumen = resumenClinicoStringRepository
                                                .findTop1ByNssAndAgregadoMedico(cc_nss, cc_agregado_medico);

                                if (!datosIncapacidad.isEmpty()) {
                                        incapacidadDto.setConsultorio_atencion(
                                                        !isBlankOrNull(resumen.get().getConsultorio_atencion())
                                                                        ? resumen.get().getConsultorio_atencion()
                                                                        : "");
                                        incapacidadDto.setDes_turno(
                                                        !isBlankOrNull(resumen.get().getDes_turno())
                                                                        ? resumen.get().getDes_turno()
                                                                        : "");
                                }

                                return incapacidadDto;

                        }
                }

                return null;
        }

        /*
         * public Optional<ResumenClinicoString>
         * findTop1ByCC_NSSAndCC_AGREGADO_MED(String cc_nss, String cc_agregado_medico
         * ){
         * 
         * return resumenClinicoStringRepository.findTop1ByNssAndAgregadoMedico(cc_nss,
         * cc_agregado_medico);
         * }
         * 
         * 
         */

        /*
         * public Optional<List<IncapacidadNSSA>> findIncapacidadByFolio(String folio){
         * 
         * return incapacidadNSSARepository.findIncapacidadNSSAByFolio(folio);
         * }
         */

        /*
         * public List<IncapacidadNSSA> findNSS_AgregadoMedico(String num_nss, String
         * agregado_medico) {
         * 
         * Criteria agregado = new Criteria("agregado_medico").is(agregado_medico);
         * Criteria agregadoVacio = new Criteria("agregado_medico").is("");
         * 
         * MatchOperation matchStage = Aggregation
         * .match(new Criteria("num_nss").is(num_nss).orOperator(agregado,
         * agregadoVacio));
         * 
         * UnionWithOperation unionWith = UnionWithOperation.unionWith("incapacidades");
         * 
         * Aggregation aggregation = Aggregation.newAggregation(unionWith, matchStage);
         * 
         * List<IncapacidadNSSA> results = mt.aggregate(aggregation,
         * "incapacidades_nssa", IncapacidadNSSA.class)
         * .getMappedResults();
         * 
         * return results;
         * 
         * }
         */
        public Page<IncapacidadNSSA> findNSS_AgregadoMedico(IncapacidadesRequest request) throws ParseException {

                // String num_nss, String agregado_medico, Integer pageNumber, Integer pageSize,
                // Boolean desc, String order
                long total = getCountIncapacidades(request.getModel().getNum_nss(),
                                request.getModel().getAgregado_medico());

                Pageable paging = PageRequest.of(request.getPage() - 1, request.getPageSize());

                // final List<IncapacidadNSSA> results = getIncapacidades(request, paging);
                final List<IncapacidadNSSA> results = getIncapacidadesGroup(request, paging);
                return new PageImpl<>(results, paging, total);
        }

        private Integer getCountIncapacidades(String num_nss, String agregado_medico) {
                // CONDICIONES DE BUSQUEDA
                Criteria agregado = new Criteria("agregado_medico").is(agregado_medico);
                Criteria agregadoVacio = new Criteria("agregado_medico").is("");
                MatchOperation matchStage = Aggregation
                                .match(new Criteria("num_nss").is(num_nss).orOperator(agregado, agregadoVacio));

                // SE COMBINAN LAS COLECCIONES
                UnionWithOperation unionWith = UnionWithOperation.unionWith("incapacidades");

                Aggregation aggregation = Aggregation.newAggregation(unionWith, matchStage);

                return mt.aggregate(aggregation, "incapacidades_nssa", IncapacidadNSSA.class).getMappedResults().size();
        }

        private List<IncapacidadNSSA> getIncapacidades(IncapacidadesRequest request, Pageable pageable)
                        throws ParseException {

                // PAGINACION
                var skip = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
                var limit = new LimitOperation(pageable.getPageSize());

                var sort = Aggregation.sort(request.getDesc() ? Sort.Direction.DESC : Sort.Direction.ASC,
                                request.getOrder());

                // CONDICIONES DE BUSQUEDA
                Criteria cNSS = Criteria.where("num_nss").is(request.getModel().getNum_nss());
                Criteria agregado = new Criteria("agregado_medico").is(request.getModel().getAgregado_medico());
                Criteria agregadoVacio = new Criteria("agregado_medico").is("");

                Criteria cAgregadoMedico = new Criteria().orOperator(agregado, agregadoVacio);

                MatchOperation matchStage = null;
                Collection<Criteria> cCriteriosFiltros = new ArrayList<>();

                if (request.getModel().getStart() != null && request.getModel().getEnd() != null) {

                        CriteriosSoloFechas(request, cCriteriosFiltros);
                }
                if (request.getModel().getDelegacion_expedidora() != null) {
                        CriteriosSoloDelegacionExpedidora(request, cCriteriosFiltros);
                }

                if (request.getModel().getUnidad_expedidora() != null) {
                        CriteriosSoloUnidadExpedidora(request, cCriteriosFiltros);
                }

                if (request.getModel().getDiagnostico() != null) {
                        CriteriosSoloDiagnostico(request, cCriteriosFiltros);
                }

                cCriteriosFiltros.add(cNSS);
                cCriteriosFiltros.add(cAgregadoMedico);

                Criteria cCriterios = new Criteria().andOperator(cCriteriosFiltros);
                matchStage = Aggregation.match(cCriterios);

                /*
                 * // OPCION 2 CONDICIONES PARA FILTROS
                 * 
                 * if (request.getModel().getNum_nss() != null &&
                 * request.getModel().getAgregado_medico() != null) {
                 * 
                 * // SE COMPLEMENTAN CRITERIOS DE BUSQUEDA BASE(NSS Y AGREGADO MEDICO)
                 * cCriteriosFiltros.add(cNSS);
                 * cCriteriosFiltros.add(cAgregadoMedico);
                 * 
                 * // SE UNEN TODOS LOS CRITERIOS(BASE Y BUSQUEDA POR FECHA)
                 * Criteria cCriterios = new Criteria().andOperator(cCriteriosFiltros);
                 * matchStage = Aggregation.match(cCriterios);
                 * }
                 * 
                 * // SOLO FECHA
                 * else if (request.getModel().getStart() != null && request.getModel().getEnd()
                 * != null
                 * && request.getModel().getDelegacion_expedidora() == null
                 * && request.getModel().getUnidad_expedidora() == null
                 * && request.getModel().getDiagnostico() == null) {
                 * 
                 * // SE OBTIENEN LOS CRITERIOS CORRESPONDIENTES A LA BUSQUEDA POR FECHAS
                 * cCriteriosFiltros = CriteriosSoloFechas(request);
                 * 
                 * // SE COMPLEMENTAN CRITERIOS DE BUSQUEDA BASE(NSS Y AGREGADO MEDICO)
                 * cCriteriosFiltros.add(cNSS);
                 * cCriteriosFiltros.add(cAgregadoMedico);
                 * 
                 * // SE UNEN TODOS LOS CRITERIOS(BASE Y BUSQUEDA POR FECHA)
                 * Criteria cCriterios = new Criteria().andOperator(cCriteriosFiltros);
                 * matchStage = Aggregation.match(cCriterios);
                 * }
                 * // SOLO OOAD(Delegacion expedidora)
                 * else if (request.getModel().getDelegacion_expedidora() != null
                 * && request.getModel().getUnidad_expedidora() == null
                 * && request.getModel().getDiagnostico() == null) {
                 * 
                 * // SE OBTIENEN LOS CRITERIOS CORRESPONDIENTES A LA BUSQUEDA POR FECHAS
                 * cCriteriosFiltros = CriteriosSoloDelegacionExpedidora(request);
                 * 
                 * // SE COMPLEMENTAN CRITERIOS DE BUSQUEDA BASE(NSS Y AGREGADO MEDICO)
                 * cCriteriosFiltros.add(cNSS);
                 * cCriteriosFiltros.add(cAgregadoMedico);
                 * 
                 * // SE UNEN TODOS LOS CRITERIOS(BASE Y BUSQUEDA POR FECHA)
                 * Criteria cCriterios = new Criteria().andOperator(cCriteriosFiltros);
                 * matchStage = Aggregation.match(cCriterios);
                 * 
                 * }
                 * 
                 * // SOLO OOAD Y UNIDAD
                 * else if (request.getModel().getDelegacion_expedidora() != null
                 * && request.getModel().getUnidad_expedidora() != null
                 * && request.getModel().getDiagnostico().equals(null)) {
                 * matchStage = Aggregation.match(new
                 * Criteria("num_nss").is(request.getModel().getNum_nss())
                 * .orOperator(agregado, agregadoVacio)
                 * .and("delegacion_expedidora").is(request.getModel().getDelegacion_expedidora(
                 * ))
                 * .and("unidad_expedidora").is(request.getModel().getUnidad_expedidora()));
                 * }
                 * // SOLO OOAD, UNIDAD, DIAGNOSTICO,
                 * else if (request.getModel().getDelegacion_expedidora() != null
                 * && request.getModel().getUnidad_expedidora() != null
                 * && request.getModel().getDiagnostico() != null) {
                 * matchStage = Aggregation.match(new
                 * Criteria("num_nss").is(request.getModel().getNum_nss())
                 * .orOperator(agregado, agregadoVacio)
                 * .and("delegacion_expedidora").is(request.getModel().getDelegacion_expedidora(
                 * ))
                 * .and("unidad_expedidora").is(request.getModel().getUnidad_expedidora())
                 * .and("unidad_expedidora").is(request.getModel().getDiagnostico()));
                 * }
                 * 
                 * else if (request.getModel().getDelegacion_expedidora() != null
                 * && request.getModel().getUnidad_expedidora() != null
                 * && request.getModel().getDiagnostico() != null) {
                 * matchStage = Aggregation.match(new
                 * Criteria("num_nss").is(request.getModel().getNum_nss())
                 * .orOperator(agregado, agregadoVacio)
                 * .and("delegacion_expedidora").is(request.getModel().getDelegacion_expedidora(
                 * ))
                 * .and("unidad_expedidora").is(request.getModel().getUnidad_expedidora())
                 * .and("unidad_expedidora").is(request.getModel().getDiagnostico()));
                 * }
                 */

                // SE COMBINAN LAS COLECCIONES
                UnionWithOperation unionWith = UnionWithOperation.unionWith("incapacidades");
                //SE APLICA EL PAGINADO SOLICITADO
                Aggregation aggregation = Aggregation.newAggregation(unionWith, matchStage, skip, limit, sort);

                return mt.aggregate(aggregation, "incapacidades_nssa", IncapacidadNSSA.class).getMappedResults();
        }

        private List<IncapacidadNSSA> getIncapacidadesGroup(IncapacidadesRequest request, Pageable pageable)
                        throws ParseException {

                // PAGINACION
                var skip = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
                var limit = new LimitOperation(pageable.getPageSize());

                var sort = Aggregation.sort(request.getDesc() ? Sort.Direction.DESC : Sort.Direction.ASC,
                                request.getOrder());

                // CONDICIONES DE BUSQUEDA
                Criteria cNSS = Criteria.where("num_nss").is(request.getModel().getNum_nss());
                Criteria agregado = new Criteria("agregado_medico").is(request.getModel().getAgregado_medico());
                Criteria agregadoVacio = new Criteria("agregado_medico").is("");

                Criteria cAgregadoMedico = new Criteria().orOperator(agregado, agregadoVacio);

                MatchOperation matchStage = null;
                Collection<Criteria> cCriteriosFiltros = new ArrayList<>();

                if (request.getModel().getStart() != null && request.getModel().getEnd() != null) {

                        CriteriosSoloFechas(request, cCriteriosFiltros);
                }
                if (request.getModel().getDelegacion_expedidora() != null) {
                        CriteriosSoloDelegacionExpedidora(request, cCriteriosFiltros);
                }

                if (request.getModel().getUnidad_expedidora() != null) {
                        CriteriosSoloUnidadExpedidora(request, cCriteriosFiltros);
                }

                if (request.getModel().getDiagnostico() != null) {
                        CriteriosSoloDiagnostico(request, cCriteriosFiltros);
                }

                cCriteriosFiltros.add(cNSS);
                cCriteriosFiltros.add(cAgregadoMedico);

                Criteria cCriterios = new Criteria().andOperator(cCriteriosFiltros);
                matchStage = Aggregation.match(cCriterios);

                /* EMPIEZA EL GROUP */

                // SE COMBINAN LAS COLECCIONES
                UnionWithOperation unionWith = UnionWithOperation.unionWith("incapacidades");

                //SE APLICA EL PAGINADO SOLICITADO
                Aggregation aggregation2 = Aggregation.newAggregation(unionWith, matchStage, skip, limit, sort);

                List<IncapacidadNSSA> respuesta = mt
                                .aggregate(aggregation2, "incapacidades_nssa", IncapacidadNSSA.class)
                                .getMappedResults();

                respuesta.forEach((x) -> {

                        // REGLA DE NEGOCIO IDENTIFICADA EN LOS SERVICIOS ANTERIORES, APLICA SOLO PARA
                        // LA COLLECTION INCAPACIDADES (ORIGEN => simf o ece)
                        if (x.getOrigen_dato().equals("simf")) {
                                //System.out.println(x);
                                var datosAgrupados = incapacidadRepository.findIncapacidadesSimfAgrupadas(
                                                x.getCve_idee(), x.getRamo_seguro(), x.getDiagnostico(),
                                                x.getFecha_expedicion());

                                if (!datosAgrupados.isEmpty()) {

                                        x.setDias_acumulados(datosAgrupados.get(0).getDiasAcumulados().toString());
                                } else {
                                        x.setDias_acumulados("0");
                                }

                                //System.out.println(x);
                        } else if (x.getOrigen_dato().equals("ece")) {
                                //System.out.println(x);
                                var datosAgrupados = incapacidadRepository.findIncapacidadesEceAgrupadas(
                                                x.getCve_idee(), x.getRamo_seguro(), x.getDiagnostico(),
                                                x.getFecha_expedicion());

                                if (!datosAgrupados.isEmpty()) {

                                        x.setDias_acumulados(datosAgrupados.get(0).getDiasAcumulados().toString());
                                } else {
                                        x.setDias_acumulados("0");
                                }

                                //System.out.println(x);
                        }

                        //SE DA FORMATO A LAS FECHAS
                        
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        String format_expedicion = formatter.format(x.getFecha_expedicion());
                        String format_inicio = formatter.format(x.getFecha_inicio());
                        String format_termino = formatter.format(x.getFecha_termino());
                        

                        x.setFecha_expedicion_string(format_expedicion);
                        x.setFecha_inicio_string(format_inicio);
                        x.setFecha_termino_string(format_termino);


                });

                return respuesta;
        }

        public Object findNSS_AgregadoMedicoByRepository(String num_nss, String agregado_medico, Integer pageNumber,
                        Integer pageSize) {

                Pageable paging = PageRequest.of(pageNumber - 1, pageSize);

                var incapacidades = incapacidadNSSARepository.findIncapacidades_NSS_AgregadoMedico(num_nss,
                                agregado_medico,
                                paging);

                // return
                // incapacidadNSSARepository.findIncapacidades_NSS_AgregadoMedico(num_nss,
                // agregado_medico, paging);
                return incapacidades;
        }

        public static boolean isBlankOrNull(String str) {
                return (str == null || "".equals(str.trim()));
        }

        /// METODOS PARA BUSQUEDA

        public static void CriteriosSoloFechas(IncapacidadesRequest request, Collection<Criteria> cCriterios)
                        throws ParseException {

                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date fromDate = dateFormat.parse(request.getModel().getStart());
                Date toDate = dateFormat.parse(request.getModel().getEnd());

                Calendar c1 = Calendar.getInstance();
                c1.setTime(toDate);
                c1.add(Calendar.DATE, 1);
                toDate = c1.getTime();

                Criteria cInicio = Criteria.where("fecha_expedicion").gte(fromDate);
                Criteria cFin = Criteria.where("fecha_expedicion").lte(toDate);

                cCriterios.add(cInicio);
                cCriterios.add(cFin);

                // return cCriterios;

        }

        public static void CriteriosSoloDelegacionExpedidora(IncapacidadesRequest request,
                        Collection<Criteria> cCriterios)
                        throws ParseException {
                // Collection<Criteria> cCriterios = new ArrayList<>();
                //
                Criteria delegacion = Criteria.where("delegacion_expedidora")
                                .regex(request.getModel().getDelegacion_expedidora(), "i");
                cCriterios.add(delegacion);
                // return cCriterios;

        }

        public static void CriteriosSoloUnidadExpedidora(IncapacidadesRequest request, Collection<Criteria> cCriterios)
                        throws ParseException {
                // Collection<Criteria> cCriterios = new ArrayList<>();
                //
                Criteria unidad = Criteria.where("unidad_expedidora")
                                .regex(request.getModel().getUnidad_expedidora(), "i");
                cCriterios.add(unidad);
                // return cCriterios;

        }

        public static void CriteriosSoloDiagnostico(IncapacidadesRequest request, Collection<Criteria> cCriterios)
                        throws ParseException {
                // Collection<Criteria> cCriterios = new ArrayList<>();
                //
                Criteria diagnostico = Criteria.where("diagnostico")
                                .regex(request.getModel().getDiagnostico(), "i");
                cCriterios.add(diagnostico);
                // return cCriterios;

        }

        /// METODOS PARA BUSQUEDA

        public static Collection<Criteria> CriteriosSoloFechas(IncapacidadesRequest request) throws ParseException {
                Collection<Criteria> cCriterios = new ArrayList<>();

                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date fromDate = dateFormat.parse(request.getModel().getStart());
                Date toDate = dateFormat.parse(request.getModel().getEnd());

                Calendar c1 = Calendar.getInstance();
                c1.setTime(toDate);
                c1.add(Calendar.DATE, 1);
                toDate = c1.getTime();

                Criteria cInicio = Criteria.where("fecha_expedicion").gte(fromDate);
                Criteria cFin = Criteria.where("fecha_expedicion").lte(toDate);

                cCriterios.add(cInicio);
                cCriterios.add(cFin);

                return cCriterios;

        }

        public static Collection<Criteria> CriteriosSoloDelegacionExpedidora(IncapacidadesRequest request)
                        throws ParseException {
                Collection<Criteria> cCriterios = new ArrayList<>();
                //
                Criteria delegacion = Criteria.where("delegacion_expedidora")
                                .regex(request.getModel().getDelegacion_expedidora(), "i");
                cCriterios.add(delegacion);
                return cCriterios;

        }

        public static Collection<Criteria> CriteriosSoloUnidadExpedidora(IncapacidadesRequest request)
                        throws ParseException {
                Collection<Criteria> cCriterios = new ArrayList<>();
                //
                Criteria unidad = Criteria.where("unidad_expedidora")
                                .regex(request.getModel().getUnidad_expedidora(), "i");
                cCriterios.add(unidad);
                return cCriterios;

        }

        public static Collection<Criteria> CriteriosSoloDiagnostico(IncapacidadesRequest request)
                        throws ParseException {
                Collection<Criteria> cCriterios = new ArrayList<>();
                //
                Criteria diagnostico = Criteria.where("diagnostico")
                                .regex(request.getModel().getDiagnostico(), "i");
                cCriterios.add(diagnostico);
                return cCriterios;

        }
}
