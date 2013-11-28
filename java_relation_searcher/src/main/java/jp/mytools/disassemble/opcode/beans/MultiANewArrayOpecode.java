package jp.mytools.disassemble.opcode.beans;

public class MultiANewArrayOpecode extends ReferenceOpecode {
	private int dimension;

	public int getDimension() {
		return dimension;
	}

	public void setDimension(int dimension) {
		this.dimension = dimension;
	}
}
