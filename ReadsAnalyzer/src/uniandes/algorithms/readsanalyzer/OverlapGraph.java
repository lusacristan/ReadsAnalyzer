package uniandes.algorithms.readsanalyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import htsjdk.samtools.util.RuntimeEOFException;
import ngsep.math.Distribution;
import ngsep.sequences.RawRead;


/**
 * Represents an overlap graph for a set of reads taken from a sequence to assemble
 * @author Jorge Duitama
 *
 */
public class OverlapGraph implements RawReadProcessor {

	private int minOverlap;
	private Map<String,Integer> readCounts = new HashMap<>();
	private Map<String,ArrayList<ReadOverlap>> overlaps = new HashMap<>();

	/**
	 * Creates a new overlap graph with the given minimum overlap
	 * @param minOverlap Minimum overlap 
	 */
	public OverlapGraph(int minOverlap) {
		this.minOverlap = minOverlap;
	}

	/**
	 * Adds a new read to the overlap graph
	 * @param read object with the new read
	 */
	public void processRead(RawRead read) {
		String sequence = read.getSequenceString();
		//TODO: Paso 1. Agregar la secuencia al mapa de conteos si no existe.

		if (!readCounts.containsKey(sequence)){

			readCounts.put(sequence, 1);
		}

		//Si ya existe, solo se le suma 1 a su conteo correspondiente y no se deben ejecutar los pasos 2 y 3 
		else{

			int valorActual = readCounts.get(sequence);
			readCounts.put(sequence,valorActual+1);
			return;
		}


		//TODO: Paso 2. Actualizar el mapa de sobrelapes con los sobrelapes en los que la secuencia nueva sea predecesora de una secuencia existente
		//2.1 Crear un ArrayList para guardar las secuencias que tengan como prefijo un sufijo de la nueva secuencia

		ArrayList<ReadOverlap> secuenciasPrefijo = new ArrayList<ReadOverlap>();

		//2.2 Recorrer las secuencias existentes para llenar este ArrayList creando los nuevos sobrelapes que se encuentren.


		//2.3 Después del recorrido para llenar la lista, agregar la nueva secuencia con su lista de sucesores al mapa de sobrelapes 

		//TODO: Paso 3. Actualizar el mapa de sobrelapes con los sobrelapes en los que la secuencia nueva sea sucesora de una secuencia existente
		// Recorrer el mapa de sobrelapes. Para cada secuencia existente que tenga como sufijo un prefijo de la nueva secuencia
		//se agrega un nuevo sobrelape a la lista de sobrelapes de la secuencia existente

		for (String sequence2:readCounts.keySet()) {

			int length1 = getOverlapLength(sequence, sequence2);

			int length2 = getOverlapLength(sequence2, sequence);

			if (length1 > 0) {
				ReadOverlap overlap= new ReadOverlap(sequence,sequence2,length1);
				secuenciasPrefijo.add(overlap);

			}	
			if (overlaps.containsKey(sequence2)) {
				if (length2 > 0) {
				ReadOverlap overlap2= new ReadOverlap(sequence2,sequence,length2);
				ArrayList<ReadOverlap> secuenciasPrefijo2 = overlaps.get(sequence2);
				secuenciasPrefijo2.add(overlap2);
			}
			}
		}
		overlaps.put(sequence, secuenciasPrefijo);

	}
	/**
	 * Returns the length of the maximum overlap between a suffix of sequence 1 and a prefix of sequence 2
	 * @param sequence1 Sequence to evaluate suffixes
	 * @param sequence2 Sequence to evaluate prefixes
	 * @return int Maximum overlap between a prefix of sequence2 and a suffix of sequence 1
	 */
	public int getOverlapLength(String sequence1, String sequence2) {
		int maximunOverlap = 0;
		
		
		
		for (int i = minOverlap; i< sequence1.length()+1; i++) {
			
			String suffixes = sequence1.substring(sequence1.length() - i, sequence1.length());
			
			String prefixes = sequence2.substring(0,i);
			
			if(suffixes.equals(prefixes)) {
				
				maximunOverlap = i;
			}
		}
			
		return maximunOverlap;
	}



	/**
	 * Returns a set of the sequences that have been added to this graph 
	 * @return Set<String> of the different sequences
	 */
	public Set<String> getDistinctSequences() {
		//TODO: Implementar metodo

		Set<String> listaSecuencias = overlaps.keySet();
		
		return listaSecuencias;
	}

	/**
	 * Calculates the abundance of the given sequence
	 * @param sequence to search
	 * @return int Times that the given sequence has been added to this graph
	 */
	public int getSequenceAbundance(String sequence) {
		//TODO: Implementar metodo
		int abudance = 0;
		if (readCounts.containsKey(sequence)) {
			abudance = readCounts.get(sequence);
		}
		
		return abudance;
	}

