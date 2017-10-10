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
package fr.ms.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.SQLXML;
import java.util.Calendar;

/**
 *
 * @see <a href="http://marcosemiao4j.wordpress.com">Marco4J</a>
 *
 *
 * @author Marco Semiao
 *
 */
public interface NamedPreparedStatement extends PreparedStatement {

	void setNull(String parameterName, int sqlType) throws SQLException;

	void setBoolean(String parameterName, boolean x) throws SQLException;

	void setByte(String parameterName, byte x) throws SQLException;

	void setShort(String parameterName, short x) throws SQLException;

	void setInt(String parameterName, int x) throws SQLException;

	void setLong(String parameterName, long x) throws SQLException;

	void setFloat(String parameterName, float x) throws SQLException;

	void setDouble(String parameterName, double x) throws SQLException;

	void setBigDecimal(String parameterName, BigDecimal x) throws SQLException;

	void setString(String parameterName, String x) throws SQLException;

	void setBytes(String parameterName, byte x[]) throws SQLException;

	void setDate(String parameterName, java.sql.Date x) throws SQLException;

	void setTime(String parameterName, java.sql.Time x) throws SQLException;

	void setTimestamp(String parameterName, java.sql.Timestamp x) throws SQLException;

	void setAsciiStream(String parameterName, java.io.InputStream x, int length) throws SQLException;

	@Deprecated
	void setUnicodeStream(String parameterName, java.io.InputStream x, int length) throws SQLException;

	void setBinaryStream(String parameterName, java.io.InputStream x, int length) throws SQLException;

	void setObject(String parameterName, Object x, int targetSqlType) throws SQLException;

	void setObject(String parameterName, Object x) throws SQLException;

	void setCharacterStream(String parameterName, java.io.Reader reader, int length) throws SQLException;

	void setRef(String parameterName, Ref x) throws SQLException;

	void setBlob(String parameterName, Blob x) throws SQLException;

	void setClob(String parameterName, Clob x) throws SQLException;

	void setArray(String parameterName, Array x) throws SQLException;

	void setDate(String parameterName, java.sql.Date x, Calendar cal) throws SQLException;

	void setTime(String parameterName, java.sql.Time x, Calendar cal) throws SQLException;

	void setTimestamp(String parameterName, java.sql.Timestamp x, Calendar cal) throws SQLException;

	void setNull(String parameterName, int sqlType, String typeName) throws SQLException;

	void setURL(String parameterName, java.net.URL x) throws SQLException;

	void setRowId(String parameterName, RowId x) throws SQLException;

	void setNString(String parameterName, String value) throws SQLException;

	void setNCharacterStream(String parameterName, Reader value, long length) throws SQLException;

	void setNClob(String parameterName, NClob value) throws SQLException;

	void setClob(String parameterName, Reader reader, long length) throws SQLException;

	void setBlob(String parameterName, InputStream inputStream, long length) throws SQLException;

	void setNClob(String parameterName, Reader reader, long length) throws SQLException;

	void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException;

	void setObject(String parameterName, Object x, int targetSqlType, int scaleOrLength) throws SQLException;

	void setAsciiStream(String parameterName, java.io.InputStream x, long length) throws SQLException;

	void setBinaryStream(String parameterName, java.io.InputStream x, long length) throws SQLException;

	void setCharacterStream(String parameterName, java.io.Reader reader, long length) throws SQLException;

	void setAsciiStream(String parameterName, java.io.InputStream x) throws SQLException;

	void setBinaryStream(String parameterName, java.io.InputStream x) throws SQLException;

	void setCharacterStream(String parameterName, java.io.Reader reader) throws SQLException;

	void setNCharacterStream(String parameterName, Reader value) throws SQLException;

	void setClob(String parameterName, Reader reader) throws SQLException;

	void setBlob(String parameterName, InputStream inputStream) throws SQLException;

	void setNClob(String parameterName, Reader reader) throws SQLException;

	void setObject(String parameterName, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException;

	void setObject(String parameterName, Object x, SQLType targetSqlType) throws SQLException;
}
