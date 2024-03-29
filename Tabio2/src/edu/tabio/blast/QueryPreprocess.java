package edu.tabio.blast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.tabio.Configuration.SubstitutionMatrix;

public class QueryPreprocess {
	


	private SubstitutionMatrix sbm;
	
	public QueryPreprocess(SubstitutionMatrix subsMat) {
		this.sbm = subsMat;
	}
	
	protected Map<String,SimilarsAndIndexes> similarWordsMap = new HashMap<>();

	public void preprocess(String query, TextPreprocess textPreprocess)
	{
		Set<String> textKBlocks = textPreprocess.getKBlocksList();
		for (int i = 0; i < query.length()-BlastConstants.K + 1 ; i++) {
			String queryBlock = query.substring(i, i + BlastConstants.K);
			SimilarsAndIndexes queryWord = similarWordsMap.get(queryBlock);
			if(queryWord == null){
				queryWord = new SimilarsAndIndexes();
				List<SimilarString> wordList = queryWord.getSimilarStrings();
				for (String textBlock : textKBlocks) {
					Integer similarity =  sbm.similarity(queryBlock, textBlock, BlastConstants.T);
					if (similarity != null){
						wordList.add(new SimilarString(textBlock, similarity, textPreprocess.textMap.get(textBlock))); //add to the similar words list
					}			
				}
				similarWordsMap.put(queryBlock , queryWord);
			}
			
			queryWord.addQueryIndexes(i);
			
			
		}
		if (BlastConstants.DEBUG)	printMap();
	}
	
	
	private void printMap(){
		for (String word : similarWordsMap.keySet()) {
			SimilarsAndIndexes similarWordsList = similarWordsMap.get(word);
			System.out.println("Word in query: " + word + ", " + similarWordsList.toString());
			
			
		}
		
		
		
	}
	
	
	
	
}

	


