//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.automation.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.LoaderOptions;

@Component
public class YamlFileReader implements FileReader {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(YamlFileReader.class);
    private final ObjectMapper mapper;

    public YamlFileReader() {
        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setCodePointLimit(10485760);
        YAMLFactory yamlFactory = YAMLFactory.builder().loaderOptions(loaderOptions).build();
        this.mapper = new ObjectMapper(yamlFactory);
        this.mapper.coercionConfigDefaults().setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsEmpty);
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        this.mapper.registerModule(new JodaModule());
        this.mapper.registerModule(new JavaTimeModule());
    }

    public <T> T readList(File data, Class<?> responseClass) {
        TypeFactory typeFactory = this.mapper.getTypeFactory();
        CollectionType collectionType = typeFactory.constructCollectionType(List.class, responseClass);
        T result = null;

        try {
            result = (T)this.mapper.readValue(data, collectionType);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Unable to parse the file:: {} and exception is::  {}", data.getName(), e);
        }

        return result;
    }

    public <T> T readMap(File file, Class<?> mapValueType) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        TypeFactory typeFactory = mapper.getTypeFactory();
        JavaType stringType = mapper.getTypeFactory().constructType(String.class);
        JavaType valueType = mapper.getTypeFactory().constructType(mapValueType);
        MapType type = typeFactory.constructMapType(Map.class, stringType, valueType);
        T result = null;

        try {
            result = (T)mapper.readValue(file, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public <T> T readList(String data, Class<?> responseClass) {
        TypeFactory typeFactory = this.mapper.getTypeFactory();
        CollectionType collectionType = typeFactory.constructCollectionType(List.class, responseClass);
        T result = null;

        try {
            result = (T)this.mapper.readValue(data, collectionType);
        } catch (Exception e) {
            log.error("Unable to parse the file and exception is:: {}", e);
        }

        return result;
    }

    public <T> T read(File file, Class<?> mapValueType) {
        T result = null;

        try {
            result = (T)this.mapper.readValue(file, mapValueType);
        } catch (Exception e) {
            log.error("Failed to Read data from the file. Error:: {} ", e);
        }

        return result;
    }

    public <T> T read(String data, Class<?> mapValueType) {
        T result = null;

        try {
            result = (T)this.mapper.readValue(data, mapValueType);
        } catch (Exception e) {
            log.error("Failed to Read String data. Error:: {}", e);
        }

        return result;
    }

    public void write(Object object, File file) {
        try {
            this.mapper.writeValue(file, object);
        } catch (Exception e) {
            log.error("Failed to write value to the file. Error:: {}", e);
        }

    }

    public String write(Object object) {
        try {
            return this.mapper.writeValueAsString(object);
        } catch (Exception e) {
            log.error("Failed to write value. Error:: " + e);
            return null;
        }
    }

    public <T> T convertTo(Object obj, Class type) {
        try {
            return (T)this.mapper.convertValue(obj, type);
        } catch (Exception e) {
            log.error("Failed to convert the object to given type. Error:: {} ", e);
            return null;
        }
    }

    public <T> T readFile(File fileName, Class<?> responseClass) {
        T result = null;

        try {
            result = (T)this.mapper.readValue(fileName, responseClass);
        } catch (Exception var5) {
            log.error("Unable to parse the file:: {}", fileName.getName());
        }

        return result;
    }

    public <T> List<T> readYamlFromClasspath(String classpathFile, Class<T> responseClass) {
        List<T> result = null;

        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(classpathFile);
            if (inputStream == null) {
                throw new FileNotFoundException("YAML file not found in classpath: " + classpathFile);
            }

            TypeFactory typeFactory = this.mapper.getTypeFactory();
            CollectionType listType = typeFactory.constructCollectionType(List.class, responseClass);
            result = (List)this.mapper.readValue(inputStream, listType);
        } catch (Exception e) {
            log.error("Failed to read YAML from classpath: ", e);
        }

        return result;
    }
}