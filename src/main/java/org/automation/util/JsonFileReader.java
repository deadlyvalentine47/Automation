//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.automation.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.util.List;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JsonFileReader implements FileReader {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(JsonFileReader.class);
    private final ObjectMapper mapper = new ObjectMapper();

    public JsonFileReader() {
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        this.mapper.registerModule(new JavaTimeModule());
    }

    public JsonFileReader(Boolean ignoreFailOnUnknownProperty) {
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        if (ignoreFailOnUnknownProperty) {
            this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }

    }

    public <T> T read(File file, Class<?> responseClass) {
        T result = null;

        try {
            result = (T)this.mapper.readValue(file, responseClass);
        } catch (Exception e) {
            log.info("Unable to parse the json file:: {} Error:: {} ", file.getName(), e);
        }

        return result;
    }

    public <T> T readList(File file, Class<?> responseClass) {
        TypeFactory typeFactory = this.mapper.getTypeFactory();
        CollectionType collectionType = typeFactory.constructCollectionType(List.class, responseClass);
        T result = null;

        try {
            result = (T)this.mapper.readValue(file, collectionType);
        } catch (Exception e) {
            log.info("Unable to parse the json file:: {} Error:: {} ", file.getName(), e);
        }

        return result;
    }

    public <T> T readList(String jsonString, Class<?> responseClass) {
        TypeFactory typeFactory = this.mapper.getTypeFactory();
        CollectionType collectionType = typeFactory.constructCollectionType(List.class, responseClass);
        T result = null;

        try {
            result = (T)this.mapper.readValue(jsonString, collectionType);
        } catch (Exception e) {
            log.info("Unable to parse the json string. Error:: {}", e);
        }

        return result;
    }

    public <T> T read(String jsonString, Class<?> responseClass) {
        T result = null;

        try {
            result = (T)this.mapper.readValue(jsonString, responseClass);
        } catch (Exception e) {
            log.info("Unable to parse the json string. Error:: {}", e);
        }

        return result;
    }

    public String covertToJsonString(Object obj) {
        try {
            return this.mapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.info("Unable to parse the json string. Error:: {}", e);
            return null;
        }
    }

    public <T> T convertTo(Object obj, Class type) {
        try {
            return (T)this.mapper.convertValue(obj, type);
        } catch (Exception e) {
            log.info("Unable to parse the json string. Error:: {}", e);
            return null;
        }
    }

    public void write(Object data, File writeToFilePath) {
        try {
            this.mapper.writeValue(writeToFilePath, data);
        } catch (IOException e) {
            log.info("Unable to parse the json string. Error:: {}", e);
        }

    }

    public String write(Object data) {
        try {
            return this.mapper.writeValueAsString(data);
        } catch (IOException e) {
            log.info("Unable to parse the json string. Error:: {}", e);
            return null;
        }
    }

    public <T> T readJson(String jsonString, Class<?> responseClass) {
        this.mapper.registerModule(new Jdk8Module());
        if (jsonString != null && responseClass != null) {
            T result = null;

            try {
                result = (T)this.mapper.readValue(jsonString, responseClass);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        } else {
            return null;
        }
    }
}