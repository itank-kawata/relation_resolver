package jp.mytools.disassemble.attributes.logic;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.mytools.disassemble.attributes.beans.Annotation;
import jp.mytools.disassemble.attributes.beans.AnnotationDefaultAttributeInfo;
import jp.mytools.disassemble.attributes.beans.AnnotationValue;
import jp.mytools.disassemble.attributes.beans.AppendFrame;
import jp.mytools.disassemble.attributes.beans.ArrayValue;
import jp.mytools.disassemble.attributes.beans.Attribute;
import jp.mytools.disassemble.attributes.beans.BootstrapMethodsAttributeInfo;
import jp.mytools.disassemble.attributes.beans.ChopFrame;
import jp.mytools.disassemble.attributes.beans.ClassInfoValue;
import jp.mytools.disassemble.attributes.beans.CodeAttributeInfo;
import jp.mytools.disassemble.attributes.beans.ConstValue;
import jp.mytools.disassemble.attributes.beans.ConstantValueAttributeInfo;
import jp.mytools.disassemble.attributes.beans.DeprecatedAttributeInfo;
import jp.mytools.disassemble.attributes.beans.ElementValue;
import jp.mytools.disassemble.attributes.beans.ElementValuePair;
import jp.mytools.disassemble.attributes.beans.EnclosingMethodAttributeInfo;
import jp.mytools.disassemble.attributes.beans.EnumConstValue;
import jp.mytools.disassemble.attributes.beans.ExceptionTableInfo;
import jp.mytools.disassemble.attributes.beans.ExceptionsAttributeInfo;
import jp.mytools.disassemble.attributes.beans.FullFrame;
import jp.mytools.disassemble.attributes.beans.GeneralVariableInfo;
import jp.mytools.disassemble.attributes.beans.InnerClasses;
import jp.mytools.disassemble.attributes.beans.InnerClassesAttributeInfo;
import jp.mytools.disassemble.attributes.beans.LineNumberTable;
import jp.mytools.disassemble.attributes.beans.LineNumberTableAttributeInfo;
import jp.mytools.disassemble.attributes.beans.LocalVariableTable;
import jp.mytools.disassemble.attributes.beans.LocalVariableTableAttributeInfo;
import jp.mytools.disassemble.attributes.beans.LocalVariableTypeTable;
import jp.mytools.disassemble.attributes.beans.LocalVariableTypeTableAttributeInfo;
import jp.mytools.disassemble.attributes.beans.ObjectVariableInfo;
import jp.mytools.disassemble.attributes.beans.ParameterAnnotation;
import jp.mytools.disassemble.attributes.beans.RuntimeInvisibleAnnotationsAttributeInfo;
import jp.mytools.disassemble.attributes.beans.RuntimeInvisibleParameterAnnotationsAttributeInfo;
import jp.mytools.disassemble.attributes.beans.RuntimeVisibleAnnotationsAttributeInfo;
import jp.mytools.disassemble.attributes.beans.RuntimeVisibleParameterAnnotationsAttributeInfo;
import jp.mytools.disassemble.attributes.beans.SameFrame;
import jp.mytools.disassemble.attributes.beans.SameFrameExtended;
import jp.mytools.disassemble.attributes.beans.SameLocals1StackItemFrame;
import jp.mytools.disassemble.attributes.beans.SameLocals1StackItemFrameExtended;
import jp.mytools.disassemble.attributes.beans.SignatureAttributeInfo;
import jp.mytools.disassemble.attributes.beans.SourceDebugExtensionAttributeInfo;
import jp.mytools.disassemble.attributes.beans.SourceFileAttributeInfo;
import jp.mytools.disassemble.attributes.beans.StackMapFrame;
import jp.mytools.disassemble.attributes.beans.StackMapTableAttributeInfo;
import jp.mytools.disassemble.attributes.beans.SyntheticAttributeInfo;
import jp.mytools.disassemble.attributes.beans.UninitializedVariableInfo;
import jp.mytools.disassemble.attributes.beans.VerificationTypeInfo;
import jp.mytools.disassemble.attributes.enums.AttributeType;
import jp.mytools.disassemble.classfile.exceptions.ClassFileFormatException;
import jp.mytools.disassemble.constantpool.beans.ConstantPool;
import jp.mytools.disassemble.constantpool.beans.Utf8ConstantPool;
import jp.mytools.disassemble.opcode.logic.OpcodeLogic;

public class AttributeLogic {
	
	private static Logger logger = LoggerFactory.getLogger(AttributeLogic.class ); 
	
	private static Set<String> constValueIndex = new HashSet<>();
	static {
		constValueIndex.add("s");
		constValueIndex.add("B");
		constValueIndex.add("C");
		constValueIndex.add("D");
		constValueIndex.add("F");
		constValueIndex.add("I");
		constValueIndex.add("J");
		constValueIndex.add("S");
		constValueIndex.add("Z");
	}
	
