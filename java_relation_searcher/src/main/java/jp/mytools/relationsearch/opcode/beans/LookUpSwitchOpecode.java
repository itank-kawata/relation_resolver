package jp.mytools.relationsearch.opcode.beans;

import java.util.Map;

public class LookUpSwitchOpecode extends GeneralOpecode {
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
