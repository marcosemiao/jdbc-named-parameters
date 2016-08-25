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

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 *
 * @see <a href="http://marcosemiao4j.wordpress.com">Marco4J</a>
 *
 *
 * @author Marco Semiao
 *
 */
class NamedQuery {

	private final Map<String, HashSet<Integer>> index = new HashMap<String, HashSet<Integer>>();

	private final String namedSql;

	private String sql;

	/**
	 * Set of characters that qualify as comment or quotes starting characters.
	 */
	private static final String[] START_SKIP = new String[] { "'", "\"", "--", "/*" };

	/**
	 * Set of characters that at are the corresponding comment or quotes ending
	 * characters.
	 */
	private static final String[] STOP_SKIP = new String[] { "'", "\"", "\n", "*/" };

	Map<String, HashSet<Integer>> getIndex() {
		return index;
	}

	String getNamedSql() {
		return namedSql;
	}

	String getSql() {
		return sql;
	}

	NamedQuery(final String namedSql) throws SQLException {
		this.namedSql = namedSql;
		this.sql = namedSql;

		final char[] statement = namedSql.toCharArray();

		int nbParameter = 1;

		int i = 0;
		while (i < statement.length) {
			int skipToPosition = i;
			while (i < statement.length) {
				skipToPosition = skipCommentsAndQuotes(statement, i);
				if (i == skipToPosition) {
					break;
				} else {
					i = skipToPosition;
				}
			}
			if (i >= statement.length) {
				break;
			}
			final char c = statement[i];

			if (c == ':' || c == '&') {
				int j = i + 1;
				if (j < statement.length && statement[j] == ':' && c == ':') {
					// Postgres-style "::" casting operator should be skipped
					i = i + 2;
					continue;
				}

				if (j < statement.length && c == ':' && statement[j] == '{') {
					// :{x} style parameter
					while (j < statement.length && !('}' == statement[j])) {
						j++;
						if (':' == statement[j] || '{' == statement[j]) {
							throw new SQLException("Parameter name contains invalid character '" + statement[j]
									+ "' at position " + i + " in statement: " + namedSql);
						}
					}
					if (j >= statement.length) {
						throw new SQLException("Non-terminated named parameter declaration at position " + i
								+ " in statement: " + namedSql);
					}
					if (j - i > 3) {
						final String parameter = namedSql.substring(i + 2, j);

						sql = sql.replaceFirst(":" + parameter, "?");

						HashSet<Integer> values = index.get(parameter);
						if (values == null) {
							values = new HashSet<Integer>();
							index.put(parameter, values);
						}
						values.add(nbParameter);

						nbParameter = nbParameter + 1;
					}
					j++;
				} else {
					while (j < statement.length && !isParameterSeparator(statement[j])) {
						j++;
					}
					if (j - i > 1) {
						final String parameter = namedSql.substring(i + 1, j);

						sql = sql.replaceFirst(":" + parameter, "?");

						HashSet<Integer> values = index.get(parameter);
						if (values == null) {
							values = new HashSet<Integer>();
							index.put(parameter, values);
						}
						values.add(nbParameter);

						nbParameter = nbParameter + 1;
					}
				}
				i = j - 1;
			}
			i++;
		}
	}

	/**
	 * Skip over comments and quoted names present in an SQL statement
	 * 
	 * @param statement
	 *            character array containing SQL statement
	 * @param position
	 *            current position of statement
	 * @return next position to process after any comments or quotes are skipped
	 */
	private static int skipCommentsAndQuotes(final char[] statement, final int position) {
		for (int i = 0; i < START_SKIP.length; i++) {
			if (statement[position] == START_SKIP[i].charAt(0)) {
				boolean match = true;
				for (int j = 1; j < START_SKIP[i].length(); j++) {
					if (!(statement[position + j] == START_SKIP[i].charAt(j))) {
						match = false;
						break;
					}
				}
				if (match) {
					final int offset = START_SKIP[i].length();
					for (int m = position + offset; m < statement.length; m++) {
						if (statement[m] == STOP_SKIP[i].charAt(0)) {
							boolean endMatch = true;
							int endPos = m;
							for (int n = 1; n < STOP_SKIP[i].length(); n++) {
								if (m + n >= statement.length) {
									// last comment not closed properly
									return statement.length;
								}
								if (!(statement[m + n] == STOP_SKIP[i].charAt(n))) {
									endMatch = false;
									break;
								}
								endPos = m + n;
							}
							if (endMatch) {
								// found character sequence ending comment or
								// quote
								return endPos + 1;
							}
						}
					}
					// character sequence ending comment or quote not found
					return statement.length;
				}

			}
		}
		return position;
	}

	private static final char[] PARAMETER_SEPARATORS = new char[] { '"', '\'', ':', '&', ',', ';', '(', ')', '|', '=',
			'+', '-', '*', '%', '/', '\\', '<', '>', '^' };

	private static boolean isParameterSeparator(final char c) {
		if (Character.isWhitespace(c)) {
			return true;
		}
		for (final char separator : PARAMETER_SEPARATORS) {
			if (c == separator) {
				return true;
			}
		}
		return false;
	}
}
