package jp.mytools.disassemble.opcode.logic;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.mytools.disassemble.classfile.exceptions.ClassFileFormatException;
import jp.mytools.disassemble.opcode.beans.BranchByteOpecode;
import jp.mytools.disassemble.opcode.beans.ConstOpecode;
import jp.mytools.disassemble.opcode.beans.GeneralOpecode;
import jp.mytools.disassemble.opcode.beans.InvokeDynamicOpecode;
import jp.mytools.disassemble.opcode.beans.InvokeInterfaceOpecode;
import jp.mytools.disassemble.opcode.beans.LookUpSwitchOpecode;
import jp.mytools.disassemble.opcode.beans.MultiANewArrayOpecode;
import jp.mytools.disassemble.opcode.beans.Opcode;
import jp.mytools.disassemble.opcode.beans.ReferenceOpecode;
import jp.mytools.disassemble.opcode.beans.TableSwitchOpecode;
import jp.mytools.disassemble.opcode.enums.OpcodeType;
import jp.mytools.disassemble.opcode.utils.OpcodeMapUtil;

public class OpcodeLogic {
	
	private static Logger logger = LoggerFactory.getLogger(OpcodeLogic.class);
	
	public List<Opcode> convertToOpcodeList(ByteBuffer byteBuffer,int codeLength) throws ClassFileFormatException {
		List<Opcode> opcodeList = new ArrayList<>();

		int startPosition = byteBuffer.position();
		Opcode opcode = null;
		while (byteBuffer.position() < startPosition + codeLength) {
			byte code = byteBuffer.get();
			OpcodeType opcodeType = OpcodeMapUtil.getOpTypeByCode(code & 0xff);
			logger.debug("\t\t\tOpecodeType(" + (code & 0xff) + ") : " + opcodeType.toString());
			switch (opcodeType) {
			case ALOAD:
				opcode = convertToReferenceOpecode(byteBuffer, opcodeType,false);
				break;
			case ANEWARRAY:
				opcode = convertToReferenceOpecode(byteBuffer, opcodeType,true);
				break;
			case ASTORE:
				opcode = convertToReferenceOpecode(byteBuffer, opcodeType,false);
				break;
			case BIPUSH:
				opcode = convertToReferenceOpecode(byteBuffer, opcodeType,false);
				break;
			case CHECKCAST:
				opcode = convertToReferenceOpecode(byteBuffer, opcodeType,true);
				break;
			case DLOAD:
				opcode = convertToReferenceOpecode(byteBuffer, opcodeType,false);
				break;
			case DSTORE:
				opcode = convertToReferenceOpecode(byteBuffer, opcodeType,false);
				break;
			case FLOAD:
				opcode = convertToReferenceOpecode(byteBuffer, opcodeType,false);
				break;
			case FSTORE:
				opcode = convertToReferenceOpecode(byteBuffer, opcodeType,false);
				break;
			case GETFIELD:
				opcode = convertToReferenceOpecode(byteBuffer, opcodeType,true);
				break;
			case GETSTATIC:
				opcode = convertToReferenceOpecode(byteBuffer, opcodeType,true);
				break;
			case GOTO:
				opcode = convertToBranchByteOpecode(byteBuffer, opcodeType,false);
				break;
			case GOTO_W:
				opcode = convertToBranchByteOpecode(byteBuffer, opcodeType,true);
				break;
			case IF_ACMPEQ:
				opcode = convertToBranchByteOpecode(byteBuffer, opcodeType,false);
				break;
			case IF_ACMPNE:
				opcode = convertToBranchByteOpecode(byteBuffer, opcodeType,false);
				break;
			case IF_ICMPEQ:
				opcode = convertToBranchByteOpecode(byteBuffer, opcodeType,false);
				break;
			case IF_ICMPGE:
				opcode = convertToBranchByteOpecode(byteBuffer, opcodeType,false);
				break;
			case IF_ICMPGT:
				opcode = convertToBranchByteOpecode(byteBuffer, opcodeType,false);
				break;
			case IF_ICMPLE:
				opcode = convertToBranchByteOpecode(byteBuffer, opcodeType,false);
				break;
			case IF_ICMPLT:
				opcode = convertToBranchByteOpecode(byteBuffer, opcodeType,false);
				break;
			case IF_ICMPNE:
				opcode = convertToBranchByteOpecode(byteBuffer, opcodeType,false);
				break;
			case IFEQ:
				opcode = convertToBranchByteOpecode(byteBuffer, opcodeType,false);
				break;
			case IFGE:
				opcode = convertToBranchByteOpecode(byteBuffer, opcodeType,false);
				break;
			case IFGT:
				opcode = convertToBranchByteOpecode(byteBuffer, opcodeType,false);
				break;
			case IFLE:
				opcode = convertToBranchByteOpecode(byteBuffer, opcodeType,false);
				break;
			case IFLT:
				opcode = convertToBranchByteOpecode(byteBuffer, opcodeType,false);
				break;
			case IFNE:
				opcode = convertToBranchByteOpecode(byteBuffer, opcodeType,false);
				break;
			case IFNONNULL:
				opcode = convertToBranchByteOpecode(byteBuffer, opcodeType,false);
				break;
			case IFNULL:
				opcode = convertToBranchByteOpecode(byteBuffer, opcodeType,false);
				break;
			case IINC:
				opcode = convertToConstOpecode(byteBuffer, opcodeType, false);
				break;
			case ILOAD:
				opcode = convertToReferenceOpecode(byteBuffer, opcodeType,false);
				break;
			case INSTANCEOF:
				opcode = convertToReferenceOpecode(byteBuffer, opcodeType,true);
				break;
			case INVOKEDYNAMIC:
				opcode = convertToInvokeDynamicOpecode(byteBuffer, opcodeType);
				break;
			case INVOKEINTERFACE:
				opcode = convertToInvokeInterfaceOpecode(byteBuffer, opcodeType);
				break;
			case INVOKESPECIAL:
				opcode = convertToReferenceOpecode(byteBuffer, opcodeType, true);
				break;
			case INVOKESTATIC:
				opcode = convertToReferenceOpecode(byteBuffer, opcodeType, true);
				break;
			case INVOKEVIRTUAL:
				opcode = convertToReferenceOpecode(byteBuffer, opcodeType, true);
				break;
			case ISTORE:
				opcode = convertToReferenceOpecode(byteBuffer, opcodeType, false);
				break;
			case JSR:
				opcode = convertToBranchByteOpecode(byteBuffer, opcodeType, false);
				break;
			case JSR_W:
				opcode = convertToBranchByteOpecode(byteBuffer, opcodeType, true);
				break;
			case LDC:
				opcode = convertToReferenceOpecode(byteBuffer, opcodeType, false);
				break;
			case LDC_W:
				opcode = convertToReferenceOpecode(byteBuffer, opcodeType, true);
				break;
			case LDC2_W:
				opcode = convertToReferenceOpecode(byteBuffer, opcodeType, true);
				break;
			case LLOAD:
				opcode = convertToReferenceOpecode(byteBuffer, opcodeType, false);
				break;
			case LOOKUPSWITCH:
				int zeroPaddingLookup = 0;
				while (zeroPaddingLookup < 4) {
					opcode = convertToLookUpSwitchOpecode(byteBuffer, opcodeType, zeroPaddingLookup);
					zeroPaddingLookup++;
					if (opcode != null) {
						int afterTableSwitchPosition = byteBuffer.position();
						OpcodeType nextOpcodeType = OpcodeMapUtil.getOpTypeByCode(byteBuffer.get() & 0xff);
						byteBuffer.position(afterTableSwitchPosition);
						if (nextOpcodeType == null) {
							logger.debug("illegal lookupswitchwitch structure.nextOpcodeType is null.zeroPadding = " + zeroPaddingLookup);
							continue;
						}
						break;
					}
				}
				if (opcode == null) {
					throw new ClassFileFormatException("Illegal lookupswitchwitch opecode");
				}
				

				
				break;
			case LSTORE:
				opcode = convertToReferenceOpecode(byteBuffer, opcodeType, false);
				break;
			case MULTIANEWARRAY:
				opcode = convertToMultiANewArrayOpecode(byteBuffer, opcodeType);
				break;
			case NEW:
				opcode = convertToReferenceOpecode(byteBuffer, opcodeType, true);
				break;
			case NEWARRAY:
				opcode = convertToReferenceOpecode(byteBuffer, opcodeType, false);
				break;
			case PUTFIELD:
				opcode = convertToReferenceOpecode(byteBuffer, opcodeType, true);
				break;
			case PUTSTATIC:
				opcode = convertToReferenceOpecode(byteBuffer, opcodeType, true);
				break;
			case TABLESWITCH:
				int zeroPadding = 0;
				while (zeroPadding < 4) {
					opcode = convertToTableSwitchOpecode(byteBuffer, opcodeType,zeroPadding);
					zeroPadding++;
					if (opcode != null) {
						int afterTableSwitchPosition = byteBuffer.position();
						OpcodeType nextOpcodeType = OpcodeMapUtil.getOpTypeByCode(byteBuffer.get() & 0xff);
						byteBuffer.position(afterTableSwitchPosition);
						if (nextOpcodeType == null) {
							logger.debug("illegal tableswitch structure.nextOpcodeType is null.zeroPadding = " + zeroPadding);
							continue;
						}
						break;
					}
				}
				if (opcode == null) {
					throw new ClassFileFormatException("Illegal tableswitch opecode");
				}
				
				break;
			case WIDE:
				opcodeType = OpcodeMapUtil.getOpTypeByCode(code & 0xff);	// wide format
				if (OpcodeMapUtil.hasWideFormat(opcodeType)) {
					opcode = convertToReferenceOpecode(byteBuffer, opcodeType, true);
				} else if (OpcodeMapUtil.hasWideConstFormat(opcodeType)) {
					opcode = convertToConstOpecode(byteBuffer, opcodeType, true);
				} else {
					throw new ClassFileFormatException("Illegal opecode type for wide format. opcodeType = " + opcodeType);
				}
				break;
			case ALOAD_0:
				GeneralOpecode go0 = new GeneralOpecode();
				go0.setOpcodeType(opcodeType);
				opcode = go0;
				break;
			case ALOAD_1:
				GeneralOpecode go1 = new GeneralOpecode();
				go1.setOpcodeType(opcodeType);
				opcode = go1;
				break;
			case ALOAD_2:
				GeneralOpecode go2 = new GeneralOpecode();
				go2.setOpcodeType(opcodeType);
				opcode = go2;
				break;
			case ALOAD_3:
				GeneralOpecode go3 = new GeneralOpecode();
				go3.setOpcodeType(opcodeType);
				opcode = go3;
				break;
			case SIPUSH:
				opcode = convertToReferenceOpecode(byteBuffer, opcodeType,true);
				break;
			default:
				GeneralOpecode go = new GeneralOpecode();
				go.setOpcodeType(opcodeType);
				opcode = go;
				break;
				//throw new ClassFileFormatException("Illegal opecode type.undifined opcodeType = " + opcodeType.toString());
			}
			
			opcodeList.add(opcode);
		}
		
		return opcodeList;
	}
	
