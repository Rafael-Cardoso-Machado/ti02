package ex03;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutosDAO extends DAO {
	
	public ProdutosDAO() {
		super();
		conectar();
	}

	public void finalize() {
		close();
	}
	
	
	public boolean insert(Produtos produto) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			String sql = "INSERT INTO produto (codigo, nome, preco) "
				       + "VALUES ("+produto.getCodigo()+ ", '" + produto.getNome() + "', "  
				       + produto.getPreco() + ");";
			System.out.println(sql);
			st.executeUpdate(sql);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}

	
	public Produtos get(int codigo) {
		Produtos produto = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM produto WHERE codigo=" + codigo;
			System.out.println(sql);
			ResultSet rs = st.executeQuery(sql);	
	        if(rs.next()){
	        	 produto = new Produtos(rs.getInt("codigo"), rs.getString("nome"), rs.getDouble("preco"));
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return produto;
	}
	
	
	public List<Produtos> get() {
		return get("");
	}

	
	public List<Produtos> getOrderByCodigo() {
		return get("codigo");		
	}
	
	
	public List<Produtos> getOrderByNome() {
		return get("nome");		
	}
	
	
	public List<Produtos> getOrderByPreco() {
		return get("preco");		
	}
	
	
	private List<Produtos> get(String orderBy) {	
	
		List<Produtos> produtos = new ArrayList<Produtos>();
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM produto" + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
			System.out.println(sql);
			ResultSet rs = st.executeQuery(sql);	           
	        while(rs.next()) {
	        	Produtos p = new Produtos(rs.getInt("codigo"), rs.getString("nome"), rs.getDouble("preco"));
	            produtos.add(p);
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return produtos;
	}
	
	
	public boolean update(Produtos produto) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			String sql = "UPDATE produto SET nome = '" + produto.getNome() + "', preco = "  
				       + produto.getPreco()
					   + " WHERE codigo = " + produto.getCodigo();
			System.out.println(sql);
			st.executeUpdate(sql);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
	
	public boolean delete(int codigo) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			String sql = "DELETE FROM produto WHERE codigo = " + codigo;
			System.out.println(sql);
			st.executeUpdate(sql);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
}