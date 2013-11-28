package jp.mytools.disassemble.constantpool.beans;

import java.io.UnsupportedEncodingException;

public class LongDoubleConstantPool extends AbstractConstantPool {

	private static final int STRUCTURE_LENGTH = 9;

	protected LongDoubleConstantPool(int tag) {
		super(tag);
	}

	private byte[] highBytes;
	private byte[] lowBytes;

	public byte[] getHighBytes() {
		return highBytes;
	}
	public void setHighBytes(byte[] highBytes) {
		this.highBytes = highBytes;
	}
	public byte[] getLowBytes() {
		return lowBytes;
	}
	public void setLowBytes(byte[] lowBytes) {
		this.lowBytes = lowBytes;
	}
	
	public String getHigh() {
		try {
			return new String(highBytes,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	public String getLow() {
		try {
			return new String(lowBytes,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	@Override
	public int getStructureLength() {
		return STRUCTURE_LENGTH;
	}

	@Override
	public String toString() {
		return super.toString() + " , high = " + getHigh() + " , low = " + getLow();
	}
}
