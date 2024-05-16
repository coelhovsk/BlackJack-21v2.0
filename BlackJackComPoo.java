import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class BlackJackComPoo {
    // #region variáveis globais
    public static int quantidadeJogadores = 0;
    public static int jogadoresDesativados = 0;
    public static int qtdJogadoresPassaram = 0;
    public static int[] baralho = new int[13];
    final static Scanner SC = new Scanner(System.in);
    final static Random ROLL = new Random();
    // //#endregion

    // #region Iniciando CORES
    public static final String RESETAR_COR = "\u001B[0m";
    public static final String COR_VERMELHA = "\u001B[31m";
    public static final String COR_VERDE = "\u001B[32m";
    public static final String COR_AMARELA = "\u001B[33m";
    // #endregion

    public static void main(String[] args) throws InterruptedException {
        // #region Inicio
        System.out.println("-------------------------------");
        System.out.println("BLACKJACK --- 21");

        // Solicita a quantidade de jogadores, preenche a mão e o baralho
        quantidadeJogadores = quantidadeJogadores(); // quantidade de jogadores
        preencherBaralho(quantidadeJogadores); // preenche a variavel baralho
        Jogadores[] jogadores = new Jogadores[quantidadeJogadores]; // vetor de jogadores, utilizando objeto
        for (int i = 0; i < jogadores.length; i++) {
            if (i == 0) {
                SC.nextLine(); // a primeira leitura não está funcionando, se tirar essa linha vai quebrar o codigo.
            }

            System.out.print("Nome do jogador " + (i + 1) + ": ");
            String nome = SC.nextLine();// envia o nome digitado
            ArrayList<Integer> maoInicial = new ArrayList<>(); // cria a mão inicial
            rodarMaoInicial(maoInicial); // preenche a mão inicial com 2 cartas
            jogadores[i] = new Jogadores(nome, maoInicial);
        }
        // //#endregion

        // #region loop infinito do jogo, compra de cartas até finalizar
        while (true) {
            for (int i = 0; i < jogadores.length; i++) { // roda o vetor para que todos jogadores possam jogar
                if (jogadores[i].getMaoDoJogador().isEmpty()) { // verifica se o jogador estiver sem cartas(ou seja,eliminado)
                    System.out.println("-------------------------------");
                    System.out.println(jogadores[i].getNome() + " está fora do jogo (sem cartas).");
                    continue; // passa para o próximo jogador
                }
                if (quantidadeJogadores - jogadoresDesativados == 1) {
                    System.out.println(COR_VERDE + jogadores[i].getNome() + " ganhou!" + RESETAR_COR);
                    System.out.println("Fim do jogo.");
                    System.exit(0);
                }
                String compraString = null; // declarando variavel para usar no do{
                boolean compra = false; // declarando variavel para usar no do{
                int gambiarraDoDiabo = 1; // gambiarra pro loop do do{ funcionar de modo simples

                // #region PERGUNTA DESEJA COMPRAR
                do { // criado sem um else pois isso gastaria mais "memória"
                    System.out.println("-------------------------------");
                    System.out.println(jogadores[i].getNome() + ", suas cartas atuais são: " + jogadores[i].getMaoDoJogador()); // mostra as cartas atuais
                    if (jogadores[i].getValorDaMaoDoJogador() == 21 && jogadores[i].getMaoDoJogador().size() == 2) { // verifica se deu BJ
                        System.out.println(COR_VERDE + jogadores[i].getNome() + " venceu com um Blackjack!" + RESETAR_COR);
                        System.out.println("Fim do jogo.");
                        System.exit(0);
                    }
                    System.out.println(jogadores[i].getNome() + ", deseja comprar?");
                    System.out.println("-------------------------------");
                    compraString = SC.next().toUpperCase();
                    if ((compraString.charAt(0) == 'S' || compraString.charAt(0) == 'Y')
                            && compraString.length() <= 3) {
                        compra = true;// caso a palavra se assemelhe com sim ou yes
                        gambiarraDoDiabo = 1;
                    } else if ((compraString.charAt(0) != 'N' && compraString.length() <= 3) || compraString.length() > 3) {
                        System.out.println(COR_VERMELHA + "Apenas diga sim ou não" + RESETAR_COR);
                        gambiarraDoDiabo = 0; // caso a palavra não se assemelhe com nada
                    }
                } while (gambiarraDoDiabo == 0);
                // #endregion

                Thread.sleep(500);

                if (compra) { // se a compra for "sim"
                    comprarCarta(jogadores, i);
                    System.out.println(jogadores[i].getNome() + " comprou a carta: " + jogadores[i].getCartaComprada());
                    System.out.println("Suas cartas atuais são: " + jogadores[i].getMaoDoJogador());

                    if (jogadores[i].getValorDaMaoDoJogador() == 21) { // verifica se ganhou
                        System.out.println(COR_VERDE + jogadores[i].getNome() + " venceu!" + RESETAR_COR);
                        System.out.println("Fim do jogo.");
                        System.exit(0);
                    } else if (jogadores[i].getValorDaMaoDoJogador() > 21) { // verifica se a mão estorou
                        jogadores[i].perdeu();
                        System.out.println(COR_VERMELHA + jogadores[i].getNome() + " estourou!" + RESETAR_COR);
                        jogadoresDesativados++;
                    }
                } else { //se não comprou
                    qtdJogadoresPassaram++; // a qtd de qm não comprou aumenta
                    if (qtdJogadoresPassaram == (quantidadeJogadores - jogadoresDesativados)) { // se todos passaram
                        finalizarJogo(jogadores); // o jogo finaliza, vendo quem ganha ou se empata
                    }
                }
            }
        }
        // #endregion
    }

    public static void finalizarJogo(Jogadores jogador[]) {
        ArrayList<String> ganhadores = new ArrayList<String>(); // arraylist pois não sei quantos jogadores irão ganhar
        int maior = jogador[0].getValorDaMaoDoJogador(); // maior valor da mesa
        for (int i = 1; i < jogador.length; i++) { // rodo for para ver qual é o maior
            if (jogador[i].getValorDaMaoDoJogador() > maior) {
                maior = jogador[i].getValorDaMaoDoJogador();
            }
        }

        for (int i = 0; i < jogador.length; i++) { // rodo for para ver se a pessoa teve o maior valor da mesa
            if (jogador[i].getValorDaMaoDoJogador() == maior) {
                ganhadores.add(jogador[i].getNome()); // se teve ela entra no arraylist ganhadores
            }
        }

        if (ganhadores.size() == 1) { // se houver somente um ganhador
            System.out.print(COR_VERDE + ganhadores.get(0) + " venceu!" + RESETAR_COR);
            
        } else { // se emparam
            for (int i = 0; i < ganhadores.size(); i++) {
                if (i == ganhadores.size() - 1) {
                    System.out.print(ganhadores.get(i));
                } else {
                    System.out.print(COR_AMARELA + ganhadores.get(i) + " e ");
                }

            }
            System.out.println(" empataram!" + RESETAR_COR);
        }
        System.out.println("Fim do jogo.");
        System.exit(0);
    }

    public static void rodarMaoInicial(ArrayList<Integer> maoInicial) {
        int carta = 0;
        for (int i = 0; i < 2; i++) {
            do {
                carta = ROLL.nextInt(13) + 1;
            } while (baralho[carta - 1] == 0);
            maoInicial.add(carta);
            baralho[carta - 1]--;
        }
    }

    public static void comprarCarta(Jogadores[] jogador, int i) {
        int carta = ROLL.nextInt(13) + 1;
        if (baralho[carta - 1] > 0) {
            jogador[i].addcarta(carta);
            baralho[carta - 1]--;
        } else {
            comprarCarta(jogador, i);
        }
    }

    // #region DEFININDO QUANTIDADE DE JOGADORES
    public static int quantidadeJogadores() throws InterruptedException {
        int quantidade = 0;
        System.out.println("-------------------------------");
        System.out.print("Quantos jogadores vão jogar: ");
        do {
            quantidade = SC.nextInt();
            if (quantidade < 2) {
                Thread.sleep(500);
                System.out.println(COR_VERMELHA + "-------------------------------");
                System.out.println("Mínimo de 2 jogadores");
                System.out.println("-------------------------------" + RESETAR_COR);
                Thread.sleep(700);
                System.out.print("Quantos jogadores vão jogar: ");
            } else if (quantidade > 8) {
                Thread.sleep(500);
                System.out.println(COR_VERMELHA + "-------------------------------");
                System.out.println("Máximo de 8 jogadores");
                System.out.println("-------------------------------" + RESETAR_COR);
                Thread.sleep(700);
                System.out.print("Quantos jogadores vão jogar: ");
            }
        } while (quantidade < 2 || quantidade > 8);
        Thread.sleep(250);
        return quantidade;
    }
    // #endregion

    public static int[] preencherBaralho(int n) {
        if (n > 4) { // verifico se a qtd de jogadores é maior que 4
            n = 8; // se for se torna 2 baralhos, apenas reutilizei a variavel
        } else {
            n = 4; // se não for é um baralho só
        }
        for (int i = 0; i < baralho.length; i++) {
            baralho[i] = n;
        }
        return baralho;
    }
}
