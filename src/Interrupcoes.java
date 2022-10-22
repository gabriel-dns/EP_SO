public class Interrupcoes {

    String nomeProcesso;
    int qtdInstrucoes;
    int execucoes;
    float media;

    Interrupcoes(String nomeProcesso, int qtdInstrucoes){
        this.nomeProcesso = nomeProcesso;
        this.qtdInstrucoes = qtdInstrucoes;
        this.execucoes = 1;
        this.media= 0 ;
    }
    public void atualizaValores(int qtdInstrucoes){
        this.qtdInstrucoes+=qtdInstrucoes;
        this.execucoes++;
        this.media = ((float)this.qtdInstrucoes /(float) this.execucoes);
    }

    
}
