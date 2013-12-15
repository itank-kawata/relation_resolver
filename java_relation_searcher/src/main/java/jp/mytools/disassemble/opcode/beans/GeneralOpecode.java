package jp.mytools.disassemble.opcode.beans;

import java.io.Serializable;

import jp.mytools.disassemble.opcode.enums.OpcodeType;

public class GeneralOpecode implements Serializable, Opcode {


	private static final long serialVersionUID = -6941495800679705400L;

	private OpcodeType opcodeType;
	
	@Override
	public OpcodeType getOpcodeType() {
		return opcodeType;
	}
	
	public void setOpcodeType(OpcodeType opcodeType) {
		this.opcodeType = opcodeType;
	}

}
