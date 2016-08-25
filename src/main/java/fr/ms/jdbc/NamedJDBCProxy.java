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
package fr.ms.jdbc;

import java.lang.reflect.InvocationHandler;
import java.sql.Connection;

import javax.sql.DataSource;

import fr.ms.jdbc.impl.ConnectionHandler;
import fr.ms.jdbc.impl.DataSourceHandler;
import fr.ms.lang.reflect.ProxyUtils;

/**
 *
 * @see <a href="http://marcosemiao4j.wordpress.com">Marco4J</a>
 *
 *
 * @author Marco Semiao
 *
 */
public class NamedJDBCProxy {

	private NamedJDBCProxy() {
	}

	public static NamedDataSource proxyDataSource(final DataSource dataSource) {

		if (dataSource == null) {
			throw new NullPointerException();
		}

		final InvocationHandler h = new DataSourceHandler(dataSource);
		final NamedDataSource instance = (NamedDataSource) ProxyUtils.newProxyInstance(dataSource, h,
				NamedDataSource.class);

		return instance;
	}

	public static NamedConnection proxyConnection(final Connection connection) {

		if (connection == null) {
			throw new NullPointerException();
		}

		final InvocationHandler h = new ConnectionHandler(connection);
		final NamedConnection instance = (NamedConnection) ProxyUtils.newProxyInstance(connection, h,
				NamedConnection.class);

		return instance;
	}
}