	private OpcodeLogic opcodeLogic = new OpcodeLogic();

	public Attribute convertToAttribute(ByteBuffer byteBuffer,
			Map<Integer, ConstantPool> cpMap) throws ClassFileFormatException {

		logger.debug("\tAttribute------------------------");
		int attributeNameIndex = byteBuffer.getShort(); // attribute_name_index(2byte)
		ConstantPool cp = cpMap.get(attributeNameIndex);
		if (cp instanceof Utf8ConstantPool == false)
			throw new ClassFileFormatException(
					"Illeagal attribute nameIndex.attributeNameIndex = "
							+ attributeNameIndex);

		String attributeTypeName = ((Utf8ConstantPool) cp).getValue();
		AttributeType type = AttributeType
				.getAttributeTypeByName(attributeTypeName);
		if (type == null)
			throw new ClassFileFormatException(
					"Illeagal attribute type.attributeTypeName = "
							+ attributeTypeName);

		int attributeLength = byteBuffer.getInt(); // attribute_length(4byte)

		Attribute attribute = null;
		logger.debug("\t\ttype : " + type.getName());
		switch (type) {
		case CONSTANTVALUE:
			attribute = convertToConstantValue(byteBuffer, attributeNameIndex,
					attributeLength);
			break;
		case CODE:
			attribute = convertToCode(byteBuffer, cpMap, attributeNameIndex,
					attributeLength);
			break;
		case EXCEPTIONS:
			attribute = convertToExceptions(byteBuffer, attributeNameIndex,
					attributeLength);
			break;
		case INNERCLASSES:
			attribute = convertToInnerClassesAttribute(byteBuffer,
					attributeNameIndex, attributeLength);
			break;
		case SYNTHETIC:
			SyntheticAttributeInfo sai = new SyntheticAttributeInfo();
			sai.setAttributeNameIndex(attributeNameIndex);
			sai.setAttributeLength(attributeLength);
			attribute = sai;
			break;
		case SOURCEFILE:
			attribute = convertToSourceFile(byteBuffer, attributeNameIndex,
					attributeLength);
			break;
		case LINENUMBERTABLE:
			attribute = convertToLineNumberTable(byteBuffer,
					attributeNameIndex, attributeLength);
			break;
		case LOCALVARIABLETABLE:
			attribute = convertToLocalVariableTable(byteBuffer,
					attributeNameIndex, attributeLength);
			break;
		case DEPRECATED:
			attribute = convertToDeprecated(attributeNameIndex, attributeLength);
			break;
		case STACKMAPTABLE:
			attribute = convertToStackMapTableAttributeInfo(byteBuffer,
					attributeNameIndex, attributeLength);
			break;
		case ENCLOSINGMETHOD:
			attribute = convertToEnclosingMethod(byteBuffer,attributeNameIndex, attributeLength);
			break;
		case SIGNATURE:
			attribute = convertToSignature(byteBuffer, attributeNameIndex, attributeLength);
			break;
		case RUNTIMEVISIBLEANNOTATIONS:
			attribute = convertToRuntimeVisibleAnnotations(byteBuffer, attributeNameIndex, attributeLength);
			break;
		case RUNTIMEINVISIBLEANNOTATIONS:
			attribute = convertToRuntimeInvisibleAnnotations(byteBuffer, attributeNameIndex, attributeLength);
			break;
		case LOCALVARIABLETYPETABLE:
			attribute = convertToLocalVariableTypeTable(byteBuffer, attributeNameIndex, attributeLength);
			break;
		case MISSINGTYPES:
			logger.debug("MISSING_TYPES : length = " + attributeLength);
			break;
		case INCONSISTENTHIERARCHY:
			logger.debug("INCONSISTENTHIERARCHY : length = " + attributeLength);
			break;
		case RUNTIMEVISIBLEPARAMETERANNOTATIONS:
			attribute = convertToRuntimeVisibleParameterAnnotations(byteBuffer, attributeNameIndex, attributeLength);
			break;
		default:
			throw new IllegalStateException("undefined type : " + type.getName());
		}

		return attribute;
	}

