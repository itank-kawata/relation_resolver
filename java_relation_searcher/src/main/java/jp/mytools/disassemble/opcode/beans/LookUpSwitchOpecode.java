package jp.mytools.disassemble.opcode.beans;

import java.util.Map;

public class LookUpSwitchOpecode extends GeneralOpecode {

	private static final long serialVersionUID = 5512022726889353867L;
	private int defaultOffset;
	private int pairCount;
	private Map<Integer, Integer> offsetMap;
	public int getDefaultOffset() {
		return defaultOffset;
	}
	public void setDefaultOffset(int defaultOffset) {
		this.defaultOffset = defaultOffset;
	}
	public int getPairCount() {
		return pairCount;
	}
	public void setPairCount(int pairCount) {
		this.pairCount = pairCount;
	}
	public Map<Integer, Integer> getOffsetMap() {
		return offsetMap;
	}
	public void setOffsetMap(Map<Integer, Integer> offsetMap) {
		this.offsetMap = offsetMap;
	}
	
}
