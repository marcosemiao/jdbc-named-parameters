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
package fr.ms.sql.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.Map;

import fr.ms.sql.NamedPreparedStatement;

/**
 *
 * @see <a href="http://marcosemiao4j.wordpress.com">Marco4J</a>
 *
 *
 * @author Marco Semiao
 *
 */
class NamedPreparedStatementHandler implements InvocationHandler {

	private final PreparedStatement statement;

	private final Map<String, HashSet<Integer>> index;

	NamedPreparedStatementHandler(final PreparedStatement statement, final Map<String, HashSet<Integer>> index) {
		this.statement = statement;
		this.index = index;
	}

	public Object invoke(final Object proxy, Method method, final Object[] args) throws Throwable {
		final Class<?> declaringClass = method.getDeclaringClass();

		String named = null;
		if (declaringClass.equals(NamedPreparedStatement.class)) {
			if (args[0] != null && args[0] instanceof String) {
				named = (String) args[0];

				final Class<?>[] parameterTypes = method.getParameterTypes();
				parameterTypes[0] = Integer.TYPE;

				method = PreparedStatement.class.getMethod(method.getName(), parameterTypes);
			}
		}

		try {
			Object invoke = null;
			if (named != null && index != null && !index.isEmpty()) {
				final HashSet<Integer> parameterIndexTab = index.get(named);
				for (final Integer parameterIndex : parameterIndexTab) {
					args[0] = parameterIndex;
					invoke = method.invoke(statement, args);
				}

			} else {
				invoke = method.invoke(statement, args);
			}
			return invoke;
		} catch (final InvocationTargetException ite) {
			final Throwable targetException = ite.getTargetException();
			throw targetException;
		}
	}
}
