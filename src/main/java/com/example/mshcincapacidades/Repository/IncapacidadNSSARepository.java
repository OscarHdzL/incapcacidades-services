package com.example.mshcincapacidades.Repository;

import java.util.List;
import java.util.Optional;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.aggregation.UnionWithOperation;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators.Slice;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import com.example.mshcincapacidades.Model.IncapacidadNSSA;

@Repository
public interface IncapacidadNSSARepository extends MongoRepository<IncapacidadNSSA, String>{
    
    Optional<IncapacidadNSSA> findIncapacidadNSSAByFolio(String folio);
    /* IncapacidadNSSA findIncapacidadNSSAByFolio(String folio); */

   /*  Optional<List<IncapacidadNSSA>> findIncapacidadNSSAByFolio(String folio); */


@Aggregation(
    pipeline = {
        "{'$unionWith': { coll: 'incapacidades'}}",
        "{'$match':{'num_nss':?0, '$or': [{'agregado_medico': ?1},{'agregado_medico': ''}] }}",
    }
)


org.springframework.data.domain.Slice<IncapacidadNSSA> findIncapacidades_NSS_AgregadoMedico(String num_nss, String agregado_medico, Pageable paging);

}
