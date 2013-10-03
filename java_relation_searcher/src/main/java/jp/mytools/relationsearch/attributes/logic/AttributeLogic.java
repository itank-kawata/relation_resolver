package jp.mytools.relationsearch.attributes.logic;

import java.nio.ByteBuffer;
import java.util.Map;

import jp.mytools.relationsearch.attributes.beans.AnnotationDefaultAttributeInfo;
import jp.mytools.relationsearch.attributes.beans.AppendFrame;
import jp.mytools.relationsearch.attributes.beans.Attribute;
import jp.mytools.relationsearch.attributes.beans.BootstrapMethodsAttributeInfo;
import jp.mytools.relationsearch.attributes.beans.ChopFrame;
import jp.mytools.relationsearch.attributes.beans.CodeAttributeInfo;
import jp.mytools.relationsearch.attributes.beans.ConstantValueAttributeInfo;
import jp.mytools.relationsearch.attributes.beans.DeprecatedAttributeInfo;
import jp.mytools.relationsearch.attributes.beans.EnclosingMethodAttributeInfo;
import jp.mytools.relationsearch.attributes.beans.ExceptionTableInfo;
import jp.mytools.relationsearch.attributes.beans.ExceptionsAttributeInfo;
import jp.mytools.relationsearch.attributes.beans.FullFrame;
import jp.mytools.relationsearch.attributes.beans.GeneralVariableInfo;
import jp.mytools.relationsearch.attributes.beans.InnerClasses;
import jp.mytools.relationsearch.attributes.beans.InnerClassesAttributeInfo;
import jp.mytools.relationsearch.attributes.beans.LineNumberTable;
import jp.mytools.relationsearch.attributes.beans.LineNumberTableAttributeInfo;
import jp.mytools.relationsearch.attributes.beans.LocalVariableTable;
import jp.mytools.relationsearch.attributes.beans.LocalVariableTableAttributeInfo;
import jp.mytools.relationsearch.attributes.beans.LocalVariableTypeTableAttributeInfo;
import jp.mytools.relationsearch.attributes.beans.ObjectVariableInfo;
import jp.mytools.relationsearch.attributes.beans.RuntimeInvisibleAnnotationsAttributeInfo;
import jp.mytools.relationsearch.attributes.beans.RuntimeInvisibleParameterAnnotationsAttributeInfo;
import jp.mytools.relationsearch.attributes.beans.RuntimeVisibleAnnotationsAttributeInfo;
import jp.mytools.relationsearch.attributes.beans.RuntimeVisibleParameterAnnotationsAttributeInfo;
import jp.mytools.relationsearch.attributes.beans.SameFrame;
import jp.mytools.relationsearch.attributes.beans.SameFrameExtended;
import jp.mytools.relationsearch.attributes.beans.SameLocals1StackItemFrame;
import jp.mytools.relationsearch.attributes.beans.SameLocals1StackItemFrameExtended;
import jp.mytools.relationsearch.attributes.beans.SignatureAttributeInfo;
import jp.mytools.relationsearch.attributes.beans.SourceDebugExtensionAttributeInfo;
import jp.mytools.relationsearch.attributes.beans.SourceFileAttributeInfo;
import jp.mytools.relationsearch.attributes.beans.StackMapFrame;
import jp.mytools.relationsearch.attributes.beans.StackMapTableAttributeInfo;
import jp.mytools.relationsearch.attributes.beans.SyntheticAttributeInfo;
import jp.mytools.relationsearch.attributes.beans.UninitializedVariableInfo;
import jp.mytools.relationsearch.attributes.beans.VerificationTypeInfo;
import jp.mytools.relationsearch.attributes.enums.AttributeType;
import jp.mytools.relationsearch.classfile.exceptions.ClassFileFormatException;
import jp.mytools.relationsearch.constantpool.beans.ConstantPool;
import jp.mytools.relationsearch.constantpool.beans.Utf8ConstantPool;
import jp.mytools.relationsearch.opcode.logic.OpcodeLogic;

public class AttributeLogic {

	private OpcodeLogic opcodeLogic = new OpcodeLogic();

	public Attribute convertToAttribute(ByteBuffer byteBuffer,
			Map<Integer, ConstantPool> cpMap) throws ClassFileFormatException {

		System.out.println("\tAttribute------------------------");
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
		System.out.println("\t\ttype : " + type.getName());
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
		default:
			System.out.println("undefined type : " + type.getName());
			throw new IllegalStateException("undefined type");
		}