	private StackMapTableAttributeInfo convertToStackMapTableAttributeInfo(
			ByteBuffer byteBuffer, int attributeNameIndex, int attributeLength) {
		StackMapTableAttributeInfo smtai = new StackMapTableAttributeInfo();
		smtai.setAttributeNameIndex(attributeNameIndex);
		logger.debug("\t\t\tAttributeNameIndex : "
				+ smtai.getAttributeNameIndex());
		smtai.setAttributeLength(attributeLength);
		logger.debug("\t\t\tAttributeLength : "
				+ smtai.getAttributeLength());
		smtai.setNumberOfEntries(byteBuffer.getShort());
		logger.debug("\t\t\tNumberOfEntries : "
				+ smtai.getNumberOfEntries());

		if (smtai.getNumberOfEntries() > 0) {
			int i = 0;
			StackMapFrame[] frames = new StackMapFrame[smtai
					.getNumberOfEntries()];
			while (i < smtai.getNumberOfEntries()) {
				StackMapFrame smFrame = null;
				int frameType = (byteBuffer.get() & 0xff);
				logger.debug("\t\t\t\tframeType : " + frameType);
				if (frameType >= 0 && frameType <= 63) {
					SameFrame sf = new SameFrame();
					sf.setFrameType(frameType);
					smFrame = sf;
				} else if (frameType >= 64 && frameType <= 127) {
					SameLocals1StackItemFrame sl1sif = new SameLocals1StackItemFrame();
					sl1sif.setFrameType(frameType);
					sl1sif.setStack(convertToVerificationTypeInfo(byteBuffer));
					smFrame = sl1sif;
				} else if (frameType == 247) {
					SameLocals1StackItemFrameExtended sl1sife = new SameLocals1StackItemFrameExtended();
					sl1sife.setFrameType(frameType);
					sl1sife.setOffsetDelta(byteBuffer.getShort());
					sl1sife.setStack(convertToVerificationTypeInfo(byteBuffer));
					smFrame = sl1sife;
				} else if (frameType >= 248 && frameType <= 250) {
					ChopFrame cf = new ChopFrame();
					cf.setFrameType(frameType);
					cf.setOffsetDelta(byteBuffer.getShort());
					logger.debug("\t\t\tOffsetDelta : " + cf.getOffsetDelta());
					smFrame = cf;
				} else if (frameType == 251) {
					SameFrameExtended sfe = new SameFrameExtended();
					sfe.setFrameType(frameType);
					sfe.setOffsetDelta(byteBuffer.getShort());
					logger.debug("\t\t\tOffsetDelta : " + sfe.getOffsetDelta());
					smFrame = sfe;
				} else if (frameType >= 252 && frameType <= 254) {
					AppendFrame af = new AppendFrame();
					af.setFrameType(frameType);
					af.setOffsetDelta(byteBuffer.getShort());
					logger.debug("\t\t\tOffsetDelta : " + af.getOffsetDelta());
					int localsCount = frameType - 251;
					VerificationTypeInfo[] locals = new VerificationTypeInfo[localsCount];
					int j = 0;
					while (j < localsCount) {
						locals[j] = convertToVerificationTypeInfo(byteBuffer);
						j++;
					}
					af.setLocals(locals);
					smFrame = af;
				} else if (frameType == 255) {
					FullFrame ff = new FullFrame();
					ff.setFrameType(frameType);
					logger.debug("\t\t\tFrameType : FullFrame (" + ff.getFrameType() + ")");
					ff.setOffsetDelta(byteBuffer.getShort());
					logger.debug("\t\t\tOffsetDelta : " + ff.getOffsetDelta());
					ff.setNumberOfLocals(byteBuffer.getShort());
					logger.debug("\t\t\tNumberOfLocals : " + ff.getNumberOfLocals());
					if (ff.getNumberOfLocals() > 0) {
						VerificationTypeInfo[] locals = new VerificationTypeInfo[ff.getNumberOfLocals()];
						int j = 0;
						while (j < ff.getNumberOfLocals()) {
							locals[j] = convertToVerificationTypeInfo(byteBuffer);
							j++;
						}
						ff.setLocals(locals);
					}
					ff.setNumberOfStackItems(byteBuffer.getShort());
					if (ff.getNumberOfStackItems() > 0) {
						VerificationTypeInfo[] stack = new VerificationTypeInfo[ff.getNumberOfStackItems()];
						int j = 0;
						while (j < ff.getNumberOfStackItems()) {
							stack[j] = convertToVerificationTypeInfo(byteBuffer);
							j++;
						}
						ff.setStack(stack);
					}
					
					smFrame = ff;
				} else {
					logger.debug("undefined frameType : " + frameType);
					throw new IllegalStateException("undefined frameType.frameType = " + frameType);
				}
				
				frames[i] = smFrame;
				i++;
			}
			smtai.setEntries(frames);
		}
		return smtai;
	}

