import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

public class Main {

	public static void main(String[] args) {
		Escalonador escalonador;
		PrintStream out;
		int quantum = 0;
		System.out.println("COMEÇANDO ");
		try {
			
			File quantumFile = new File(System.getProperty("user.dir") + File.separator + "quantum.txt");
			BufferedReader leitor = new BufferedReader(new FileReader(quantumFile));
			quantum = Integer.parseInt(leitor.readLine());
			escalonador = new Escalonador(quantum);
			
			out = new PrintStream(new FileOutputStream("log"+ String.format("%02d", quantum) +".txt"));
			System.setOut(out);
			
			File listaDePrioridades = new File(System.getProperty("user.dir") + File.separator + "prioridades.txt");
			leitor = new BufferedReader(new FileReader(listaDePrioridades));
			String prioridade;
			int linha = 1;
			
			while((prioridade = leitor.readLine()) != null) { 
				escalonador.novoProcesso(new BCP(linha, Integer.parseInt(prioridade)));
				linha++;
			}
			
			leitor.close();
			
			escalonador.executar();
			escalonador.imprimirMedias();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("QUANTUM: "+ quantum);
	}

}