		return attribute;
	}

	private StackMapTableAttributeInfo convertToStackMapTableAttributeInfo(
			ByteBuffer byteBuffer, int attributeNameIndex, int attributeLength) {
		StackMapTableAttributeInfo smtai = new StackMapTableAttributeInfo();
		smtai.setAttributeNameIndex(attributeNameIndex);
		System.out.println("\t\t\tAttributeNameIndex : "
				+ smtai.getAttributeNameIndex());
		smtai.setAttributeLength(attributeLength);
		System.out.println("\t\t\tAttributeLength : "
				+ smtai.getAttributeLength());
		smtai.setNumberOfEntries(byteBuffer.getShort());
		System.out.println("\t\t\tNumberOfEntries : "
				+ smtai.getNumberOfEntries());

		if (smtai.getNumberOfEntries() > 0) {
			int i = 0;
			StackMapFrame[] frames = new StackMapFrame[smtai
					.getNumberOfEntries()];
			while (i < smtai.getNumberOfEntries()) {
				StackMapFrame smFrame = null;
				int frameType = (byteBuffer.get() & 0xff);
				System.out.println("\t\t\t\tframeType : " + frameType);
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
					System.out.println("\t\t\tOffsetDelta : " + cf.getOffsetDelta());
					smFrame = cf;
				} else if (frameType == 251) {
					SameFrameExtended sfe = new SameFrameExtended();
					sfe.setFrameType(frameType);
					sfe.setOffsetDelta(byteBuffer.getShort());
					System.out.println("\t\t\tOffsetDelta : " + sfe.getOffsetDelta());
					smFrame = sfe;
				} else if (frameType >= 252 && frameType <= 254) {
					AppendFrame af = new AppendFrame();
					af.setFrameType(frameType);
					af.setOffsetDelta(byteBuffer.getShort());
					System.out.println("\t\t\tOffsetDelta : " + af.getOffsetDelta());
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
					System.out.println("\t\t\tFrameType : FullFrame (" + ff.getFrameType() + ")");
					ff.setOffsetDelta(byteBuffer.getShort());
					System.out.println("\t\t\tOffsetDelta : " + ff.getOffsetDelta());
					ff.setNumberOfLocals(byteBuffer.getShort());
					System.out.println("\t\t\tNumberOfLocals : " + ff.getNumberOfLocals());
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
					System.out.println("undefined frameType : " + frameType);
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
		System.out.println("\t\t\t\t\t\ttag : " + tag);
		VerificationTypeInfo info = null;
		if (tag >= 0 && tag <= 6) {
			GeneralVariableInfo gvi = new GeneralVariableInfo();
			gvi.setTag(tag);
			info = gvi;
		} else if (tag == 7) {
			ObjectVariableInfo ovi = new ObjectVariableInfo();
			ovi.setTag(tag);
			ovi.setCpoolIndex(byteBuffer.getShort());
			System.out.println("\t\t\t\t\t\tCpoolIndex : " + ovi.getCpoolIndex());
			info = ovi;
		} else if (tag == 8) {
			UninitializedVariableInfo uvi = new UninitializedVariableInfo();
			uvi.setTag(tag);
			uvi.setOffset(byteBuffer.getShort());
			System.out.println("\t\t\t\t\t\tCOffset : " + uvi.getOffset());
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
		icai.setAttributeLength(attributeLength);
		icai.setNumberOfClasses(byteBuffer.getShort());
		if (icai.getNumberOfClasses() > 0) {
			int i = 0;
			InnerClasses[] classes = new InnerClasses[icai.getNumberOfClasses()];
			while (i < icai.getNumberOfClasses()) {
				InnerClasses clazz = new InnerClasses();
				clazz.setInnerClassInfoIndex(byteBuffer.getShort());
				clazz.setOuterClassInfoIndex(byteBuffer.getShort());
				clazz.setInnerNameIndex(byteBuffer.getShort());
				clazz.setInnerClassAccessFlags(byteBuffer.getShort());
				classes[i] = clazz;
			}
			icai.setClasses(classes);
		}

		return icai;
	}

	private ConstantValueAttributeInfo convertToConstantValue(
			ByteBuffer byteBuffer, int attributeNameIndex, int attributeLength) {
		ConstantValueAttributeInfo cvai = new ConstantValueAttributeInfo();
		cvai.setAttributeNameIndex(attributeNameIndex);
		System.out.println("\t\t\tAttributeNameIndex : "
				+ cvai.getAttributeNameIndex());
		cvai.setAttributeLength(attributeLength);
		System.out.println("\t\t\tAttributeLength : "
				+ cvai.getAttributeLength());
		cvai.setConstantvalueIndex(byteBuffer.getShort()); // constantvalue_index(2byte)
		System.out.println("\t\t\tConstantvalueIndex : "
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
		System.out.println("\t\t\tMaxStack : " + cai.getMaxStack());
		cai.setMaxLocals(byteBuffer.getShort()); // max_locals(2byte)
		System.out.println("\t\t\tMaxLocals : " + cai.getMaxStack());
		cai.setCodeLength(byteBuffer.getInt()); // code_length(4byte)
		System.out.println("\t\t\tCodeLength : " + cai.getMaxStack());
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
		System.out.println("\t\t\tAttributeNameIndex : "
				+ eai.getAttributeNameIndex());
		eai.setAttributeLength(attributeLength);
		System.out.println("\t\t\tAttributeLength : "
				+ eai.getAttributeLength());
		eai.setNumberOfExceptions(byteBuffer.getShort());
		System.out.println("\t\t\tNumberOfExceptions : "
				+ eai.getNumberOfExceptions());
		int i = 0;
		int[] exceptionIndexTable = new int[eai.getNumberOfExceptions()];
		while (i < eai.getNumberOfExceptions()) {
			exceptionIndexTable[i] = byteBuffer.getShort();
			System.out.println("\t\t\texceptionIndexTable[" + i + "] : "
					+ exceptionIndexTable[i]);
			i++;
		}
		eai.setExceptionIndexTable(exceptionIndexTable);
		return eai;
	}

	private EnclosingMethodAttributeInfo convertToEnclosingMethod(
			ByteBuffer byteBuffer) {
		return null;
	}

	private SignatureAttributeInfo convertToSignature(ByteBuffer byteBuffer) {
		return null;
	}

	private SourceFileAttributeInfo convertToSourceFile(ByteBuffer byteBuffer,
			int attributeNameIndex, int attributeLength) {
		SourceFileAttributeInfo sfai = new SourceFileAttributeInfo();
		sfai.setAttributeNameIndex(attributeNameIndex);
		sfai.setAttributeLength(attributeLength);
		sfai.setSourcefileIndex(byteBuffer.getShort());
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
		System.out.println("\t\t\tAttributeNameIndex : "
				+ lnta.getAttributeNameIndex());
		lnta.setAttributeLength(attributeLength);
		System.out.println("\t\t\tAttributeLength : "
				+ lnta.getAttributeLength());
		lnta.setLineNumberTableLength(byteBuffer.getShort());
		System.out.println("\t\t\tLineNumberTableLength : "
				+ lnta.getLineNumberTableLength());
		if (lnta.getLineNumberTableLength() > 0) {
			LineNumberTable[] lineNumberTables = new LineNumberTable[lnta
					.getLineNumberTableLength()];
			int i = 0;
			while (i < lnta.getLineNumberTableLength()) {
				LineNumberTable lineNumberTable = new LineNumberTable();
				lineNumberTable.setStartPc(byteBuffer.getShort());
				System.out.println("\t\t\t\tStartPc : "
						+ lineNumberTable.getStartPc());
				lineNumberTable.setLineNumber(byteBuffer.getShort());
				System.out.println("\t\t\t\tLineNumber : "
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
		lvtai.setAttributeLength(attributeLength);
		System.out.println("\t\t\tAttributeLength : "
				+ lvtai.getAttributeLength());
		lvtai.setLocalVariableTableLength(byteBuffer.getShort());
		System.out.println("\t\t\tLocalVariableTableLength : "
				+ lvtai.getLocalVariableTableLength());
		if (lvtai.getLocalVariableTableLength() > 0) {
			LocalVariableTable[] localVariableTables = new LocalVariableTable[lvtai
					.getLocalVariableTableLength()];
			int i = 0;
			while (i < lvtai.getLocalVariableTableLength()) {
				LocalVariableTable localVariableTable = new LocalVariableTable();
				localVariableTable.setStartPc(byteBuffer.getShort());
				System.out.println("\t\t\t\tStartPc : "
						+ localVariableTable.getStartPc());
				localVariableTable.setLength(byteBuffer.getShort());
				System.out.println("\t\t\t\tLength : "
						+ localVariableTable.getLength());
				localVariableTable.setNameIndex(byteBuffer.getShort());
				System.out.println("\t\t\t\tNameIndex : "
						+ localVariableTable.getNameIndex());
				localVariableTable.setDescriptorIndex(byteBuffer.getShort());
				System.out.println("\t\t\t\tDescriptorIndex : "
						+ localVariableTable.getDescriptorIndex());
				localVariableTable.setIndex(byteBuffer.getShort());
				System.out.println("\t\t\t\tIndex : "
						+ localVariableTable.getIndex());
				localVariableTables[i] = localVariableTable;
				i++;
			}
			lvtai.setLocalVariableTables(localVariableTables);
		}
		return lvtai;
	};

	private LocalVariableTypeTableAttributeInfo convertToLocalVariableTypeTable(
			ByteBuffer byteBuffer) {
		return null;
	};

	private DeprecatedAttributeInfo convertToDeprecated(int attributeNameIndex,
			int attributeLength) {
		DeprecatedAttributeInfo dai = new DeprecatedAttributeInfo();
		dai.setAttributeNameIndex(attributeNameIndex);
		dai.setAttributeLength(attributeLength);
		return dai;
	};

	private RuntimeVisibleAnnotationsAttributeInfo convertToRuntimeVisibleAnnotations(
			ByteBuffer byteBuffer) {
		return null;
	};

	private RuntimeInvisibleAnnotationsAttributeInfo convertToRuntimeInvisibleAnnotations(
			ByteBuffer byteBuffer) {
		return null;
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
