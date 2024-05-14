import java.util.ArrayList;

public class Jogadores {
    private String nome;
    private ArrayList<Integer> maoDoJogador = new ArrayList<Integer>();
    

    public Jogadores(String nome, ArrayList<Integer> maoDoJogador) {
        this.setNome(nome);
        this.setMaoInicial(maoDoJogador);
    }
    public String getNome() {
        return this.nome;
    }
    public void setNome(String n) {
        this.nome = n;
    }

    public ArrayList<Integer> getMaoDoJogador() {
        return this.maoDoJogador;
    }
    public void setMaoInicial(ArrayList<Integer> maoInicial) {
        this.maoDoJogador = maoInicial;
    }

    public int getValorDaMaoDoJogador(){
        int soma = 0;
        
        for (int i = 0; i < this.maoDoJogador.size(); i++) {
            soma += this.maoDoJogador.get(i);
        }
        if(this.maoDoJogador.contains(1) && soma <= 11){ //se tem Ás e seu valor total é 11 ou menos, o Ás vale 11
            soma += 10; //como já foi setado o 1 caso tenha ás, aqui seto apenas 10 assim completando o 11
        }
        return soma;
    }

    public void addcarta(int n) {
        this.maoDoJogador.add(n);
    }

    public void perdeu(){
        this.maoDoJogador.clear();
    }

    public int getCartaComprada(){
        return this.maoDoJogador.get(this.maoDoJogador.size() -1);
    }
}
