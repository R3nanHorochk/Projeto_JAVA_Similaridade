package rha;

public class Resultado {
	private String nomeFile1;
	private String nomeFile2;
	private double similaridade;
	public String getNomeFile1() {
		return nomeFile1;
	}
	public void setNomeFile1(String nomeFile1) {
		this.nomeFile1 = nomeFile1;
	}
	public String getNomeFile2() {
		return nomeFile2;
	}
	public void setNomeFile2(String nomeFile2) {
		this.nomeFile2 = nomeFile2;
	}
	public double getSimilaridade() {
		return similaridade;
	}
	public void setSimilaridade(double similaridade) {
		this.similaridade = similaridade;
	}
	public Resultado(String nomeFile1, String nomeFile2, double similaridade) {
		this.nomeFile1 = nomeFile1;
		this.nomeFile2 = nomeFile2;
		this.similaridade = similaridade;
	}
	
	
}
