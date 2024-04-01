package org.dhv.pbl5server.common_service.repository;

import org.dhv.pbl5server.common_service.model.DbJsonArrayModel;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Fake spring data repository.
 * Used when we store an Array of objects in database.
 *
 * @param <T> object type
 * @param <K> primary key type
 */
@NoRepositoryBean
public interface CrudDbJsonArrayRepository<T extends DbJsonArrayModel<K>, K> {
    Optional<T> findById(List<T> data, K id);

    boolean existsById(List<T> data, K id);


    List<T> save(List<T> data, T object);


    List<T> saveAll(List<T> data, List<T> objects);


    List<T> deleteById(List<T> data, K id);

    List<T> deleteAllById(List<T> data, List<K> ids);


    Map<K, T> convertArrayToMap(List<T> data);
}
