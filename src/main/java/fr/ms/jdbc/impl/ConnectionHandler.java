/*
 * Copyright 2016 Marco Semiao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package fr.ms.jdbc.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.Map;

import fr.ms.jdbc.NamedPreparedStatement;
import fr.ms.lang.reflect.ProxyUtils;

/**
 *
 * @see <a href="http://marcosemiao4j.wordpress.com">Marco4J</a>
 *
 *
 * @author Marco Semiao
 *
 */
public class ConnectionHandler implements InvocationHandler {

	private final Connection connection;

	public ConnectionHandler(final Connection connection) {
		this.connection = connection;
	}

	public Object invoke(final Object proxy, Method method, final Object[] args) throws Throwable {

		Map<String, HashSet<Integer>> index = null;

		final Class<?> returnType = method.getReturnType();

		if (returnType.equals(NamedPreparedStatement.class)) {
			method = Connection.class.getMethod(method.getName(), method.getParameterTypes());

			if (args[0] != null && args[0] instanceof String) {
				final String sql = (String) args[0];

				final NamedQuery namedQuery = new NamedQuery(sql);

				index = namedQuery.getIndex();

				if (!index.isEmpty()) {
					args[0] = namedQuery.getSql();
				}
			}
		}

		Object invoke;

		try {
			invoke = method.invoke(connection, args);
		} catch (final InvocationTargetException ite) {
			final Throwable targetException = ite.getTargetException();
			throw targetException;
		}

		if (invoke instanceof PreparedStatement) {
			final PreparedStatement statement = (PreparedStatement) invoke;
			final InvocationHandler h = new NamedPreparedStatementHandler(statement, index);
			final NamedPreparedStatement instance = (NamedPreparedStatement) ProxyUtils.newProxyInstance(statement, h,
					NamedPreparedStatement.class);

			return instance;
		}

		return invoke;
	}
}