	/**
	 * union verification_type_info { 
	 * Top_variable_info; 0
	 * Integer_variable_info; 1
	 * Float_variable_info; 2
	 * Long_variable_info; 4
	 * Double_variable_info; 3
	 * Null_variable_info; 5
	 * UninitializedThis_variable_info; 6
	 * Object_variable_info; 7
	 * Uninitialized_variable_info; 8
	 * }
	 * 
	 * @param byteBuffer
	 * @return
	 */
	private VerificationTypeInfo convertToVerificationTypeInfo(ByteBuffer byteBuffer) {

		int tag = (byteBuffer.get() & 0xff);
		logger.debug("\t\t\t\t\t\ttag : " + tag);
		VerificationTypeInfo info = null;
		if (tag >= 0 && tag <= 6) {
			GeneralVariableInfo gvi = new GeneralVariableInfo();
			gvi.setTag(tag);
			info = gvi;
		} else if (tag == 7) {
			ObjectVariableInfo ovi = new ObjectVariableInfo();
			ovi.setTag(tag);
			ovi.setCpoolIndex(byteBuffer.getShort());
			logger.debug("\t\t\t\t\t\tCpoolIndex : " + ovi.getCpoolIndex());
			info = ovi;
		} else if (tag == 8) {
			UninitializedVariableInfo uvi = new UninitializedVariableInfo();
			uvi.setTag(tag);
			uvi.setOffset(byteBuffer.getShort());
			logger.debug("\t\t\t\t\t\tCOffset : " + uvi.getOffset());
			info = uvi;
		} else {
			throw new IllegalStateException("undefined VerificationTypeInfo tag. tag = " + tag);
		}
		return info;
	}

	private InnerClassesAttributeInfo convertToInnerClassesAttribute(
			ByteBuffer byteBuffer, int attributeNameIndex, int attributeLength) {
		InnerClassesAttributeInfo icai = new InnerClassesAttributeInfo();
		icai.setAttributeNameIndex(attributeNameIndex);
		logger.debug("\t\t\tAttributeNameIndex : " + icai.getAttributeNameIndex());
		icai.setAttributeLength(attributeLength);
		logger.debug("\t\t\tAttributeLength : " + icai.getAttributeLength());
		icai.setNumberOfClasses(byteBuffer.getShort());
		logger.debug("\t\t\tNumberOfClasses : " + icai.getNumberOfClasses());
		if (icai.getNumberOfClasses() > 0) {
			int i = 0;
			InnerClasses[] classes = new InnerClasses[icai.getNumberOfClasses()];
			while (i < icai.getNumberOfClasses()) {
				InnerClasses clazz = new InnerClasses();
				clazz.setInnerClassInfoIndex(byteBuffer.getShort());
				logger.debug("\t\t\t\tInnerClassInfoIndex[" + i + "] : " + clazz.getInnerClassInfoIndex());
				clazz.setOuterClassInfoIndex(byteBuffer.getShort());
				logger.debug("\t\t\t\tInnerOuterClassInfoIndex[" + i + "] : " + clazz.getOuterClassInfoIndex());
				clazz.setInnerNameIndex(byteBuffer.getShort());
				logger.debug("\t\t\t\tInnerNameIndex[" + i + "] : " + clazz.getInnerNameIndex());
				clazz.setInnerClassAccessFlags(byteBuffer.getShort());
				logger.debug("\t\t\t\tInnerClassAccessFlags[" + i + "] : " + clazz.getInnerClassAccessFlags());
				classes[i] = clazz;
				i++;
			}
			icai.setClasses(classes);
		}

		return icai;
	}

	private ConstantValueAttributeInfo convertToConstantValue(
			ByteBuffer byteBuffer, int attributeNameIndex, int attributeLength) {
		ConstantValueAttributeInfo cvai = new ConstantValueAttributeInfo();
		cvai.setAttributeNameIndex(attributeNameIndex);
		logger.debug("\t\t\tAttributeNameIndex : "
				+ cvai.getAttributeNameIndex());
		cvai.setAttributeLength(attributeLength);
		logger.debug("\t\t\tAttributeLength : "
				+ cvai.getAttributeLength());
		cvai.setConstantvalueIndex(byteBuffer.getShort()); // constantvalue_index(2byte)
		logger.debug("\t\t\tConstantvalueIndex : "
				+ cvai.getConstantvalueIndex());
		return cvai;
	}

	private CodeAttributeInfo convertToCode(ByteBuffer byteBuffer,
			Map<Integer, ConstantPool> cpMap, int attributeNameIndex,
			int attributeLength) throws ClassFileFormatException {
		CodeAttributeInfo cai = new CodeAttributeInfo();
		cai.setAttributeNameIndex(attributeNameIndex);
		cai.setAttributeLength(attributeLength);
		cai.setMaxStack(byteBuffer.getShort()); // max_stack(2byte)
		logger.debug("\t\t\tMaxStack : " + cai.getMaxStack());
		cai.setMaxLocals(byteBuffer.getShort()); // max_locals(2byte)
		logger.debug("\t\t\tMaxLocals : " + cai.getMaxStack());
		cai.setCodeLength(byteBuffer.getInt()); // code_length(4byte)
		logger.debug("\t\t\tCodeLength : " + cai.getMaxStack());
		cai.setOpcodes(opcodeLogic.convertToOpcodeList(byteBuffer,cai.getCodeLength()));
		cai.setExceptionTableLength(byteBuffer.getShort()); // exception_table_length(2byte)
		if (cai.getExceptionTableLength() > 0) {
			ExceptionTableInfo[] etis = new ExceptionTableInfo[cai
					.getExceptionTableLength()];
			int i = 0;
			while (i < cai.getExceptionTableLength()) {
				etis[i] = convertToExceptionTableInfo(byteBuffer);
				i++;
			}
		}

		cai.setAttributesCount(byteBuffer.getShort()); // attribute_count(2byte)
		if (cai.getAttributesCount() > 0) {
			Attribute[] attributes = new Attribute[cai.getAttributesCount()];
			int i = 0;
			while (i < cai.getAttributesCount()) {
				attributes[i] = convertToAttribute(byteBuffer, cpMap);
				i++;
			}
		}
		return cai;
	}

