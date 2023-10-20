package com.example.mshcincapacidades.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.mshcincapacidades.Model.Incapacidad;
import com.example.mshcincapacidades.Model.RespuestaDelegacion;
import com.example.mshcincapacidades.Model.RespuestaDiagnostico;
import com.example.mshcincapacidades.Model.RespuestaIncapacidad;
import com.example.mshcincapacidades.Model.RespuestaUnidad;

@Repository
public interface IncapacidadRepository extends MongoRepository<Incapacidad, String>{
    
    Optional<Incapacidad> findIncapacidadByFolio(String folio);
    
@Aggregation(
    pipeline = {
        
        "{ '$match': { '$expr': { '$and': [{ '$eq': ['$cve_idee', ?0  ] },{ '$eq': ['$ramo_seguro',  ?1] },{ '$eq': ['$diagnostico', ?2] },{ '$lt': ['$fecha_expedicion', ?3] }] } } }",
        "{ '$group' : { '_id' : { 'ramoSeguro': '$ramo_seguro', 'diagnostico' : '$diagnostico' }, 'diasAcumulados' : { '$sum': { '$toInt': '$dias_autorizados' } }} }"
    }
)
List<RespuestaIncapacidad> findIncapacidadesSimfAgrupadas(String ide, String ramo_seguro, String diagnostico, Date fecha_expedicion);


@Aggregation(
    pipeline = {
        
        "{ '$match': { '$expr': { '$and': [{ '$eq': ['$cve_idee', ?0  ] },{ '$eq': ['$ramo_seguro',  ?1] },{ '$eq': ['$diagnostico', ?2] },{ '$gte': ['$fecha_expedicion', ?3] },{ '$lte': ['$fecha_expedicion', ?3] }] } } }",
        "{ '$group' : { '_id' : { 'ramoSeguro': '$ramo_seguro', 'diagnostico' : '$diagnostico' }, 'diasAcumulados' : { '$sum': { '$toInt': '$dias_autorizados' } }} }"
    }
)
List<RespuestaIncapacidad> findIncapacidadesEceAgrupadas(String ide, String ramo_seguro, String diagnostico, Date fecha_expedicion);


@Aggregation(
    pipeline = {
        
        "{ '$match': { '$expr': { '$and': [{ '$eq': ['$cve_idee', ?0  ] },{ '$eq': ['$ramo_seguro',  ?1] },{ '$eq': ['$diagnostico', ?2] }] } } }",
        "{ '$group' : { '_id' : { 'ramoSeguro': '$ramo_seguro', 'diagnostico' : '$diagnostico' }, 'diasAcumulados' : { '$sum': { '$toInt': '$dias_autorizados' } }} }"
    }
)
List<RespuestaIncapacidad> findIncapacidadesAgrupadas(String ide, String ramo_seguro, String diagnostico);

/* BUSQUEDA DIAGNOSTICOS */
@Aggregation(
    pipeline = {
        "{'$unionWith': {'coll': 'incapacidades_nssa'}}",
        "{'$match':{'$and':[{'num_nss':?0},{'$or':[{'agregado_medico':?1},{'agregado_medico':''}]}]}}",
        "{ '$group' : { '_id' : { 'diagnostico': '$diagnostico'}, 'diagnostico' : { '$first': '$diagnostico'}} }"
    }
)
List<RespuestaDiagnostico> findDiagnosticosByNssAgregado(String num_nss, String agregado_medico);

@Aggregation(
    pipeline = {
        "{'$unionWith': {'coll': 'incapacidades_nssa'}}",
        "{'$match':{'$expr':{'$and':[{'$eq':['$num_nss',?0]},{'$or':[{'agregado_medico':?1},{'agregado_medico':''}]},{'$gte':['$fecha_expedicion', ?2]},{'$lt':['$fecha_expedicion', ?3]}]}}}",
        "{'$group':{'_id':{'diagnostico':'$diagnostico'},'diagnostico':{'$first':'$diagnostico'}}},{'$sort':{'diagnostico':1}}"
    }
)
List<RespuestaDiagnostico> findDiagnosticosByNssAgregadoFechas(String num_nss, String agregado_medico, Date start, Date end);


@Aggregation(
    pipeline = {
         "{'$unionWith': {'coll': 'incapacidades_nssa'}}",
        "{'$match':{'$expr':{'$and':[{'$eq':['$num_nss',?0]},{'$or':[{'agregado_medico':?1},{'agregado_medico':''}]},{'$gte':['$fecha_expedicion', ?2]},{'$lt':['$fecha_expedicion', ?3]},{'$eq':['$delegacion_expedidora',?4]}]}}}",
        "{'$group':{'_id':{'diagnostico':'$diagnostico'},'diagnostico':{'$first':'$diagnostico'}}},{'$sort':{'diagnostico':1}}"
    }
)
List<RespuestaDiagnostico> findDiagnosticosByNssAgregadoFechasDelegacion(String num_nss, String agregado_medico, Date start, Date end, String delegacion);

@Aggregation(
    pipeline = {
         "{'$unionWith': {'coll': 'incapacidades_nssa'}}",
        "{'$match':{'$expr':{'$and':[{'$eq':['$num_nss',?0]},{'$or':[{'agregado_medico':?1},{'agregado_medico':''}]},{'$gte':['$fecha_expedicion', ?2]},{'$lt':['$fecha_expedicion', ?3]},{'$eq':['$unidad_expedidora',?4]}]}}}",
        "{'$group':{'_id':{'diagnostico':'$diagnostico'},'diagnostico':{'$first':'$diagnostico'}}},{'$sort':{'diagnostico':1}}"
    }
)
List<RespuestaDiagnostico> findDiagnosticosByNssAgregadoFechasUnidad(String num_nss, String agregado_medico, Date start, Date end, String unidad);

@Aggregation(
    pipeline = {
         "{'$unionWith': {'coll': 'incapacidades_nssa'}}",
        "{'$match':{'$expr':{'$and':[{'$eq':['$num_nss',?0]},{'$or':[{'agregado_medico':?1},{'agregado_medico':''}]},{'$gte':['$fecha_expedicion', ?2]},{'$lt':['$fecha_expedicion', ?3]},{'$eq':['$delegacion_expedidora',?4]},{'$eq':['$unidad_expedidora',?5]}]}}}",
        "{'$group':{'_id':{'diagnostico':'$diagnostico'},'diagnostico':{'$first':'$diagnostico'}}},{'$sort':{'diagnostico':1}}"
    }
)
List<RespuestaDiagnostico> findDiagnosticosByNssAgregadoFechasDelegacionUnidad(String num_nss, String agregado_medico, Date start, Date end, String delegacion, String unidad);

@Aggregation(
    pipeline = {
         "{'$unionWith': {'coll': 'incapacidades_nssa'}}",
        "{'$match':{'$expr':{'$and':[{'$eq':['$num_nss',?0]},{'$or':[{'agregado_medico':?1},{'agregado_medico':''}]},{'$eq':['$delegacion_expedidora',?2]}]}}}",
        "{'$group':{'_id':{'diagnostico':'$diagnostico'},'diagnostico':{'$first':'$diagnostico'}}},{'$sort':{'diagnostico':1}}"
    }
)
List<RespuestaDiagnostico> findDiagnosticosByNssAgregadoDelegacion(String num_nss, String agregado_medico, String delegacion);


@Aggregation(
    pipeline = {
         "{'$unionWith': {'coll': 'incapacidades_nssa'}}",
        "{'$match':{'$expr':{'$and':[{'$eq':['$num_nss',?0]},{'$or':[{'agregado_medico':?1},{'agregado_medico':''}]},{'$eq':['$unidad_expedidora',?2]}]}}}",
        "{'$group':{'_id':{'diagnostico':'$diagnostico'},'diagnostico':{'$first':'$diagnostico'}}},{'$sort':{'diagnostico':1}}"
    }
)
List<RespuestaDiagnostico> findDiagnosticosByNssAgregadoUnidad(String num_nss, String agregado_medico, String unidad);



/* UNIDADES */

@Aggregation(
    pipeline = {
        "{'$unionWith': {'coll': 'incapacidades_nssa'}}",
        "{'$match':{'$and':[{'num_nss':?0},{'$or':[{'agregado_medico':?1},{'agregado_medico':''}]}]}}",
        "{'$group':{'_id':{'unidad_expedidora':'$unidad_expedidora'},'unidad_expedidora':{'$first':'$unidad_expedidora'}}},{'$sort':{'unidad_expedidora':1}}"
    }
)
List<RespuestaUnidad> findUnidadesByNssAgregado(String num_nss, String agregado_medico);

@Aggregation(
    pipeline = {
        "{'$unionWith': {'coll': 'incapacidades_nssa'}}",
        "{'$match':{'$expr':{'$and':[{'$eq':['$num_nss',?0]},{'$or':[{'agregado_medico':?1},{'agregado_medico':''}]},{'$gte':['$fecha_expedicion', ?2]},{'$lt':['$fecha_expedicion', ?3]}]}}}",
        "{'$group':{'_id':{'unidad_expedidora':'$unidad_expedidora'},'unidad_expedidora':{'$first':'$unidad_expedidora'}}},{'$sort':{'unidad_expedidora':1}}"
    }
)
List<RespuestaUnidad> findUnidadesByNssAgregadoFechas(String num_nss, String agregado_medico, Date start, Date end);


@Aggregation(
    pipeline = {
         "{'$unionWith': {'coll': 'incapacidades_nssa'}}",
        "{'$match':{'$expr':{'$and':[{'$eq':['$num_nss',?0]},{'$or':[{'agregado_medico':?1},{'agregado_medico':''}]},{'$eq':['$delegacion_expedidora',?2]}]}}}",
        "{'$group':{'_id':{'unidad_expedidora':'$unidad_expedidora'},'unidad_expedidora':{'$first':'$unidad_expedidora'}}},{'$sort':{'unidad_expedidora':1}}"
    }
)
List<RespuestaUnidad> findUnidadesByNssAgregadoDelegacion(String num_nss, String agregado_medico, String delegacion);


@Aggregation(
    pipeline = {
         "{'$unionWith': {'coll': 'incapacidades_nssa'}}",
        "{'$match':{'$expr':{'$and':[{'$eq':['$num_nss',?0]},{'$or':[{'agregado_medico':?1},{'agregado_medico':''}]},{'$gte':['$fecha_expedicion', ?2]},{'$lt':['$fecha_expedicion', ?3]},{'$eq':['$delegacion_expedidora',?4]}]}}}",
        "{'$group':{'_id':{'unidad_expedidora':'$unidad_expedidora'},'unidad_expedidora':{'$first':'$unidad_expedidora'}}},{'$sort':{'unidad_expedidora':1}}"
    }
)
List<RespuestaUnidad> findUnidadesByNssAgregadoFechasDelegacion(String num_nss, String agregado_medico, Date start, Date end, String delegacion);

/* DELEGACIONES */

@Aggregation(
    pipeline = {
        "{'$unionWith': {'coll': 'incapacidades_nssa'}}",
        "{'$match':{'$and':[{'num_nss':?0},{'$or':[{'agregado_medico':?1},{'agregado_medico':''}]}]}}",
        "{'$group':{'_id':{'delegacion_expedidora':'$delegacion_expedidora'},'delegacion_expedidora':{'$first':'$delegacion_expedidora'}}},{'$sort':{'delegacion_expedidora':1}}"
    }
)
List<RespuestaDelegacion> findDelegacionesByNssAgregado(String num_nss, String agregado_medico);


}
