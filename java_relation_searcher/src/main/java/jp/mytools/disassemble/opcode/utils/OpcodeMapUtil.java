package jp.mytools.disassemble.opcode.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.mytools.disassemble.opcode.enums.OpcodeType;


public class OpcodeMapUtil {

	private static Logger logger = LoggerFactory.getLogger(OpcodeMapUtil.class);
	private static Map<Integer, OpcodeType> opcodeMap = new HashMap<Integer, OpcodeType>();

	private static Set<OpcodeType> hasWideFormatSet = new HashSet<>();
	private static Set<OpcodeType> hasWideConstFormatSet = new HashSet<>();;

	static {
		for (OpcodeType opcodeEnum : OpcodeType.values()) {
			opcodeMap.put(opcodeEnum.getCode(), opcodeEnum);
			switch (opcodeEnum) {
			case ALOAD:
				hasWideFormatSet.add(opcodeEnum);
				break;
			case DLOAD:
				hasWideFormatSet.add(opcodeEnum);
				break;
			case ILOAD:
				hasWideFormatSet.add(opcodeEnum);
				break;
			case FLOAD:
				hasWideFormatSet.add(opcodeEnum);
				break;
			case LLOAD:
				hasWideFormatSet.add(opcodeEnum);
				break;
			case ASTORE:
				hasWideFormatSet.add(opcodeEnum);
				break;
			case DSTORE:
				hasWideFormatSet.add(opcodeEnum);
				break;
			case ISTORE:
				hasWideFormatSet.add(opcodeEnum);
				break;
			case FSTORE:
				hasWideFormatSet.add(opcodeEnum);
				break;
			case LSTORE:
				hasWideFormatSet.add(opcodeEnum);
				break;
			case RET:
				hasWideFormatSet.add(opcodeEnum);
				break;
			case WIDE:
				hasWideFormatSet.add(opcodeEnum);
				break;
			case IINC:
				hasWideConstFormatSet.add(opcodeEnum);
				break;
			default:
				break;
			}
		}
	}
	
	public static OpcodeType getOpTypeByCode(int code) {
		if (opcodeMap.containsKey(code) == false) {
			logger.warn("not defined code : " + code);
		}
		return opcodeMap.get(code);
	}
	
	public static boolean hasWideFormat(OpcodeType opcodeType) {
		return hasWideFormatSet.contains(opcodeType);
	}

	public static boolean hasWideConstFormat(OpcodeType opcodeType) {
		return hasWideConstFormatSet.contains(opcodeType);
	}
}
