package jp.mytools.disassemble.opcode.beans;

public class MultiANewArrayOpecode extends ReferenceOpecode {

	private static final long serialVersionUID = 2887536356899890285L;
	private int dimension;

	public int getDimension() {
		return dimension;
	}

	public void setDimension(int dimension) {
		this.dimension = dimension;
	}
}
