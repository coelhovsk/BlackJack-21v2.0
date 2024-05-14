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
            System.out.print("Nome do jogador " + (i +1) + ": ");
            jogadores[i].setNome(SC.nextLine()); // envia o nome digitado
            comprarCarta(jogadores, i); // preenche a mão inicial com 2 cartas
            comprarCarta(jogadores, i);
        }
        // //#endregion 

        // #region loop infinito do jogo, compra de cartas até finalizar
        while (true) { 
            for (int i = 0; i < jogadores.length; i++) { // roda o vetor para que todos jogadores possam jogar
                if (jogadores[i].getMaoDoJogador().isEmpty()) { // verifica se o jogador estiver sem cartas(ou seja,
                                                                // eliminado)
                    System.out.println(jogadores[i].getNome() + " está fora do jogo (sem cartas).");
                    continue; // passa para o próximo jogador
                }

                String compraString = null; // declarando variavel para usar no do{
                boolean compra = false; // declarando variavel para usar no do{
                int gambiarraDoDiabo = 1; // gambiarra pro loop do do{ funcionar de modo simples

                // #region PERGUNTA DESEJA COMPRAR
                do { // criado sem um else pois isso gastaria mais "memória"
                    System.out.println("-------------------------------");
                    System.out.println(jogadores[i].getNome() + ", suas cartas atuais são: " + jogadores[i].getMaoDoJogador()); // mostra cartas atuais
                    System.out.println(jogadores[i].getNome() + ", deseja comprar?");
                    System.out.println("-------------------------------");
                    compraString = SC.next().toUpperCase();
                    if ((compraString.charAt(0) == 'S' || compraString.charAt(0) == 'Y')
                            && compraString.length() <= 3) {
                        compra = true;// caso a palavra se assemelhe com sim ou yes
                        gambiarraDoDiabo = 1;
                    } else if ((compraString.charAt(0) != 'N' && compraString.length() <= 3)
                            || compraString.length() > 3) {
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
                     
                    if(jogadores[i].getValorDaMaoDoJogador() == 21){ // verifica se ganhou
                        if(jogadores[i].getMaoDoJogador().size() == 2){ // Verifica se o jogador fez BJ a partir do metodo verificarBlackJack
                            System.out.println(jogadores[i].getNome() + ", suas cartas iniciais são: " + jogadores[i].getMaoDoJogador());
                            System.out.println(COR_VERDE + jogadores[i].getNome() + " venceu com um Blackjack!" + RESETAR_COR);
                        } else { // caso não tenha feito printa uma mensagem de venceu normal.
                            System.out.println(COR_VERDE + jogadores[i].getNome() + " venceu!" + RESETAR_COR);
                        }
                        System.out.println("Fim do jogo.");
                        System.exit(0);
                    }else if(jogadores[i].getValorDaMaoDoJogador() > 21){ // verifica se a mão estorou
                        jogadores[i].perdeu();
                        System.out.println(COR_VERMELHA + jogadores[i].getNome() + "estourou!" + RESETAR_COR);
                        jogadoresDesativados++;
                    }
                 } else {
                     qtdJogadoresPassaram++;
                     if(qtdJogadoresPassaram == (quantidadeJogadores-jogadoresDesativados)){
                         finalizarJogo();
                    }
                }
            }
        }
    }

    public static void finalizarJogo(){
        // a fazer
    }
    public static void comprarCarta(Jogadores[] jogador, int i){
        int carta = ROLL.nextInt(13) + 1;
        if (baralho[carta] > 0) {
            jogador[i].addcarta(carta);;
            baralho[carta]--;
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

    public static int[] preencherBaralho(int n){
        if(n > 4){ //verifico se a qtd de jogadores é maior que 4
            n = 8; // se for se torna 2 baralhos, apenas reutilizei a variavel
        }else{
            n = 4; // se não for é um baralho só
        }
        for (int i = 0; i < baralho.length; i++) {
            baralho[i] = n;
        }
        return baralho;
    }
}