	private StackMapTableAttributeInfo convertToStackMapTable(
			ByteBuffer byteBuffer) {
		return null;
	}

	private ExceptionsAttributeInfo convertToExceptions(ByteBuffer byteBuffer,
			int attributeNameIndex, int attributeLength) {
		ExceptionsAttributeInfo eai = new ExceptionsAttributeInfo();
		eai.setAttributeNameIndex(attributeNameIndex);
		logger.debug("\t\t\tAttributeNameIndex : "
				+ eai.getAttributeNameIndex());
		eai.setAttributeLength(attributeLength);
		logger.debug("\t\t\tAttributeLength : "
				+ eai.getAttributeLength());
		eai.setNumberOfExceptions(byteBuffer.getShort());
		logger.debug("\t\t\tNumberOfExceptions : "
				+ eai.getNumberOfExceptions());
		int i = 0;
		int[] exceptionIndexTable = new int[eai.getNumberOfExceptions()];
		while (i < eai.getNumberOfExceptions()) {
			exceptionIndexTable[i] = byteBuffer.getShort();
			logger.debug("\t\t\texceptionIndexTable[" + i + "] : "
					+ exceptionIndexTable[i]);
			i++;
		}
		eai.setExceptionIndexTable(exceptionIndexTable);
		return eai;
	}

	private EnclosingMethodAttributeInfo convertToEnclosingMethod(ByteBuffer byteBuffer,int attributeNameIndex, int attributeLength) {
		EnclosingMethodAttributeInfo ema = new EnclosingMethodAttributeInfo();
		ema.setAttributeNameIndex(attributeNameIndex);
		logger.debug("\t\t\tAttributeNameIndex : " + ema.getAttributeNameIndex());
		ema.setAttributeLength(attributeLength);
		logger.debug("\t\t\tAttributeLength : " + ema.getAttributeLength());
		ema.setClassIndex(byteBuffer.getShort());
		logger.debug("\t\t\tClassIndex : " + ema.getClassIndex());
		ema.setMethodIndex(byteBuffer.getShort());
		logger.debug("\t\t\tMethodIndex : " + ema.getMethodIndex());
		return ema;
	}

	private SignatureAttributeInfo convertToSignature(ByteBuffer byteBuffer,int attributeNameIndex, int attributeLength) {
		SignatureAttributeInfo sai = new SignatureAttributeInfo();
		sai.setAttributeNameIndex(attributeNameIndex);
		logger.debug("\t\t\tAttributeNameIndex : " + sai.getAttributeNameIndex());
		sai.setAttributeLength(attributeLength);
		logger.debug("\t\t\tAttributeLength : " + sai.getAttributeLength());
		sai.setSignatureIndex(byteBuffer.getShort());
		logger.debug("\t\t\tSignatureIndex : " + sai.getSignatureIndex());
		return sai;
	}

	private SourceFileAttributeInfo convertToSourceFile(ByteBuffer byteBuffer,
			int attributeNameIndex, int attributeLength) {
		SourceFileAttributeInfo sfai = new SourceFileAttributeInfo();
		sfai.setAttributeNameIndex(attributeNameIndex);
		logger.debug("\t\t\tAttributeNameIndex : " + sfai.getAttributeNameIndex());
		sfai.setAttributeLength(attributeLength);
		logger.debug("\t\t\tAttributeLength : " + sfai.getAttributeLength());
		sfai.setSourcefileIndex(byteBuffer.getShort());
		logger.debug("\t\t\tSourcefileIndex : " + sfai.getSourcefileIndex());
		return sfai;
	}

	private SourceDebugExtensionAttributeInfo convertToSourceDebugExtension(
			ByteBuffer byteBuffer) {
		return null;
	};

