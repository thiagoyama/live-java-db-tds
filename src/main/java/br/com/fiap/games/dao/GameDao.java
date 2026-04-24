package br.com.fiap.games.dao;

import br.com.fiap.games.exception.EntidadeNaoEncontrada;
import br.com.fiap.games.factory.ConnectionFactory;
import br.com.fiap.games.model.Game;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GameDao {

    private Connection conexao;

    public GameDao() throws SQLException {
        conexao = ConnectionFactory.getConnection();
    }

    public void cadastrar(Game game) throws SQLException {
        //Cria o comando SQL para executar no banco de dados
        PreparedStatement stmt = conexao.prepareStatement("insert into tb_game " +
                "(cd_game, nm_game, nr_nota, nr_ano, dt_compra, st_finalizado, cd_plataforma) " +
                "values (seq_game.nextVal, ?, ?, ?, ?, ?, ?)");

        popularStatement(game, stmt);

        stmt.executeUpdate(); //Executa o comando SQL no banco de dados
        stmt.close(); //Fecha o comando SQL
    }

    private static void popularStatement(Game game, PreparedStatement stmt) throws SQLException {
        //Setando os dados no comando SQL
        stmt.setString(1, game.getNome());
        stmt.setDouble(2, game.getNota());
        stmt.setInt(3, game.getAno());
        stmt.setDate(4, Date.valueOf(game.getDataCompra()));
        stmt.setBoolean(5, game.isFinalizado());
    }

    public void atualizar(Game game) throws SQLException, EntidadeNaoEncontrada {
        //Criar o comando SQL para executar no banco de dados
        PreparedStatement stmt = conexao.prepareStatement("update tb_game set nm_game = ?, nr_nota = ?,nr_ano = ?," +
                "dt_compra = ?, st_finalizado = ? where cd_game = ?");
        //Setando os dados no comando SQL
        popularStatement(game, stmt);
        stmt.setLong(6, game.getCodigo());

        int linhas = stmt.executeUpdate();
        stmt.close();
        //validar se realmente atualizou o game no banco
        if (linhas == 0)
            throw new EntidadeNaoEncontrada();
    }

    public Game pesquisar(long codigo) throws SQLException, EntidadeNaoEncontrada {
        PreparedStatement stmt = conexao.prepareStatement("select * from tb_game where cd_game = ?");
        stmt.setLong(1, codigo);

        ResultSet resultSet = stmt.executeQuery();
        //Posicionar o cursor na primeira linha e retorna true se for possivel (se existir dados)
        if (resultSet.next()) {
            String nome  = resultSet.getString("nm_game");
            double nota = resultSet.getDouble("nr_nota");
            int ano = resultSet.getInt("nr_ano");
            LocalDate data =  resultSet.getDate("dt_compra").toLocalDate();
            boolean finalizado = resultSet.getBoolean("st_finalizado");
            return new Game(codigo, nome, nota, ano, data, finalizado);
        }
        throw new EntidadeNaoEncontrada();
    }

    public List<Game> listar() throws SQLException {
        PreparedStatement stmt = conexao.prepareStatement("select * from tb_game");
        ResultSet resultSet = stmt.executeQuery();
        List<Game> lista = new ArrayList<>();
        while (resultSet.next()){
            long codigo = resultSet.getLong("cd_game");
            String nome  = resultSet.getString("nm_game");
            double nota = resultSet.getDouble("nr_nota");
            int ano = resultSet.getInt("nr_ano");
            LocalDate data =  resultSet.getDate("dt_compra").toLocalDate();
            boolean finalizado = resultSet.getBoolean("st_finalizado");
            Game game = new Game(codigo, nome, nota, ano, data, finalizado);
            lista.add(game);
        }
        stmt.close();
        return lista;
    }

    public void remover(long codigo) throws SQLException, EntidadeNaoEncontrada {
        PreparedStatement stmt = conexao.prepareStatement("delete tb_game where cd_game = ?");
        stmt.setLong(1, codigo);
        int linhas = stmt.executeUpdate();
        //validar se realmente apagou um game no banco
        if (linhas == 0)
            throw new EntidadeNaoEncontrada();
        stmt.close();
    }

}