	private TableSwitchOpecode convertToTableSwitchOpecode(ByteBuffer byteBuffer ,OpcodeType opcodeType,int zeroPadding) {
		int startPosition = byteBuffer.position();
		TableSwitchOpecode tso = new TableSwitchOpecode();
		tso.setOpcodeType(opcodeType);
		int i = 0;
		while (i < zeroPadding) {
			logger.debug("\t\t\t\t\tZeroPadding[" + i + "] : " + (byteBuffer.get() & 0xff)); // skip zero paddings
			i++;
		}
		
		tso.setDefaultOffset(byteBuffer.getInt());
		logger.debug("\t\t\t\t\tdefaultOffset : " + tso.getDefaultOffset()); 

		tso.setLow(byteBuffer.getInt());
		logger.debug("\t\t\t\t\tlow : " + tso.getLow()); 

		tso.setHigh(byteBuffer.getInt());
		logger.debug("\t\t\t\t\thigh : " + tso.getHigh()); 

		int offsetCount = tso.getHigh() - tso.getLow() + 1;

		// TODO 他にもっと動的な0paddingを解決出来る方法を考えたい
		if (offsetCount < 1) {
			logger.debug("\t\t\t\t\tpadding is illegal (offset is negative value): " + zeroPadding);
			byteBuffer.position(startPosition);
			return null;
		}
		int offsetCountAfterPosition = offsetCount * 4;
		if (offsetCountAfterPosition < 0 || offsetCountAfterPosition < offsetCount) {
			// overflow
			logger.debug("\t\t\t\t\tpadding is illegal (tableswitch's offsetCountAfterPosition is negative value): " + offsetCountAfterPosition); 
			byteBuffer.position(startPosition);
			return null;
		}
		int afterPosition = byteBuffer.position() + offsetCountAfterPosition;
		if (afterPosition < 0) {
			logger.debug("\t\t\t\t\tpadding is illegal (tableswitch's afterPosition is negative value): " + afterPosition); 
			byteBuffer.position(startPosition);
			return null;
		}
		if (byteBuffer.limit() < afterPosition) {
			logger.debug("\t\t\t\t\tpadding is illegal (tableswitch's opecode bytes are over total bytes): " + zeroPadding); 
			byteBuffer.position(startPosition);
			return null;
		}
		
		i = 0;
		int[] offsets = new int[offsetCount];
		
		while (i < offsetCount) {
			offsets[i] = (byteBuffer.getInt());
			logger.debug("\t\t\t\t\toffset[" + i + "] : " + offsets[i]);
			i++;
		}
		
		tso.setOffsets(offsets);
		
		return tso;
	}
	
