//
// Copyright (c) 2014-2015 Oliver Lehmann <oliver@laladev.org>
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
// $Id: RequestInterceptor.java,v 1.9 2015/09/20 10:48:51 olivleh1 Exp $
//
package org.laladev.moneyjinn.client.core.rest.util;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.laladev.moneyjinn.client.core.config.Environment;
import org.laladev.moneyjinn.core.rest.util.BytesToHexConverter;
import org.laladev.moneyjinn.core.rest.util.RESTAuthorization;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class RequestInterceptor implements ClientHttpRequestInterceptor {
	private final RESTAuthorization restAuthorization;
	private final SimpleDateFormat dateFormatter;
	private MessageDigest sha1MD;

	public RequestInterceptor() {
		this.restAuthorization = new RESTAuthorization();
		this.dateFormatter = new SimpleDateFormat(RESTAuthorization.DATE_HEADER_FORMAT, Locale.US);
		this.dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		try {
			this.sha1MD = MessageDigest.getInstance("SHA1");
		} catch (final NoSuchAlgorithmException e) {
		}
	}

	public ClientHttpResponse intercept(final HttpRequest request, final byte[] body,
			final ClientHttpRequestExecution execution) throws IOException {

		this.sha1MD.reset();

		final Date now = new Date(System.currentTimeMillis());
		final String date = this.dateFormatter.format(now);

		final String userName = Environment.getInstance().getUserName();
		final String userPassword = Environment.getInstance().getUserPassword();

		String contentType = null;
		if (request.getHeaders().getContentType() != null) {
			contentType = request.getHeaders().getContentType().toString();
		}
		byte[] secret = null;
		if (userPassword != null) {
			secret = BytesToHexConverter.convert(this.sha1MD.digest(userPassword.getBytes())).getBytes();
		}

		String authString;
		try {
			authString = this.restAuthorization.getRESTAuthorization(secret, request.getMethod().toString(),
					contentType, request.getURI().getPath(), date, body, userName);
			request.getHeaders().add(RESTAuthorization.DATE_HEADER_NAME, date);
			request.getHeaders().add(RESTAuthorization.AUTH_HEADER_NAME, authString.trim());
		} catch (final InvalidKeyException e) {
			e.printStackTrace();
		} catch (final NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return execution.execute(request, body);
	}

}