	private LineNumberTableAttributeInfo convertToLineNumberTable(
			ByteBuffer byteBuffer, int attributeNameIndex, int attributeLength) {
		LineNumberTableAttributeInfo lnta = new LineNumberTableAttributeInfo();
		lnta.setAttributeNameIndex(attributeNameIndex);
		logger.debug("\t\t\tAttributeNameIndex : "
				+ lnta.getAttributeNameIndex());
		lnta.setAttributeLength(attributeLength);
		logger.debug("\t\t\tAttributeLength : "
				+ lnta.getAttributeLength());
		lnta.setLineNumberTableLength(byteBuffer.getShort());
		logger.debug("\t\t\tLineNumberTableLength : "
				+ lnta.getLineNumberTableLength());
		if (lnta.getLineNumberTableLength() > 0) {
			LineNumberTable[] lineNumberTables = new LineNumberTable[lnta
					.getLineNumberTableLength()];
			int i = 0;
			while (i < lnta.getLineNumberTableLength()) {
				LineNumberTable lineNumberTable = new LineNumberTable();
				lineNumberTable.setStartPc(byteBuffer.getShort());
				logger.debug("\t\t\t\tStartPc : "
						+ lineNumberTable.getStartPc());
				lineNumberTable.setLineNumber(byteBuffer.getShort());
				logger.debug("\t\t\t\tLineNumber : "
						+ lineNumberTable.getLineNumber());
				lineNumberTables[i] = lineNumberTable;
				i++;
			}
			lnta.setLineNumberTables(lineNumberTables);
		}
		return lnta;
	};

	private LocalVariableTableAttributeInfo convertToLocalVariableTable(
			ByteBuffer byteBuffer, int attributeNameIndex, int attributeLength) {
		LocalVariableTableAttributeInfo lvtai = new LocalVariableTableAttributeInfo();
		lvtai.setAttributeNameIndex(attributeNameIndex);
		logger.debug("\t\t\tAttributeNameIndex : "+ lvtai.getAttributeNameIndex());
		lvtai.setAttributeLength(attributeLength);
		logger.debug("\t\t\tAttributeLength : "+ lvtai.getAttributeLength());
		lvtai.setLocalVariableTableLength(byteBuffer.getShort());
		logger.debug("\t\t\tLocalVariableTableLength : "
				+ lvtai.getLocalVariableTableLength());
		if (lvtai.getLocalVariableTableLength() > 0) {
			LocalVariableTable[] localVariableTables = new LocalVariableTable[lvtai
					.getLocalVariableTableLength()];
			int i = 0;
			while (i < lvtai.getLocalVariableTableLength()) {
				LocalVariableTable localVariableTable = new LocalVariableTable();
				localVariableTable.setStartPc(byteBuffer.getShort());
				logger.debug("\t\t\t\tStartPc : "
						+ localVariableTable.getStartPc());
				localVariableTable.setLength(byteBuffer.getShort());
				logger.debug("\t\t\t\tLength : "
						+ localVariableTable.getLength());
				localVariableTable.setNameIndex(byteBuffer.getShort());
				logger.debug("\t\t\t\tNameIndex : "
						+ localVariableTable.getNameIndex());
				localVariableTable.setDescriptorIndex(byteBuffer.getShort());
				logger.debug("\t\t\t\tDescriptorIndex : "
						+ localVariableTable.getDescriptorIndex());
				localVariableTable.setIndex(byteBuffer.getShort());
				logger.debug("\t\t\t\tIndex : "
						+ localVariableTable.getIndex());
				localVariableTables[i] = localVariableTable;
				i++;
			}
			lvtai.setLocalVariableTables(localVariableTables);
		}
		return lvtai;
	};

	private LocalVariableTypeTableAttributeInfo convertToLocalVariableTypeTable(
			ByteBuffer byteBuffer,int attributeNameIndex,int attributeLength) {
		LocalVariableTypeTableAttributeInfo lvttai = new LocalVariableTypeTableAttributeInfo();
		lvttai.setAttributeNameIndex(attributeNameIndex);
		logger.debug("\t\t\tAttributeNameIndex : "+ lvttai.getAttributeNameIndex());
		lvttai.setAttributeLength(attributeLength);
		logger.debug("\t\t\tAttributeLength : "+ lvttai.getAttributeLength());
		lvttai.setLocalVariableTypeTableLength(byteBuffer.getShort());
		logger.debug("\t\t\tLocalVariableTypeTableLength : "
				+ lvttai.getLocalVariableTypeTableLength());
		if (lvttai.getLocalVariableTypeTableLength() > 0) {
			LocalVariableTypeTable[] localVariableTypeTables = new LocalVariableTypeTable[lvttai.getLocalVariableTypeTableLength()];
			int i = 0;
			while (i < lvttai.getLocalVariableTypeTableLength()) {
				LocalVariableTypeTable localVariableTypeTable = new LocalVariableTypeTable();
				localVariableTypeTable.setStartPc(byteBuffer.getShort());
				logger.debug("\t\t\t\tStartPc : "
						+ localVariableTypeTable.getStartPc());
				localVariableTypeTable.setLength(byteBuffer.getShort());
				logger.debug("\t\t\t\tLength : "
						+ localVariableTypeTable.getLength());
				localVariableTypeTable.setNameIndex(byteBuffer.getShort());
				logger.debug("\t\t\t\tNameIndex : "
						+ localVariableTypeTable.getNameIndex());
				localVariableTypeTable.setSignatureIndex(byteBuffer.getShort());
				logger.debug("\t\t\t\tSignatureIndex : "
						+ localVariableTypeTable.getSignatureIndex());
				localVariableTypeTable.setIndex(byteBuffer.getShort());
				logger.debug("\t\t\t\tIndex : "
						+ localVariableTypeTable.getIndex());
				localVariableTypeTables[i] = localVariableTypeTable;
				i++;
			}
			lvttai.setLocalVariableTypeTables(localVariableTypeTables);
		}
		return lvttai;
	};