	private MultiANewArrayOpecode convertToMultiANewArrayOpecode(ByteBuffer byteBuffer ,OpcodeType opcodeType) {
		MultiANewArrayOpecode manao = new MultiANewArrayOpecode();
		manao.setOpcodeType(opcodeType);
		int index = 0;
		index = byteBuffer.getShort();
		manao.setIndex(index);
		logger.debug("\t\t\t\t\tindex : " + index);
		manao.setDimension(byteBuffer.getShort() & 0xff);
		
		return manao;
	}
	
	private LookUpSwitchOpecode convertToLookUpSwitchOpecode(ByteBuffer byteBuffer ,OpcodeType opcodeType,int zeroPadding) {
		int startPosition = byteBuffer.position();
		LookUpSwitchOpecode luso = new LookUpSwitchOpecode();
		luso.setOpcodeType(opcodeType);
		
		int i = 0;
		while (i < zeroPadding) {
			logger.debug("\t\t\t\t\t" + Integer.toString(byteBuffer.get() & 0xff)); // skip zero paddings
			i++;
		}
		
		luso.setDefaultOffset((byteBuffer.getInt()));
		luso.setPairCount((byteBuffer.getInt()));

		// TODO 他にもっと動的な0paddingを解決出来る方法を考えたい
		if (luso.getPairCount() < 0) {
			logger.debug("\t\t\t\t\tpadding is illegal (offset is negative value): " + zeroPadding);
			byteBuffer.position(startPosition);
			return null;
		}
		logger.debug("" + byteBuffer.limit() + ":" + byteBuffer.position()  + ":" + luso.getPairCount() + ":" + (byteBuffer.position() + luso.getPairCount() * 8));
		if ((byteBuffer.position() + luso.getPairCount() * 8) < 0 || byteBuffer.limit() < (byteBuffer.position() + luso.getPairCount() * 8)) {
			logger.debug("\t\t\t\t\tpadding is illegal (lookupswitch's opecode bytes are over total bytes): " + zeroPadding); 
			byteBuffer.position(startPosition);
			return null;
		}
		
		
		i = 0;
		Map<Integer,Integer> offsetMap = new HashMap<>();
		while (i < luso.getPairCount()) {
			int key = (byteBuffer.getInt());
			int offset = (byteBuffer.getInt());
			offsetMap.put(key, offset);
			i++;
		}
		
		luso.setOffsetMap(offsetMap);
		
		return luso;
	}
	
