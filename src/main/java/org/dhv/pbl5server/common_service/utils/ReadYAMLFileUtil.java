package org.dhv.pbl5server.common_service.utils;

import lombok.NoArgsConstructor;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

@NoArgsConstructor
public final class ReadYAMLFileUtil {

    public Map<String, Object> getValueFromYAMLFile(String nameFile) {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(nameFile);
        return yaml.load(inputStream);
    }
    
}