	private DeprecatedAttributeInfo convertToDeprecated(int attributeNameIndex,
			int attributeLength) {
		DeprecatedAttributeInfo dai = new DeprecatedAttributeInfo();
		dai.setAttributeNameIndex(attributeNameIndex);
		dai.setAttributeLength(attributeLength);
		return dai;
	};

	private RuntimeVisibleAnnotationsAttributeInfo convertToRuntimeVisibleAnnotations(ByteBuffer byteBuffer,int attributeNameIndex,int attributeLength) {
		
		RuntimeVisibleAnnotationsAttributeInfo rvaai = new RuntimeVisibleAnnotationsAttributeInfo();
		rvaai.setAttributeNameIndex(attributeNameIndex);
		logger.debug("\t\t\tAttributeNameIndex : "+ rvaai.getAttributeNameIndex());
		rvaai.setAttributeLength(attributeLength);
		logger.debug("\t\t\tAttributeLength : "+ rvaai.getAttributeLength());
		rvaai.setNumAnnotations(byteBuffer.getShort());
		logger.debug("\t\t\tNumAnnotations : "+ rvaai.getNumAnnotations());
		
		if (rvaai.getNumAnnotations() > 0) {
			Annotation[] annotations = new Annotation[rvaai.getNumAnnotations()];
			int i = 0;
			while (i < rvaai.getNumAnnotations()) {
				Annotation annotation = convertToAnnotation(byteBuffer);
				annotations[i] = annotation;
				i++;
			}
			rvaai.setAnnotations(annotations);
		}
		
		return rvaai;
	};
	
	private RuntimeVisibleParameterAnnotationsAttributeInfo convertToRuntimeVisibleParameterAnnotations(ByteBuffer byteBuffer,int attributeNameIndex,int attributeLength) {
		
		RuntimeVisibleParameterAnnotationsAttributeInfo rvpaai = new RuntimeVisibleParameterAnnotationsAttributeInfo();
		rvpaai.setAttributeNameIndex(attributeNameIndex);
		logger.debug("\t\t\tAttributeNameIndex : "+ rvpaai.getAttributeNameIndex());
		rvpaai.setAttributeLength(attributeLength);
		logger.debug("\t\t\tAttributeLength : "+ rvpaai.getAttributeLength());
		rvpaai.setNumParameters(byteBuffer.get() & 0xff);
		logger.debug("\t\t\tNumParameters : "+ rvpaai.getNumParameters());

		
		if (rvpaai.getNumParameters() > 0) {
			int i = 0;
			ParameterAnnotation[] parameterAnnotations = new ParameterAnnotation[rvpaai.getNumParameters()];
			while ( i < rvpaai.getNumParameters()) {
				ParameterAnnotation pa = new ParameterAnnotation();
				pa.setNumAnnotations(byteBuffer.getShort());
				Annotation[] annotations = new Annotation[pa.getNumAnnotations()];
				int j = 0;
				while (j < pa.getNumAnnotations()) {
					Annotation annotation = convertToAnnotation(byteBuffer);
					annotations[j] = annotation;
					j++;
				}
				pa.setAnnotations(annotations);
				parameterAnnotations[i] = pa;
				i++;
			}
			rvpaai.setParameterAnnotations(parameterAnnotations);
		}
		
		return rvpaai;
	};
	
	
	private Annotation convertToAnnotation(ByteBuffer byteBuffer) {
		Annotation annotation = new Annotation();
		annotation.setTypeIndex(byteBuffer.getShort());
		logger.debug("\t\t\t\tTypeIndex : "+ annotation.getTypeIndex());
		annotation.setNumElementValuePairs(byteBuffer.getShort());
		logger.debug("\t\t\t\tNumElementValuePairs : "+ annotation.getNumElementValuePairs());
		if (annotation.getNumElementValuePairs() > 0) {
			ElementValuePair[] elementValuePairs = new ElementValuePair[annotation.getNumElementValuePairs()];
			int i = 0;
			while (i < annotation.getNumElementValuePairs()){
				ElementValuePair elementValuePair = new ElementValuePair();
				elementValuePair.setElementNameIndex(byteBuffer.getShort());
				logger.debug("\t\t\t\t\tElementNameIndex[" + i + "] : "+ elementValuePair.getElementNameIndex());
				ElementValue elementValue = convertToElementValue(byteBuffer);
				elementValuePair.setElementNameValue(elementValue);
				elementValuePairs[i] = elementValuePair;
				i++;
			}
			annotation.setElementValuePairs(elementValuePairs);
		}
		return annotation;
	}
	
