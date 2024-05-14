// git commit -m "PBL-850 set up base"

package org.dhv.pbl5server.common_service.repository;

import org.dhv.pbl5server.common_service.model.DbJsonArrayModel;
import org.dhv.pbl5server.common_service.utils.CommonUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CrudDbJsonArrayRepositoryImpl<T extends DbJsonArrayModel<K>, K>
        implements CrudDbJsonArrayRepository<T, K> {
    @Override
    public Optional<T> findById(List<T> data, K id) {
        var mapData = convertArrayToMap(data);
        if (!mapData.containsKey(id))
            return Optional.empty();
        return Optional.of(mapData.get(id));
    }

    @Override
    public boolean existsById(List<T> data, K id) {
        var mapData = convertArrayToMap(data);
        return mapData.containsKey(id);
    }

    @Override
    public List<T> save(List<T> data, T object) {
        var mapData = convertArrayToMap(data);
        // if object has id, update it
        if (object.getId() != null) {
            if (mapData.containsKey(object.getId())) {
                var rawObj = mapData.get(object.getId());
                object.setUpdatedAt(CommonUtils.getCurrentTimestamp());
                object.setCreatedAt(rawObj.getCreatedAt());
                mapData.replace(object.getId(), object);
                return mapData.values().stream().toList();
            }
        }
        // if object has no id, create new one
        object.setId(object.generateId());
        object.setCreatedAt(CommonUtils.getCurrentTimestamp());
        object.setUpdatedAt(CommonUtils.getCurrentTimestamp());
        mapData.put(object.getId(), object);
        return mapData.values().stream().toList();
    }

    @Override
    public List<T> saveAll(List<T> data, List<T> objects) {
        List<T> result = data;
        for (var obj : objects)
            result = save(result, obj);
        return result;
    }

    @Override
    public List<T> deleteById(List<T> data, K id) {
        var mapData = convertArrayToMap(data);
        if (!mapData.containsKey(id))
            return data;
        mapData.remove(id);
        return mapData.values().stream().toList();
    }

    @Override
    public List<T> deleteAllById(List<T> data, List<K> ids) {
        List<T> result = data;
        for (var id : ids)
            result = deleteById(result, id);
        return result;
    }

    @Override
    public Map<K, T> convertArrayToMap(List<T> data) {
        if (data == null)
            return new HashMap<>();
        return data
                .stream()
                .collect(Collectors.toMap(DbJsonArrayModel::getId, e -> e));
    }
}
