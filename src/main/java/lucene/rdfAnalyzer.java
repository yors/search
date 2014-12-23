package lucene;

import java.io.Reader;
import org.apache.lucene.analysis.Analyzer;

public class rdfAnalyzer extends Analyzer {

	@Override
	protected TokenStreamComponents createComponents(String fieldName,
			Reader reader) {
	
		return null;
	}

}
