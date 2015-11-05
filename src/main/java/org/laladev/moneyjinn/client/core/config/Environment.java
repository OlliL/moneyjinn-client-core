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
// $Id: Environment.java,v 1.1 2015/08/29 00:48:13 olivleh1 Exp $
//
package org.laladev.moneyjinn.client.core.config;

import java.text.SimpleDateFormat;
import java.util.Locale;

public final class Environment {
	private static final Environment instance = new Environment();
	private Long userId;
	private String userName;
	private String userPassword;
	private SimpleDateFormat dateFormat;

	private Environment() {
	};

	public static Environment getInstance() {
		return instance;
	}

	public final Long getUserId() {
		return userId;
	}

	public final void setUserId(final Long userId) {
		this.userId = userId;
	}

	public final String getUserName() {
		return userName;
	}

	public final void setUserName(final String userName) {
		this.userName = userName;
	}

	public final String getUserPassword() {
		return userPassword;
	}

	public final void setUserPassword(final String userPassword) {
		this.userPassword = userPassword;
	}

	public final SimpleDateFormat getDateFormat() {
		return dateFormat;
	}

	public final void setDateFormat(final String dateFormatString) {
		this.dateFormat = new SimpleDateFormat(dateFormatString.replace('Y', 'y').replace('D', 'd'), Locale.UK);
	}
}
