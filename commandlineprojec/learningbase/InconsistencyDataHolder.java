package learningbase;

public class InconsistencyDataHolder {


	
	private final Row firstRow;

	
	private final Row secondRow;

	
	private final String differingSymbol;

	public InconsistencyDataHolder(Row firstRow,Row secondRow, String differingSymbol) {
		this.firstRow = firstRow;
		this.secondRow = secondRow;
		this.differingSymbol = differingSymbol;
	}

	
	public String getFirstState() {
		return firstRow.getLabel();
	}


	public String getSecondState() {
		return secondRow.getLabel();
	}


	public String getDifferingSymbol() {
		return differingSymbol;
	}


	public Row getFirstRow() {
		return secondRow;
	}


	public Row getSecondRow() {
		return firstRow;
	}


	public String getSymbol() {
		return differingSymbol;
	}

}
