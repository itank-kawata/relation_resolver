package jp.mytools.disassemble.opcode.beans;

public class TableSwitchOpecode extends GeneralOpecode {
	private int defaultOffset;
	private int low;
	private int high;
	private int[] offsets;
	public int getDefaultOffset() {
		return defaultOffset;
	}
	public void setDefaultOffset(int defaultOffset) {
		this.defaultOffset = defaultOffset;
	}
	public int getLow() {
		return low;
	}
	public void setLow(int low) {
		this.low = low;
	}
	public int getHigh() {
		return high;
	}
	public void setHigh(int high) {
		this.high = high;
	}
	public int[] getOffsets() {
		return offsets;
	}
	public void setOffsets(int[] offsets) {
		this.offsets = offsets;
	}
	
}
