package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Aula;
import model.AulaDto;

public class Db {

	private static Db instance = null;
	private Connection connection = null;

	private String driver;
	private String url;
	private String user;
	private String password;

	private Db() {
		this.confDB();
		this.conectar();
		this.criarTabela();
	}

	public static Db getInstance() {
		if (instance == null) {
			instance = new Db();
		}
		return instance;
	}

	private void confDB() {
		try {
			this.driver = "org.h2.Driver";
			this.url = "jdbc:h2:~/testdb";
			this.user = "sa";
			this.password = "";
			Class.forName(this.driver);
		} catch (ClassNotFoundException e) {
		    System.err.println("Class not found: " + e.getMessage());
		}
	}

	
	private void conectar() {
		try {
			this.connection = DriverManager.getConnection(this.url, this.user, this.password);
		} catch (SQLException e) {
			 System.err.println("Class not found: " + e.getMessage());	
		}
	}

	private void criarTabela() {
		String db = "CREATE TABLE IF NOT EXISTS AULA ("
				+ "    ID BIGINT AUTO_INCREMENT PRIMARY KEY,"
				+ "    COD_DISCIPLINA INT,"
				+ "    ASSUNTO VARCHAR(255),"
				+ "    DURACAO INT,"
				+ "    DATA VARCHAR(20),"
				+ "    HORARIO VARCHAR(20)"
				+ ")";
		try {
			Statement statement = this.connection.createStatement();
			statement.executeUpdate(db);
			this.connection.commit();
		} catch (SQLException e) {
			 System.err.println("Class not found: " + e.getMessage());
		}
	}

	
	public void close() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			 System.err.println("Class not found: " + e.getMessage());	
		}
	}
	
	
		private static void closeStatement(Statement statement) {
			if(statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					 System.err.println("Class not found: " + e.getMessage());
				}
			}
		}
		
		
		private static void closeResultSet(ResultSet result) {
			if(result != null) {
				try {
					result.close();
				} catch (SQLException e) {
					 System.err.println("Class not found: " + e.getMessage());
				}
			}
		}

	

	
		public ArrayList<AulaDto> findAll() {
		    PreparedStatement preparedStatement = null;
		    ResultSet result = null;
		    String db = "SELECT ID, COD_DISCIPLINA, ASSUNTO, DURACAO, DATA, HORARIO FROM AULA;";
		    ArrayList<AulaDto> lista = new ArrayList<AulaDto>();
		    try {
		        preparedStatement = connection.prepareStatement(db);
		        result = preparedStatement.executeQuery();

		        while (result.next()) {
		            Aula aula = instantiateAula(result);
		            AulaDto aulaDto = new AulaDto(aula);
		            lista.add(aulaDto);
		        }

		        return lista;
		    } catch (SQLException e) {
		        System.err.println("SQL Error: " + e.getMessage());
		    } finally {
		        closeResultSet(result);
		        closeStatement(preparedStatement);
		    }
		    return lista;
		}


		public AulaDto findById(String id) {
		    PreparedStatement preparedStatement = null;
		    ResultSet result = null;
		    String db = "SELECT ID, COD_DISCIPLINA, ASSUNTO, DURACAO, DATA, HORARIO FROM AULA WHERE ID = ?";

		    try {
		        preparedStatement = connection.prepareStatement(db);
		        preparedStatement.setString(1, id);

		        result = preparedStatement.executeQuery();

		        if (result.next()) {
		            Aula aula = instantiateAula(result);
		            AulaDto aulaDto = new AulaDto(aula);
		            return aulaDto;
		        }

		        return null;

		    } catch (SQLException e) {
		        System.err.println("SQL Error: " + e.getMessage());
		    } finally {
		        closeResultSet(result);
		        closeStatement(preparedStatement);
		    }
		    return null;
		}

	
	public void create(AulaDto dto) {
		PreparedStatement prepare = null;
		String db = "INSERT INTO AULA (COD_DISCIPLINA, ASSUNTO, DURACAO, DATA, HORARIO) "
				+ "VALUES (?,?,?,?,?)";
		try {
			 Aula aula = new Aula(dto);

		        prepare = this.connection.prepareStatement(db);

		        prepare.setInt(1, aula.getCodDisciplina());
		        prepare.setString(2, aula.getAssunto());
		        prepare.setInt(3, aula.getDuracao());
		        prepare.setString(4, aula.getData());
		        prepare.setString(5, aula.getHorario());
		        prepare.execute();
		} catch (SQLException e) {
			System.err.println("Class not found: " + e.getMessage());
		} finally {
			closeStatement(prepare);
		}
	}

	
	public void deleteAll() {
		String db = "DELETE FROM AULA";
		Statement statement = null;
		try {
			statement = this.connection.createStatement();
			statement.execute(db);
		} catch (SQLException e) {
			e.getStackTrace();
			System.err.println("Class not found: " + e.getMessage());
		}
		finally {
			closeStatement(statement);
		}
	}

	
	public void delete(String id) {
		String db = "DELETE FROM AULA WHERE ID = ?";
		PreparedStatement prepare = null;
		try {
			prepare = this.connection.prepareStatement(db);
			prepare.setString(1, id);
			prepare.execute();
		} catch (SQLException e) {
			System.err.println("Class not found: " + e.getMessage());
		} finally {
	        closeStatement(prepare);
	    }
	}

	
	public void update(AulaDto dto) {
		PreparedStatement preparedStatement = null;
		String db = "UPDATE AULA SET "
				+ "COD_DISCIPLINA = ?, ASSUNTO = ?, DURACAO = ?, DATA = ?, HORARIO = ? "
				+ "WHERE ID = ?";
		
		 try {
		        preparedStatement = this.connection.prepareStatement(db);
		        preparedStatement.setInt(1, Integer.parseInt(dto.codDisciplina)); 
		        preparedStatement.setString(2, dto.assunto); 
		        preparedStatement.setInt(3, Integer.parseInt(dto.duracao)); 
		        preparedStatement.setString(4, dto.data);
		        preparedStatement.setString(5, dto.horario);
		        preparedStatement.setString(6, dto.id);
		        preparedStatement.executeUpdate();
		    } catch (SQLException e) {
		        e.printStackTrace();
		        System.err.println("Class not found: " + e.getMessage());
		    } finally {
		        closeStatement(preparedStatement);
		    }
		}
	
	
	
		private Aula instantiateAula(ResultSet result) throws SQLException{
			Aula aula = new Aula();
			aula.setAssunto(result.getString("ASSUNTO"));
			aula.setCodDisciplina(result.getInt("COD_DISCIPLINA"));
			aula.setData(result.getString("DATA"));
			aula.setDuracao(result.getInt("DURACAO"));
			aula.setHorario(result.getString("HORARIO"));
			aula.setId(result.getLong("ID"));
			return aula;
		}

	

		public void reset() {
			this.deleteAll();
		}
	
		

	}
