# JDBC Named Parameters

## Fonctionnalités générales
Cette librairie permet d'utiliser les paramètres nommées avec du jdbc.

- Facile d'utilisation, il suffit de rajouter la dépendance Maven dans votre application.
- Disponible sur le repository central de Maven.
- Compatible à partir de Java 5.

## Utilisation rapide
- Ajouter la dépendance dans votre projet :

````xml
<dependency>
	<groupId>com.github.marcosemiao</groupId>
	<artifactId>jdbc-named-parameters</artifactId>
	<version>0.0.1</version>
</dependency>
````

- Dans votre code, il suffit d'encapulser votre DataSource ou Connection

Exemple avec une DataSource

````java
	final OpenJPAEntityManager cast = OpenJPAPersistence.cast(entityManager);
	final OpenJPAConfiguration conf = cast.getConfiguration();
	final DataSource dataSource = (DataSource) conf.getConnectionFactory();
	
    /* Proxy - JDBC Named Parameters */
	final NamedDataSource proxyDataSource = NamedJDBCProxy.proxyDataSource(dataSource);
	
	final NamedConnection connection = proxyDataSource.getConnection();

	final NamedPreparedStatement statement = connection.prepareStatement(sql);
	statement.setInt("siren", siren);
	statement.setString("matricule", matricule);
    
    statement.executeUpdate();
````

Exemple avec une Connection

````java
	final OpenJPAEntityManager cast = OpenJPAPersistence.cast(entityManager);
	final Connection connection = (Connection) cast.getConnection();

	/* Proxy - JDBC Named Parameters */
	final NamedConnection proxyConnection = NamedJDBCProxy.proxyConnection(connection);

	final NamedPreparedStatement statement = connection.prepareStatement(sql);
	statement.setInt("siren", siren);
	statement.setString("matricule", matricule);
    
    statement.executeUpdate();
````