package jp.mytools.disassemble.opcode.beans;

public class BranchByteOpecode extends GeneralOpecode {
	private static final long serialVersionUID = -957713112433900539L;
	private int branch;

	public int getBranch() {
		return branch;
	}

	public void setBranch(int branch) {
		this.branch = branch;
	}
}
