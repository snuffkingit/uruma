/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.uruma.example.employee.exception;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class AppRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 3915897008168682928L;

	private static final Object[] EMPTY_ARGS = new Object[0];

	private static final String BUNDLE_NAME = "appMessages";

	private String messageId;

	private Object[] args;

	public AppRuntimeException(final String messageId) {
		this(messageId, EMPTY_ARGS);
	}

	public AppRuntimeException(final String messageId, final Object[] args) {
		this(messageId, args, null);
	}

	public AppRuntimeException(final String messageId, final Object[] args,
			final Throwable cause) {
		initCause(cause);
		this.messageId = messageId;
		this.args = args;
	}

	public Object[] getArgs() {
		return args;
	}

	public String getMessageId() {
		return messageId;
	}

	@Override
	public String getMessage() {
		ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME);
		String pattern = bundle.getString(messageId);
		return MessageFormat.format(pattern, args);
	}
}
