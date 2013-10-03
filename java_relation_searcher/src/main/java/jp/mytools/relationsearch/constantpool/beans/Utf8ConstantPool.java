package jp.mytools.relationsearch.constantpool.beans;

import java.io.UnsupportedEncodingException;

import jp.mytools.relationsearch.constantpool.enums.ConstantPoolType;


public class Utf8ConstantPool extends AbstractConstantPool {

	public Utf8ConstantPool() {
		super(ConstantPoolType.UTF8.getTag());
	}
	
	private int length;
	private byte[] bytes;
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public byte[] getBytes() {
		return bytes;
	}
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	
	public String getValue() {
		try {
			return new String(bytes,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	@Override
	public int getStructureLength() {
		// tag(u1) + length(u2) + bytes[length](u1 * length);
		return 1 + 2 + length;
	}
	
	@Override
	public String toString() {
		return super.toString() + " + length = " + length + " , bytes(String value) = " + getValue();
	}
}