	/**
	 * Calculates the distribution of abundances
	 * @return int [] array where the indexes are abundances and the values are the number of sequences
	 * observed as many times as the corresponding array index. Position zero should be equal to zero
	 */
	public int[] calculateAbundancesDistribution() {
		//TODO: Implementar metodo
		int[] disAbundancia = new int[400];
		disAbundancia[0] = 0;
		
		for (Map.Entry<String, Integer> entry : readCounts.entrySet() ) {
			
			int cantidad = entry.getValue();
			
			if (cantidad < 399) {
				
				disAbundancia[cantidad]++;
			}
			
			else {
				disAbundancia[399]++;
			}
		}
		return disAbundancia;
	}
	/**
	 * Calculates the distribution of number of successors
	 * @return int [] array where the indexes are number of successors and the values are the number of 
	 * sequences having as many successors as the corresponding array index.
	 */
	public int[] calculateOverlapDistribution() {
		// TODO: Implementar metodo
		int[] disOverlap = new int[400];
		
		for (Map.Entry<String, ArrayList<ReadOverlap>> entry : overlaps.entrySet() ) {
			
			ArrayList<ReadOverlap> successors = entry.getValue();
			int cantidadSuccessors= successors.size();
			
			if (cantidadSuccessors < 399) {
				
				disOverlap[cantidadSuccessors]++;
			}
			
			else {
				disOverlap[399]++;
			}
		}
		return disOverlap;
	}
	/**
	 * Predicts the leftmost sequence of the final assembly for this overlap graph
	 * @return String Source sequence for the layout path that will be the left most subsequence in the assembly
	 */
	public String getSourceSequence () {
		// TODO Implementar metodo recorriendo las secuencias existentes y buscando una secuencia que no tenga predecesores
		
				String sourceSequence = "";
		
		for(String i:getDistinctSequences()) {
			
			Boolean existeSecuencia = false;
			
			for(String j: getDistinctSequences()) {
				
				if(!j.equals(i) && existeSecuencia == false) {
					
					ArrayList<ReadOverlap> value = overlaps.get(j);
					
					for(ReadOverlap valor: value ) {
						
						if(valor.getDestSequence().equals(i)) {
							
							existeSecuencia = true;
							
							break;
						}

					}
				}

			}
			if(existeSecuencia == false) {
				
				sourceSequence = i;
				break;
			}
		}
		
		return sourceSequence;
	}

	/**
	 * Calculates a layout path for this overlap graph
	 * @return ArrayList<ReadOverlap> List of adjacent overlaps. The destination sequence of the overlap in 
	 * position i must be the source sequence of the overlap in position i+1. 
	 */
	public ArrayList<ReadOverlap> getLayoutPath() {
		ArrayList<ReadOverlap> layout = new ArrayList<>();
		HashSet<String> visitedSequences = new HashSet<>(); 
		// TODO Implementar metodo. Comenzar por la secuencia fuente que calcula el método anterior
		
		String sourceSequence = getSourceSequence();
		
		visitedSequences.contains(sourceSequence);
		
		String secuenciaActual = "";
		
		ReadOverlap actualOverleap  = new ReadOverlap("", sourceSequence , 0);
		
		ReadOverlap siguiente  = new ReadOverlap("", "", 0);
		// Luego, hacer un ciclo en el que en cada paso se busca la secuencia no visitada que tenga mayor sobrelape con la secuencia actual.
		
		while(!layout.contains(actualOverleap)) {
			
			layout.add(actualOverleap);
			
			secuenciaActual = actualOverleap.getDestSequence();
			
			ArrayList<ReadOverlap> values = overlaps.get(secuenciaActual);
			
			int mayorOverlap = 0;
			
		// Agregar el sobrelape a la lista de respuesta y la secuencia destino al conjunto de secuencias visitadas. Parar cuando no se encuentre una secuencia nueva
			for(ReadOverlap v: values ) {
				
				String secuenciaDestino = v.getDestSequence();
				
				if(v.getOverlap() >= mayorOverlap && !visitedSequences.contains(secuenciaDestino)) {
					
					mayorOverlap = v.getOverlap();
					visitedSequences.add(secuenciaDestino);
					siguiente = v;
				}
			}
			actualOverleap = siguiente;
			
			
			
		}

		return layout;
	}
	/**
	 * Predicts an assembly consistent with this overlap graph
	 * @return String assembly explaining the reads and the overlaps in this graph
	 */
	public String getAssembly () {
		ArrayList<ReadOverlap> layout = getLayoutPath();
		StringBuilder assembly = new StringBuilder();
		// TODO Recorrer el layout y ensamblar la secuencia agregando al objeto assembly las bases adicionales que aporta la región de cada secuencia destino que está a la derecha del sobrelape 

				for (int i = 0; i < layout.size(); i++) {
			
			ReadOverlap overlap = layout.get(i);
			
			String secuenciaEnsamblar = overlap.getDestSequence();
			
			if(assembly.length()== 0) {
				
				System.out.println("entro");
				assembly.append(secuenciaEnsamblar);
				
			}
			else {
				
				String secuenciaNueva = secuenciaEnsamblar.substring(overlap.getOverlap(), secuenciaEnsamblar.length());
				assembly.append(secuenciaNueva);
				
			}
			
		}
		return assembly.toString();
	}


}
