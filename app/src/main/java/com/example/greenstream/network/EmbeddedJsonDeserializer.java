package com.example.greenstream.network;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

/**
 * Custom wrapping deserializer for embedded JSON, i.e. a string containing a proper JSON object.
 * To use this method on an attribute of an object, the declaration of this attribute needs to be
 * annotated with the {@link JsonDeserialize} annotation, where {@link JsonDeserialize#using()} must
 * be set to {@code EmbeddedJsonDeserializer.class} and {@link JsonDeserialize#as()} to the class
 * to which the contained json object is to be parsed (which corresponds to the type parameter of
 * this class).
 * This is necessary for instantiating the correct {@link ContextualDeserializer}
 * @param <T>
 */
public class EmbeddedJsonDeserializer<T> extends StdDeserializer<T> implements ContextualDeserializer {

    private Class<T> clazz = null;

    public EmbeddedJsonDeserializer() {
        super((JavaType) null);
    }

    private EmbeddedJsonDeserializer(Class<T> clazz) {
        this();
        this.clazz = clazz;
    }

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return p.getCodec().getFactory().createParser(p.getText()).readValueAs(clazz);
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
        return new EmbeddedJsonDeserializer<>(property.getAnnotation(JsonDeserialize.class).as());
    }
}
