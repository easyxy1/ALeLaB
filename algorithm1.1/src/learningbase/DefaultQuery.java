package learningbase;

public class DefaultQuery {
	private String output;
    private String prefix, suffix;
    public DefaultQuery(String prefix, String suffix) {
        this.prefix=prefix;
        this.suffix=suffix;
    }
    
    public DefaultQuery(String prefix, String suffix, String output) {
    	this.prefix=prefix;
        this.suffix=suffix;
    	this.output = output;
    }
  
    public String getOutput() {
        return output;
    }
    public String getPrefix() {
        return prefix;
    }
    public String getSuffix() {
        return suffix;
    }
    /**
     * Checks if the query is normalized, i.e., if all the information is stored
     * in the {@link #getSuffix() suffix} part of the counterexample.
     * @return {@code true} if the counterexample is normalized, {@code false}
     * otherwise.
     */
    public boolean isNormalized() {
    	return prefix.isEmpty();
    }

    public void answer(String output) {
        this.output = output;
    }
    
    public boolean notAnswered(){
    	return !output.isEmpty();
    }
}
