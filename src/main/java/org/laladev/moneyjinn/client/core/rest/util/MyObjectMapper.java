package org.laladev.moneyjinn.client.core.rest.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

public class MyObjectMapper extends ObjectMapper {

	private static final long serialVersionUID = 1L;

	public MyObjectMapper() {
		super();
		super.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
		super.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
		super.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		super.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		super.setAnnotationIntrospector(new JaxbAnnotationIntrospector(TypeFactory.defaultInstance()));
	}
}
