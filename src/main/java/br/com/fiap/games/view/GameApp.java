package br.com.fiap.games.view;

import br.com.fiap.games.dao.GameDao;
import br.com.fiap.games.exception.EntidadeNaoEncontrada;
import br.com.fiap.games.factory.ConnectionFactory;
import br.com.fiap.games.model.Game;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class GameApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            //Instancia o DAO e o GAME com os dados inseridos pelo usuário
            GameDao dao = new GameDao();
            System.out.println("Escolha 1-Cadastrar, 2-Atualizar, 3-Pesquisar por Id, 4-listar, 5-Remover");
            int op = scanner.nextInt();
            switch (op){
                case 1:
                    Game game = lerGame(scanner);
                    dao.cadastrar(game);
                    System.out.println("Cadastrado com sucesso!");
                    break;
                case 2:
                    Game game2 = lerGame(scanner);
                    System.out.println("Digite o código do jogo");
                    long codigo = scanner.nextLong();
                    game2.setCodigo(codigo);
                    dao.atualizar(game2);
                    System.out.println("Jogo atualizado");
                    break;
                case 3:
                    System.out.println("Digite o codigo do jogo");
                    codigo = scanner.nextLong();
                    Game game3 = dao.pesquisar(codigo);
                    System.out.println(game3);
                    break;
                case 4:
                    List<Game> lista = dao.listar();
                    for (Game item : lista){
                        System.out.println(item);
                    }
                    break;
                case 5:
                    System.out.println("Digite o codigo do game");
                    codigo = scanner.nextLong();
                    dao.remover(codigo);
                    System.out.println("Jogo removido!");
                    break;
                default:
                    System.out.println("Opção inválida");
            }

        } catch (SQLException e){
            System.err.println(e.getMessage());
        } catch (EntidadeNaoEncontrada e) {
            System.out.println("Game não encontrado");
        }
    }

    private static Game lerGame(Scanner scanner) {
        //Pedir os dados do game para o usuário
        System.out.println("Digite o nome do jogo");
        String nome = scanner.next() + scanner.nextLine();
        System.out.println("Digite a nota do jogo");
        double nota = scanner.nextDouble();
        System.out.println("Digite o ano do jogo");
        int ano = scanner.nextInt();
        System.out.println("Digite a data de compra do jogo (dd/mm/aaaa)");
        String dataStr = scanner.next();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); //Formato da data
        LocalDate data = LocalDate.parse(dataStr,formatter); //Transformar a String em uma Data (LocalDate)
        System.out.println("Finalizou o jogo? (true/false)");
        boolean finalizado = scanner.nextBoolean();

        Game game = new Game(nome, nota, ano, data, finalizado);
        return game;
    }

}
