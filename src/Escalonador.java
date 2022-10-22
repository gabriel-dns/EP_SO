import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unused")
public class Escalonador {
	
	private List<BCP> filaProntos;
	private List<BCP> filaBloqueados;
	private List<Interrupcoes> mediaInterrupcoes;
	private int quantum;
	private float mediaQuantum;
	private int interrupcoes;
	
	Escalonador(int quantum) {
		this.filaProntos = new ArrayList<BCP>();
		this.filaBloqueados = new LinkedList<BCP>();
		this.mediaInterrupcoes = new ArrayList<Interrupcoes>();
		this.quantum = quantum;
		this.mediaQuantum = 0;
		this.interrupcoes = 0;
	}
	
	public void novoProcesso(BCP bcp) {
		filaProntos.add(bcp);
	//	System.out.println("Carregando "+ bcp.getNome());
	}
	
	public void executar() 
	{
		while (!filaProntos.isEmpty() || !filaBloqueados.isEmpty()) 
		{
			diminuiTempoFilaBloqueio();
			verificaFilaBloqueados();

			if (filaProntos.isEmpty())
				continue;
			
			verificaCreditosFilaProntos();
			
			ordenaFilaProntos();
			
			BCP maiorPrioridade = filaProntos.get(0);
			System.out.println("Executando "+ maiorPrioridade.getNome());
			
			for (int i = 1; i <= quantum; i++) 
			{	
				if (!maiorPrioridade.temCreditos()) {
					imprimeMensagemInterrupcao(maiorPrioridade.getNome(), i-1,quantum);
					break;
				}
				
				String comando = maiorPrioridade.executar();
				
				if (comando.equals("SAIDA")) {
					filaProntos.remove(0);
					System.out.println(maiorPrioridade.getNome() +" terminado. "+ maiorPrioridade.imprimeVariaveis());
					break;
				}
				else if (comando.equals("E/S")) 
				{
					maiorPrioridade.bloquear(3);
					filaProntos.remove(0);
					filaBloqueados.add(maiorPrioridade);
					System.out.println("E/S iniciada em "+ maiorPrioridade.getNome());
					imprimeMensagemInterrupcao(maiorPrioridade.getNome(), i,quantum);
					break;
				}
				else if (comando.substring(1, 2).equals("="))
				{
					if (comando.substring(0, 1).equals("X"))
						maiorPrioridade.setX(Integer.parseInt(comando.substring(2)));
					else
						maiorPrioridade.setY(Integer.parseInt(comando.substring(2)));
				}				
				if (i == quantum && maiorPrioridade.estaExecutando()) {
					imprimeMensagemInterrupcao(maiorPrioridade.getNome(), quantum,quantum);
					maiorPrioridade.interromper();
				}
			}				
		}
		

	}

	
	private void imprimeMensagemInterrupcao(String nomeProcesso, int qtdInstrucoes, int quantum) {
		System.out.println("Interrompendo "+ nomeProcesso +" apos "+ qtdInstrucoes + ((qtdInstrucoes) > 1 ? " instru��es" : " instru��o"));
		System.out.println("Media de Instrucoes executadas por quantum "+(float)qtdInstrucoes/ quantum);
		mediaQuantum += (float) qtdInstrucoes/ quantum;
		interrupcoes++;
		calculaMediaInterrupcoes(nomeProcesso, qtdInstrucoes);
	}
	private void calculaMediaInterrupcoes(String nomeProcesso, int qtdInstrucoes){
	//	System.out.println("INICIO CALCULA-MEDIA");
		boolean inserirNovo =true;
		if(mediaInterrupcoes.isEmpty() == false){

		for(Interrupcoes media : mediaInterrupcoes){
			if(media.nomeProcesso.equals(nomeProcesso) ){
			//	System.out.println("\n \n--> PROCESSO REPETIDO");
			//	System.out.println("TESTE equals: "+media.nomeProcesso.equals(nomeProcesso));
			//	 System.out.println("NOME OBJETO:"+media.nomeProcesso);
			//	 System.out.println("NOME TESTADO:"+nomeProcesso);
				 media.atualizaValores(qtdInstrucoes);
				 inserirNovo = false;
				 break;	
			}
			
		}
		if(inserirNovo == true){
		//	System.out.println("--> PROCESSO NOVO");
			
		Interrupcoes in = new Interrupcoes(nomeProcesso,qtdInstrucoes);
		
		//System.out.println("nome ARQUIVO "+in.nomeProcesso);
	
		mediaInterrupcoes.add(in);
		//System.out.println("--> INSERIDO \n \n");

			
		} else inserirNovo = true;


	}else{
			//System.out.println("--> INSERINDO PRIMEIRA MEDIA");
			Interrupcoes in = new Interrupcoes(nomeProcesso,qtdInstrucoes);
			
			//System.out.println("nome ARQUIVO "+in.nomeProcesso);
			
			mediaInterrupcoes.add(in);
			//System.out.println("--> INSERIDO  <--- \n");
		} 
	}


	public void imprimirMedias(){
		System.out.println("--> IMPRESSAO MEDIAS <--");
		float mediaGeral = 0;
		for(Interrupcoes in : mediaInterrupcoes){
			System.out.println("Media de interrupcoes do "+ in.nomeProcesso +
			" e de:   "+in.media );
			mediaGeral += in.media;
		}
		mediaQuantum = mediaQuantum/interrupcoes;
		System.out.println("Media (geral) de interrupcoes de processos: "+mediaGeral/mediaInterrupcoes.size() );
		System.out.println("Numero medio (geral) de instrucoes executadas por quantum: "+mediaQuantum);
	}
	
	private void diminuiTempoFilaBloqueio() {
		filaBloqueados.forEach((b) -> b.diminuiTempoBloqueio());
	}
	
	private void verificaFilaBloqueados() {
		if (!filaBloqueados.isEmpty() && filaBloqueados.get(0).terminouTempoBloqueio())
			filaProntos.add(filaBloqueados.remove(0));
	}
	
	private void verificaCreditosFilaProntos() {
		if(filaProntos.stream().filter((b) -> b.temCreditos()).count() == 0) 
			filaProntos.forEach((b) -> b.redistribuirCreditos());
		
	}
	
	private void ordenaFilaProntos() {
		filaProntos.sort(new Comparator<BCP>() {
			@Override
			public int compare(BCP o1, BCP o2) {
				if (o1.getCreditos() > o2.getCreditos())
					return -1;
				else if (o1.getCreditos() < o2.getCreditos())
					return 1;
				else 
					return 0;
			}
		});
	}
}