	private ElementValue convertToElementValue(ByteBuffer byteBuffer) {
		byte[] tagByte = {byteBuffer.get()};
		String tag = new String(tagByte);
//		elementValuePair.setElementNameValue(byteBuffer.getShort());
		logger.debug("\t\t\t\t\tElementNameValue.tag : "+ tag);
		ElementValue elementValue = null;
		if (constValueIndex.contains(tag)) {
			ConstValue constValue = new ConstValue();
			constValue.setTag(tag);
			constValue.setConstValueIndex(byteBuffer.getShort());
			elementValue = constValue;
		} else if ("e".equals(tag)) {
			EnumConstValue enumConstValue = new EnumConstValue();
			enumConstValue.setTag(tag);
			enumConstValue.setTypeNameIndex(byteBuffer.getShort());
			enumConstValue.setConstNameIndex(byteBuffer.getShort());
			elementValue = enumConstValue;
		} else if ("c".equals(tag)) {
			ClassInfoValue classInfoValue = new ClassInfoValue();
			classInfoValue.setTag(tag);
			classInfoValue.setClassInfoIndex(byteBuffer.getShort());
			elementValue = classInfoValue;
		} else if ("@".equals(tag)) {
			AnnotationValue annotationValue = new AnnotationValue();
			annotationValue.setTag(tag);
			annotationValue.setAnnotation(convertToAnnotation(byteBuffer));
		} else if ("[".equals(tag)) {
			ArrayValue arrayValue = new ArrayValue();
			arrayValue.setTag(tag);
			arrayValue.setNumValues(byteBuffer.getShort());
			if (arrayValue.getNumValues() > 0) {
				int i = 0;
				ElementValue[] elementValues = new ElementValue[arrayValue.getNumValues()];
				while (i < arrayValue.getNumValues()) {
					ElementValue elementValue2 = convertToElementValue(byteBuffer);
					elementValues[i] = elementValue2;
					i++;
				}
				arrayValue.setElementValues(elementValues);
			}
		} else {
			throw new IllegalStateException("tag = " + tag);
		}
		
		return elementValue;
	}
	
	private RuntimeInvisibleAnnotationsAttributeInfo convertToRuntimeInvisibleAnnotations(ByteBuffer byteBuffer,int attributeNameIndex,int attributeLength) {
		RuntimeInvisibleAnnotationsAttributeInfo riaai = new RuntimeInvisibleAnnotationsAttributeInfo();
		riaai.setAttributeNameIndex(attributeNameIndex);
		logger.debug("\t\t\tAttributeNameIndex : "+ riaai.getAttributeNameIndex());
		riaai.setAttributeLength(attributeLength);
		logger.debug("\t\t\tAttributeLength : "+ riaai.getAttributeLength());
		riaai.setNumAnnotations(byteBuffer.getShort());
		logger.debug("\t\t\tNumAnnotations : "+ riaai.getNumAnnotations());
		
		if (riaai.getNumAnnotations() > 0) {
			Annotation[] annotations = new Annotation[riaai.getNumAnnotations()];
			int i = 0;
			while (i < riaai.getNumAnnotations()) {
				Annotation annotation = convertToAnnotation(byteBuffer);
				annotations[i] = annotation;
				i++;
			}
			riaai.setAnnotations(annotations);
		}
		
		return riaai;
	};

	private RuntimeVisibleParameterAnnotationsAttributeInfo convertToRuntimeVisibleParameterAnnotations(
			ByteBuffer byteBuffer) {
		return null;
	};

	private RuntimeInvisibleParameterAnnotationsAttributeInfo convertToRuntimeInvisibleParameterAnnotations(
			ByteBuffer byteBuffer) {
		return null;
	};

	private AnnotationDefaultAttributeInfo convertToAnnotationDefault(
			ByteBuffer byteBuffer) {
		return null;
	};

	private BootstrapMethodsAttributeInfo convertToBootstrapMethods(
			ByteBuffer byteBuffer) {
		return null;
	};

	private ExceptionTableInfo convertToExceptionTableInfo(ByteBuffer byteBuffer) {
		ExceptionTableInfo eti = new ExceptionTableInfo();
		eti.setStartPc(byteBuffer.getShort()); // start_pc(2byte)
		eti.setEndPc(byteBuffer.getShort()); // end_pc(2byte)
		eti.setHandlerPc(byteBuffer.getShort()); // handler_pc(2byte)
		eti.setCatchType(byteBuffer.getShort()); // catch_type(2byte)
		return eti;
	}
}
