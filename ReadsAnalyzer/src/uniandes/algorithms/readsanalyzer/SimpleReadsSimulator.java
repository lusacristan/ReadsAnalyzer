package uniandes.algorithms.readsanalyzer;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Random;

import ngsep.sequences.QualifiedSequence;
import ngsep.sequences.QualifiedSequenceList;
import ngsep.sequences.io.FastaSequencesHandler;

/**
 * Simple script that simulates error free reads from a text in fasta format
 * @author Jorge Duitama
 *
 */
public class SimpleReadsSimulator {
	/**
	 * Main class that executes the program
	 * @param args Array of arguments:
	 * args[0]: Source sequence in fasta format. If many sequences are present, it only takes the first sequence
	 * args[1]: Length of the reads to simulate
	 * args[2]: Number of reads to simulate
	 * args[3]: Path to the output file
	 * args[4]: Simulation error
	 * @throws Exception If the fasta file can not be loaded
	 */
	public static void main(String[] args) throws Exception {
		String filename = args[0];
		int readLength = Integer.parseInt(args[1]);
		int numReads = Integer.parseInt(args[2]);
		String outFile = args[3];
		double tasaError = Double.parseDouble(args[4]);
		FastaSequencesHandler handler = new FastaSequencesHandler();
		handler.setSequenceType(StringBuilder.class);
		QualifiedSequenceList sequences = handler.loadSequences(filename);
		if(sequences.size()==0) throw new Exception("No sequences found in file: "+filename);
		QualifiedSequence seq = sequences.get(0);
		String sequence = seq.getCharacters().toString();
		int seqLength = sequence.length();
		System.out.println("Length of the sequence to simulate reads: "+seqLength);
		double averageRD = ((double)numReads*readLength)/seqLength;
		System.out.println("Expected average RD: "+averageRD);
		char [] fixedQS = new char [readLength];
		Arrays.fill(fixedQS, '5');
		String fixedQSStr = new String(fixedQS);
		Random random = new Random();
		
		//Mejorar el script reads simulator incluyendo un parámetro nuevo que permita simular una tasa de error aleatoria. 
		//Simular datos con un tamaño de lectura de 50bp y
		//profundidad promedio de 20X. Visualizar y comparar la distribución de abundancia de k-mers en cada caso
		
		

		try (PrintStream out = new PrintStream(outFile)){
			// TODO: Generar lecturas aleatorias. Utilizar el objeto random para generar una posicion aleatoria de inicio
			// en la cadena sequence. Extraer la lectura de tamanho readLength e imprimirla en formato fastq.
			// Utilizar la cadena fixedQSStr para generar calidades fijas para el formato
			
			//TODO:Mejorar el script reads simulator incluyendo un parámetro nuevo que permita simular una tasa de error aleatoria. 
			//Simular datos con un tamaño de lectura de 50bp y
			//profundidad promedio de 20X. Visualizar y comparar la distribución de abundancia de k-mers en cada caso

			int i= 0;
			while (i < numReads) {
				
				int posicionInicio= random.nextInt(0,seqLength - readLength +1);
				
				String read = sequence.substring(posicionInicio,posicionInicio+readLength);
				
				char[] nucleotidos = {'A', 'G', 'C','T'};
				
				for (int e = 0; e <readLength; e++) {
					
					double errorAleatorio = Math.random();
					int aleatorio = random.nextInt(4);
					
					while (read.charAt(e)!=nucleotidos[aleatorio]){
						
						aleatorio = random.nextInt(4);
					}
					
					if (tasaError > errorAleatorio){
						
					}
					
					String inicioSec = read.substring(0, e);
					String finalSec = read.substring(e+1, readLength);
					
					read = inicioSec + nucleotidos[aleatorio] + finalSec;
					
					
				}
					
				
				String idReads ="read_"+i;
				
								
				out.println("@id"+ idReads);
				out.println(read);
				out.println("+");
				out.println(fixedQSStr);
				i++;

			}


		}
	
	}
}
