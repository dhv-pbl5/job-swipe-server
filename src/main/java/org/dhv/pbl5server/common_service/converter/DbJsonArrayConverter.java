package org.dhv.pbl5server.common_service.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.dhv.pbl5server.common_service.utils.CommonUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Converter which converts a list of object to a JSON Array string and vice versa
 * Column in db will store an Array of JSON string (String[])
 *
 * @param <T> the type of object in the list
 */
@Converter
public abstract class DbJsonArrayConverter<T> implements AttributeConverter<List<T>, Object> {

    protected abstract Class<T> getClazz();

    /**
     * @param attribute the list object data
     * @return a string format of JSON Array
     * {"{\"key\": \"_key\", \"value\": \"_value\"}","{\"key\": \"_key\", \"value\": \"_value\"}"}
     */
    @Override
    public Object convertToDatabaseColumn(List<T> attribute) {
        return attribute;
//        if (CommonUtils.isEmptyOrNullList(attribute)) return null;
//        var jsonEncode = attribute.stream()
//            .map(item -> {
//                var json = CommonUtils.convertToJson(item);
//                if (json == null) return null;
//                StringBuilder ans = new StringBuilder();
//                for (int i = 1; i < json.length() - 1; i++) {
//                    var character = json.charAt(i);
//                    if (character == '\"') {
//                        ans.append("\\");
//                        ans.append(character);
//                        continue;
//                    }
//                    ans.append(character);
//                }
//                return STR."\"{\{ans}}\"";
//            })
//            .toList();
//        return STR."{\{String.join(",", jsonEncode)}}";
    }

    /**
     * Convert a JSON string to a list of OtherDescription
     *
     * @param dbData a JSON Array string from db with format
     *               {"{\"key\": \"_key\", \"value\": \"_value\"}","{\"key\": \"_key\", \"value\": \"_value\"}"}
     * @return a list of decoded object
     */
    @Override
    public List<T> convertToEntityAttribute(Object dbData) {
        var str1 = dbData.toString();
        var clazz = getClazz();
        if (clazz == null || CommonUtils.isEmptyOrNullString(str1)) return null;
        StringBuilder str = new StringBuilder();
        for (int i = 2; i < str1.length() - 2; i++) {
            if (str1.charAt(i) != '\\')
                str.append(str1.charAt(i));
        }
        if (CommonUtils.isEmptyOrNullString(str.toString())) return null;
        var x = Arrays.stream(str.toString().split("\",\""))
            .map(json -> CommonUtils.decodeJson(json, clazz)).toList();
        return x;
    }
}
