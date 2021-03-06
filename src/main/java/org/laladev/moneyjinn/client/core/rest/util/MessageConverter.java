//
// Copyright (c) 2014-2015 Oliver Lehmann <lehmann@ans-netz.de>
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
// OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
// OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE.
//
// $Id: MessageConverter.java,v 1.4 2015/09/11 08:00:10 olivleh1 Exp $
//
package org.laladev.moneyjinn.client.core.rest.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Type;

import org.apache.commons.io.IOUtils;
import org.laladev.moneyjinn.core.rest.model.AbstractResponse;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

public class MessageConverter extends MappingJackson2HttpMessageConverter {

	public MessageConverter() {
		final ObjectMapper mapper = new MyObjectMapper();
		super.setObjectMapper(mapper);
	}

	// @Override
	// protected Object readInternal(Class<?> clazz, HttpInputMessage
	// inputMessage)
	// throws JsonParseException, JsonMappingException, IOException {

	@Override
	public Object read(final Type type, final Class<?> contextClass, final HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {

		final byte[] body = IOUtils.toByteArray(inputMessage.getBody());
		inputMessage.getBody().close();

		try {
			final JavaType javaType = this.getJavaType(type, contextClass);
			return super.getObjectMapper().readValue(body, javaType);
		} catch (final IOException ex) {
			try {
				if (type != null && type instanceof Class) {
					final Class<?> clazz = (Class<?>) type;
					final Object response = clazz.newInstance();
					if (response instanceof AbstractResponse) {
						final JavaType javaType = this.getJavaType(ErrorResponse.class, null);
						final ErrorResponse errorResponse = super.getObjectMapper().readValue(body, javaType);
						((AbstractResponse) response).setErrorResponse(errorResponse);
						return response;
					}
				}
			} catch (final Exception e) {
				throw new HttpMessageNotReadableException("Could not read JSON: " + e.getMessage(), e);
			}
			throw new HttpMessageNotReadableException("Could not read JSON: " + ex.getMessage(), ex);
		}
	}
}
