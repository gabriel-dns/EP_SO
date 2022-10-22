import java.io.*;

public class BCP {
	//Atributos de controle do BCP
	private int PID;
	private int programCounter;
	private int prioridade;
	private int X;
	private int Y;
	private Estado estado;
	private int regiaoDeMemoria;
	private int creditos;
	private int tempoBloqueio;
	private Processo processo;

	BCP(int PID, int prioridade){
		this.PID = PID;
		this.prioridade = prioridade;
		this.estado = Estado.PRONTO;
		this.programCounter = 0;
		this.regiaoDeMemoria = this.hashCode();
		this.creditos = prioridade;
		this.tempoBloqueio = 0;
		this.processo = new Processo(System.getProperty("user.dir") + File.separator + String.format("%02d", PID)+".txt");
	}

	@Override
	public String toString() {
		return "BCP [PID=" + PID + ", programCounter=" + programCounter + ", estado=" + this.estado
				+ ", prioridade=" + prioridade + ", X=" + X + ", Y=" + Y + ", Créditos: "+creditos+", regiaoDeMemoria=@" + regiaoDeMemoria + "]";
	}
	
	public String toStringSimplificado() {
		return "BCP [PID=" + PID + ", programCounter = " + programCounter + ", prioridade = " + prioridade +", Créditos: "+creditos+"]";
	}
	
	public String executar() {
		this.estado = Estado.EXECUTANDO;
		this.creditos--;
		return processo.executar(++this.programCounter);
	}
	
	public void desbloquear() {
		this.estado = Estado.PRONTO;
	}
	
	public void interromper() {
		this.estado = Estado.PRONTO;
	}
	
	public void diminuiTempoBloqueio() {
		this.tempoBloqueio--;
	}
	
	public void bloquear(int tempoBloqueio) {
		this.estado = Estado.BLOQUEADO;
		this.tempoBloqueio = tempoBloqueio;
	}
	
	public boolean temCreditos() {
		return this.creditos > 0;
	}
	
	public boolean terminouTempoBloqueio() {
		return tempoBloqueio == 0;
	}
	
	public int getCreditos() {
		return creditos;
	}
	
	public void redistribuirCreditos() {
		this.creditos = this.prioridade;
	}
	
	public void setX(int x) {
		this.X = x;
	}
	
	public void setY(int y) {
		this.Y = y;
	}
	
	public String imprimeVariaveis() {
		return "X="+ this.X +" Y="+this.Y; 
	}
	
	public String getNome() {
		return "TESTE-"+PID;
	}
	
	public boolean estaExecutando() {
		return this.estado == Estado.EXECUTANDO;
	}
}
