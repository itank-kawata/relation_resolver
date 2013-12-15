package jp.mytools.disassemble.constantpool.beans;

import java.nio.ByteBuffer;


public class IntegerFloatConstantPool extends AbstractConstantPool {

	private static final long serialVersionUID = 4179559295956750830L;
	private static final int STRUCTURE_LENGTH = 9;

	protected IntegerFloatConstantPool(int tag) {
		super(tag);
	}

	private byte[] bytes;
	public byte[] getBytes() {
		return bytes;
	}
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	
	public int getInt() {
		return ByteBuffer.wrap(bytes).getInt();
	}
	
	public float getFloat() {
		return ByteBuffer.wrap(bytes).getFloat();
	}
	@Override
	public int getStructureLength() {
		return STRUCTURE_LENGTH;
	}

	@Override
	public String toString() {
		return super.toString() + " , " + "int value = " + getInt() + " , float value = " + getFloat();
	}
}