	private InvokeInterfaceOpecode convertToInvokeInterfaceOpecode(ByteBuffer byteBuffer ,OpcodeType opcodeType) {
		InvokeInterfaceOpecode iio = new InvokeInterfaceOpecode();
		iio.setOpcodeType(opcodeType);
		iio.setIndex(byteBuffer.getShort());
		logger.debug("\t\t\t\t\tindex : " + iio.getIndex());
		iio.setCount(byteBuffer.getShort() & 0xff);
		logger.debug("\t\t\t\t\tCount" + iio.getCount());
//		iio.setThirdIndex(byteBuffer.getShort() & 0xff);
		logger.debug("\t\t\t\t\tThirdIndex" + iio.getThirdIndex());
		return iio;
	}
	
	private InvokeDynamicOpecode convertToInvokeDynamicOpecode(ByteBuffer byteBuffer ,OpcodeType opcodeType) {
		InvokeDynamicOpecode ido = new InvokeDynamicOpecode();
		ido.setOpcodeType(opcodeType);
		ido.setIndex(byteBuffer.getShort());
		ido.setThirdOperand(0);
		ido.setFourthOperand(0);
		return ido;
	}
	
	private ReferenceOpecode convertToReferenceOpecode(ByteBuffer byteBuffer ,OpcodeType opcodeType ,boolean isWide) {
		ReferenceOpecode ro = new ReferenceOpecode();
		ro.setOpcodeType(opcodeType);
		
		int index = 0;
		if (isWide) {
			index = byteBuffer.getShort();
		} else {
			index = byteBuffer.get() & 0xff;
		}
		
		ro.setIndex(index);
		logger.debug("\t\t\t\t\tindex : " + index);
		return ro;
	}
	
	private BranchByteOpecode convertToBranchByteOpecode(ByteBuffer byteBuffer ,OpcodeType opcodeType ,boolean isWide) {
		BranchByteOpecode bbo = new BranchByteOpecode();
		bbo.setOpcodeType(opcodeType);

		if (isWide) {
			bbo.setBranch(byteBuffer.getInt());
		} else {
			bbo.setBranch(byteBuffer.getShort());
		}

		logger.debug("\t\t\t\t\tBranch : " + bbo.getBranch());

		return bbo;
	}
	
	private ConstOpecode convertToConstOpecode(ByteBuffer byteBuffer ,OpcodeType opcodeType ,boolean isWide) {
		ConstOpecode co = new ConstOpecode();
		co.setOpcodeType(opcodeType);
		if (isWide) {
			co.setIndex(byteBuffer.getShort());
			co.setConstValue(byteBuffer.getShort());
		} else {
			co.setIndex(byteBuffer.get() & 0xff);
			co.setConstValue(byteBuffer.get() & 0xff);
		}
		
		logger.debug("\t\t\t\t\tIndex : " + co.getIndex());
		logger.debug("\t\t\t\t\tConstValue : " + co.getConstValue());
		return co;
	}
	
}
