import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Processo {
	
	private List<String> comandos;
	
	Processo(String arquivo) 
	{	
		this.comandos = new ArrayList<String>();
		try {
			carregarComandos(arquivo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void carregarComandos(String arquivo) throws IOException {
		try {
			File programa = new File(arquivo);
			BufferedReader leitor = new BufferedReader(new FileReader(programa));
			String comando;
			
			while((comando = leitor.readLine()) != null)
			
				comandos.add(comando);
			
			leitor.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public String executar(int PC) {
		System.out.println(comandos.get(PC));
		return comandos.get(PC);
	}
}
