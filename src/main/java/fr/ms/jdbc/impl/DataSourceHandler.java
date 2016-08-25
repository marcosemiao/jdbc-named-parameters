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

import javax.sql.DataSource;

import fr.ms.jdbc.NamedConnection;
import fr.ms.jdbc.NamedJDBCProxy;

/**
 *
 * @see <a href="http://marcosemiao4j.wordpress.com">Marco4J</a>
 *
 *
 * @author Marco Semiao
 *
 */
public class DataSourceHandler implements InvocationHandler {

	private final DataSource dataSource;

	public DataSourceHandler(final DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Object invoke(final Object proxy, Method method, final Object[] args) throws Throwable {

		final Class<?> returnType = method.getReturnType();

		if (returnType.equals(NamedConnection.class)) {
			method = DataSource.class.getMethod(method.getName(), method.getParameterTypes());
		}

		Object invoke;

		try {
			invoke = method.invoke(dataSource, args);
		} catch (final InvocationTargetException ite) {
			final Throwable targetException = ite.getTargetException();
			throw targetException;
		}

		if (invoke instanceof Connection) {
			final Connection connection = (Connection) invoke;
			final NamedConnection proxyConnection = NamedJDBCProxy.proxyConnection(connection);
			return proxyConnection;
		}

		return invoke;
	}

}
