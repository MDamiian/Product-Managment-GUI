package com.alura.jdbc.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.alura.jdbc.modelo.Producto;

public class ProductoDAO {
	final private Connection con;

	public ProductoDAO(Connection con) {
		this.con = con;
	}

	public void guardar(Producto producto) {
		try {
			final java.sql.PreparedStatement statement = con.prepareStatement(
					"INSERT INTO PRODUCTO (nombre, descripcion, cantidad, categoria_id) VALUES (?, ?, ?, ?)",
					java.sql.Statement.RETURN_GENERATED_KEYS);

			try (statement) {
				statement.setString(1, producto.getNombre());
				statement.setString(2, producto.getDescripcion());
				statement.setInt(3, producto.getCantidad());
				statement.setInt(4, producto.getCategoriaId());
				statement.execute();

				final ResultSet resultSet = statement.getGeneratedKeys();

				try (resultSet) {
					while (resultSet.next()) {
						producto.setId(resultSet.getInt(1));
						System.out.println(String.format("Fue insertado el producto de ID %s", producto.toString()));
					}
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Producto> listar() {
		List<Producto> resultado = new ArrayList<>();

		try {
			final java.sql.PreparedStatement statement = con.prepareStatement("SELECT * FROM PRODUCTO");

			try (statement) {
				statement.execute();
				final ResultSet resultSet = statement.getResultSet();

				try (resultSet) {
					while (resultSet.next()) {
						Producto fila = new Producto(resultSet.getInt("ID"), resultSet.getString("NOMBRE"),
								resultSet.getString("DESCRIPCION"), resultSet.getInt("CANTIDAD"));

						resultado.add(fila);
					}
				}
				return resultado;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public int eliminar(Integer id) {
		try {
			final java.sql.PreparedStatement statement = con.prepareStatement("DELETE FROM PRODUCTO WHERE ID = ?");

			try (statement) {
				statement.setInt(1, id);
				statement.execute();
				return statement.getUpdateCount();
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void modificar(Producto producto) {
		try {
			final java.sql.PreparedStatement statement = con
					.prepareStatement("UPDATE producto SET NOMBRE = ?, DESCRIPCION = ?, CANTIDAD = ? WHERE id = ?");

			try (statement) {
				statement.setString(1, producto.getNombre());
				statement.setString(2, producto.getDescripcion());
				statement.setInt(3, producto.getCantidad());
				statement.setInt(4, producto.getId());
				statement.execute();
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Producto> listar(Integer categoriaId) {
		List<Producto> resultado = new ArrayList<>();

		try {
			final java.sql.PreparedStatement statement = con.prepareStatement("SELECT * FROM PRODUCTO WHERE categoria_id = ?");

			try (statement) {
				statement.setInt(1, categoriaId);
				statement.execute();
				final ResultSet resultSet = statement.getResultSet();

				try (resultSet) {
					while (resultSet.next()) {
						Producto fila = new Producto(resultSet.getInt("ID"), resultSet.getString("NOMBRE"),
								resultSet.getString("DESCRIPCION"), resultSet.getInt("CANTIDAD"));

						resultado.add(fila);
					}
				}
				return resultado;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
