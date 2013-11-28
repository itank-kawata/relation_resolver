package jp.mytools.disassemble.opcode.beans;

import jp.mytools.disassemble.opcode.enums.OpcodeType;

public class GeneralOpecode implements Opcode {

	private OpcodeType opcodeType;
	
	@Override
	public OpcodeType getOpcodeType() {
		return opcodeType;
	}
	
	public void setOpcodeType(OpcodeType opcodeType) {
		this.opcodeType = opcodeType;
	}

}
