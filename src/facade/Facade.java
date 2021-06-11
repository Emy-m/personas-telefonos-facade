package facade;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Facade implements DBFacade {
	private String CONTROLADOR = "com.mysql.cj.jdbc.Driver";
	private String URL = "jdbc:mysql://localhost:3306/persona_proxy";
	private String USUARIO = "root";
	private String CLAVE = "";
	private Connection conexion;

	@Override
	public void open() {
		try {
			Class.forName(CONTROLADOR);
			conexion = DriverManager.getConnection(URL, USUARIO, CLAVE);
		} catch (ClassNotFoundException | SQLException e) {
			throw new RuntimeException("Hubo un error abriendo la conexion", e);
		}
	}

	@Override
	public List<Map<String, String>> queryResultAsAsociation(String sql) {
		try {
			open();
			PreparedStatement querySQL = conexion.prepareStatement(sql);
			ResultSet respuesta = querySQL.executeQuery();
			ResultSetMetaData metaDataRespuesta;
			String nombreColumna;

			Map<String, String> columnaYValor;
			List<Map<String, String>> filas = new ArrayList<Map<String, String>>();

			while (respuesta.next()) {
				metaDataRespuesta = respuesta.getMetaData();
				columnaYValor = new HashMap<String, String>();
				for (int i = 1; i <= metaDataRespuesta.getColumnCount(); i++) {
					nombreColumna = metaDataRespuesta.getColumnName(i);
					columnaYValor.put(nombreColumna, respuesta.getString(nombreColumna));
				}
				filas.add(columnaYValor);
			}

			close();
			return filas;
		} catch (SQLException e) {
			throw new RuntimeException("Hubo un error en la consulta", e);
		}
	}

	@Override
	public List<String[]> queryResultAsArray(String sql) {
		try {
			open();
			PreparedStatement querySQL = conexion.prepareStatement(sql);
			ResultSet respuesta = querySQL.executeQuery();
			ResultSetMetaData metaDataRespuesta;

			List<String[]> filas = new ArrayList<String[]>();
			List<String> valorColumnas;

			while (respuesta.next()) {
				metaDataRespuesta = respuesta.getMetaData();
				valorColumnas = new ArrayList<String>();
				for (int i = 1; i <= metaDataRespuesta.getColumnCount(); i++) {
					valorColumnas.add(respuesta.getString(i));
				}
				filas.add(valorColumnas.toArray(new String[valorColumnas.size()]));
			}

			close();
			return filas;
		} catch (SQLException e) {
			throw new RuntimeException("Hubo un error en la consulta", e);
		}
	}

	@Override
	public void close() {
		try {
			conexion.close();
		} catch (SQLException e) {
			throw new RuntimeException("Hubo un error cerrando la conexion");
		}
	}

}
