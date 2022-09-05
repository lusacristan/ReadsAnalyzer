package uniandes.algorithms.readsanalyzer;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import ngsep.sequences.RawRead;
/**
 * Stores abundances information on a list of subsequences of a fixed length k (k-mers)
 * @author Jorge Duitama
 */
public class KmersTable implements RawReadProcessor {
	
	Hashtable<String, Integer> tablaKmer;
	int tamanoKmer;
	/**
	 * Creates a new table with the given k-mer size
	 * @param kmerSize length of k-mers stored in this table
	 */
	public KmersTable(int kmerSize) {
		
		// TODO: Implementar metodo
		tablaKmer = new Hashtable<>();
		
		tamanoKmer = kmerSize;
		
		
	}

	/**
	 * Identifies k-mers in the given read
	 * @param read object to extract new k-mers
	 */
	public void processRead(RawRead read) {
		String sequence = read.getSequenceString();
		
		// TODO Implementar metodo. Calcular todos los k-mers del tamanho dado en la constructora y actualizar la abundancia de cada k-mer
		//a la secuencia del read le sacamos el kmer si no existe el kmer en la tabla añadirño .. si existe actualizr abundancia 
		for (int i = 0; i< sequence.length() - tamanoKmer + 1 ; i++) {
			
			String kmer = sequence.substring(i, tamanoKmer + i);
			
			if (tablaKmer.containsKey(kmer))
			{
				tablaKmer.replace(kmer,tablaKmer.get(kmer) +1);
			}
			
			else {
				tablaKmer.put(kmer,1);
			}
			
		}
	}
	
	/**
	 * List with the different k-mers found up to this point
	 * @return Set<String> set of k-mers
	 */
	public Set<String> getDistinctKmers() {
		// TODO Implementar metodo 
		
		Set<String> listaKmers = new HashSet<>();
		
		for (String kmers: tablaKmer.keySet()) {
			
			listaKmers.add(kmers);
		
		}
		
		return listaKmers;
	}
	
	/**
	 * Calculates the current abundance of the given k-mer 
	 * @param kmer sequence of length k
	 * @return int times that the given k-mer have been extracted from given reads
	 */
	public int getAbundance(String kmer) {
		// TODO Implementar metodo 
		return 0;
	}
	
	/**
	 * Calculates the distribution of abundances
	 * @return int [] array where the indexes are abundances and the values are the number of k-mers
	 * observed as many times as the corresponding array index. Position zero should be equal to zero
	 */
	public int[] calculateAbundancesDistribution() {
		// TODO Implementar metodo
		return null;
	}
}
