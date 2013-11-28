package jp.mytools.disassemble.classfile.logic;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.mytools.disassemble.attributes.beans.Attribute;
import jp.mytools.disassemble.attributes.beans.AttributeInfo;
import jp.mytools.disassemble.attributes.logic.AttributeLogic;
import jp.mytools.disassemble.classfile.beans.ClassFileInfo;
import jp.mytools.disassemble.classfile.exceptions.ClassFileFormatException;
import jp.mytools.disassemble.constantpool.beans.ClassConstantPool;
import jp.mytools.disassemble.constantpool.beans.ConstantPool;
import jp.mytools.disassemble.constantpool.beans.DoubleConstantPool;
import jp.mytools.disassemble.constantpool.beans.FieldrefConstantPool;
import jp.mytools.disassemble.constantpool.beans.FloatConstantPool;
import jp.mytools.disassemble.constantpool.beans.IntegerConstantPool;
import jp.mytools.disassemble.constantpool.beans.IntegerFloatConstantPool;
import jp.mytools.disassemble.constantpool.beans.InterfaceMethodrefConstantPool;
import jp.mytools.disassemble.constantpool.beans.LongConstantPool;
import jp.mytools.disassemble.constantpool.beans.LongDoubleConstantPool;
import jp.mytools.disassemble.constantpool.beans.MethodrefConstantPool;
import jp.mytools.disassemble.constantpool.beans.NameAndTypeConstantPool;
import jp.mytools.disassemble.constantpool.beans.ReferenceConstantPool;
import jp.mytools.disassemble.constantpool.beans.StringConstantPool;
import jp.mytools.disassemble.constantpool.beans.Utf8ConstantPool;
import jp.mytools.disassemble.constantpool.enums.ConstantPoolType;
import jp.mytools.disassemble.constantpool.exceptions.ConstantPoolException;
import jp.mytools.disassemble.fields.beans.FieldInfo;
import jp.mytools.disassemble.methods.beans.MethodInfo;

public class ClassFileLogic {

	private static Logger logger = LoggerFactory.getLogger(ClassFileLogic.class);
	
	public ClassFileInfo convert(byte[] bytes) throws ConstantPoolException, ClassFileFormatException {
		
		if (ArrayUtils.isEmpty(bytes)) return null;
		
		ClassFileInfo result = new ClassFileInfo();
		ByteBuffer byteBuff = ByteBuffer.wrap(bytes);
		// magic (4byte)
		result.setMagic(byteBuff.getInt());
		logger.debug("magic : " + result.getMagic());
		// compile minor version (2byte)
		result.setMinorVersion(byteBuff.getShort());
		logger.debug("minorVersion : " + result.getMinorVersion());
		// compile major version  (2byte)
		result.setMajorVersion(byteBuff.getShort());
		logger.debug("majorVersion : " + result.getMajorVersion());
		// constant pool's count  (2byte)
		result.setConstantPoolCount(byteBuff.getShort() - 1);
		logger.debug("constantPoolCount : " + result.getConstantPoolCount());

		int processCnt = 0;
		Map<Integer,ConstantPool> cpMap = new TreeMap<>();
		while (processCnt < result.getConstantPoolCount()) {
			// tag
			int cpTag = byteBuff.get();
			// constant pool type
			ConstantPoolType cpType = ConstantPoolType.getByTag(cpTag);
			//logger.debug((processCnt + 1) + " : constantPool(" + cpTag + ") : " + cpType);
			ConstantPool cp = null;
			boolean isLongOrDouble = false;
			switch (cpType) {
			case CLASS:
				cp = convertToClassInfo(byteBuff);
				break;
			case DOUBLE:
				cp = convertToLongDoubleInfo(byteBuff, cpType);
				isLongOrDouble = true;
				break;
			case FIELDREF:
				cp = convertToReferenceInfo(byteBuff, cpType);
				break;
			case FLOAT:
				cp = convertToIntegerFloatInfo(byteBuff, cpType);
				break;
			case INTEGER:
				cp = convertToIntegerFloatInfo(byteBuff, cpType);
				break;
			case INTERFACEMETHODREF:
				cp = convertToReferenceInfo(byteBuff, cpType);
				break;
			case LONG:
				cp = convertToLongDoubleInfo(byteBuff, cpType);
				isLongOrDouble = true;
				break;
			case METHODREF:
				cp = convertToReferenceInfo(byteBuff, cpType);
				break;
			case NAMEANDTYPE:
				cp = convertToNameAndTypeInfo(byteBuff);
				break;
			case STRING:
				cp = convertToStringInfo(byteBuff);
				break;
			case UTF8:
				cp = convertToUtf8Info(byteBuff);
				break;
			default:
				break;
			}
			
			if (cp == null) {
				throw new ConstantPoolException("Illeagal tag. tag = " + cpTag);
			}
			cpMap.put(processCnt + 1,cp);
			processCnt++;
			if (isLongOrDouble) {
				processCnt++;	// next index is n + 2
			}
		}
		
		result.setConstantPoolMap(cpMap);
		for (Entry<Integer, ConstantPool> cpEntry : cpMap.entrySet()) {
			logger.debug(cpEntry.getKey() + ":" + cpEntry.getValue().toString());
		}
		// access_flag(2byte)
		result.setAccessFlag(byteBuff.getShort());
		logger.debug("accessFlag : " + result.getAccessFlag());
		// this_class(2byte)
		result.setThisClass(byteBuff.getShort());
		logger.debug("thisClass : " + result.getThisClass());
		// super_class(2byte)
		result.setSuperClass(byteBuff.getShort());
		logger.debug("superClass : " + result.getSuperClass());
		// interfaces_count(2byte)
		result.setInterfacesCount(byteBuff.getShort());
		logger.debug("interfacesCount : " + result.getInterfacesCount());
		if (result.getInterfacesCount() > 0) {
			int[] interfaces = new int[result.getInterfacesCount()];
			int i = 0;
			while (i < result.getInterfacesCount()) {
				interfaces[i] = byteBuff.getShort();
				logger.debug("\tßinterfaces[" + i + "] : " + interfaces[i]);
				i++;
			}
			// interfaces(2byte * interfaces_count)
			result.setInterfaces(interfaces);
		}
		// fields_count(2byte)
		result.setFieldsCount(byteBuff.getShort());
		logger.debug("fieldsCount : " + result.getFieldsCount());
		if (result.getFieldsCount() > 0) {
			FieldInfo[] fields = new FieldInfo[result.getFieldsCount()];
			int i = 0;
			while (i < result.getFieldsCount()) {
				fields[i] = convertToFieldInfo(byteBuff,cpMap);
				i++;
			}
			result.setFields(fields);
		}
		
		result.setMethodsCount(byteBuff.getShort());	// methods_count(2byte)
		if (result.getMethodsCount() > 0) {
			MethodInfo[] methods = new MethodInfo[result.getMethodsCount()];
			int i = 0;
			while (i < result.getMethodsCount()) {
				methods[i] = convertToMethodInfo(byteBuff, cpMap);
				i++;
			}
			result.setMethods(methods);
		}
		
		result.setAttributesCount(byteBuff.getShort());	// attributes_count(2byte)
		logger.debug("attributesCount : " + result.getAttributesCount());
		if (result.getAttributesCount() > 0) {
			Attribute[] attributes = new Attribute[result.getAttributesCount()];
			int i = 0;
			AttributeLogic attributeLogic = new AttributeLogic();
			while (i < result.getAttributesCount()) {
				
				attributes[i] = attributeLogic.convertToAttribute(byteBuff, cpMap);
				i++;
			}
			result.setAttributes(attributes);
		}
		
		logger.debug("position/limit : " + byteBuff.position() + "/" + byteBuff.limit());
		
		return result;
	}
	
	private MethodInfo convertToMethodInfo(ByteBuffer byteBuffer , Map<Integer,ConstantPool> cpMap) throws ClassFileFormatException {
		logger.debug("[MethodInfo]------------------------");
		MethodInfo methodInfo = new MethodInfo();
		methodInfo.setAccessFlags(byteBuffer.getShort());	// access_flags(2byte)
		methodInfo.setNameIndex(byteBuffer.getShort());
		logger.debug("\tNameIndex : " + cpMap.get(methodInfo.getNameIndex()).toString());
		methodInfo.setDescriptorIndex(byteBuffer.getShort());
		logger.debug("\tDescriptorIndex : " + cpMap.get(methodInfo.getDescriptorIndex()).toString());
		methodInfo.setAttributesCount(byteBuffer.getShort());
		logger.debug("\tAttributeCount : " + methodInfo.getAttributesCount());
		if (methodInfo.getAttributesCount() > 0) {
			Attribute[] attributes = new AttributeInfo[methodInfo.getAttributesCount()];
			int i = 0;
			AttributeLogic attributeLogic = new AttributeLogic();
			while (i < methodInfo.getAttributesCount()) {
				attributes[i] = attributeLogic.convertToAttribute(byteBuffer, cpMap);
				i++;
			}
			methodInfo.setAttributes(attributes);
		}
		return methodInfo;
	}
	
	private FieldInfo convertToFieldInfo(ByteBuffer byteBuffer , Map<Integer,ConstantPool> cpMap) throws ClassFileFormatException {
		logger.debug("[FieldInfo]------------------------");
		FieldInfo fieldInfo = new FieldInfo();
		fieldInfo.setAccessFlags(byteBuffer.getShort());		// access_flags(2byte)
		fieldInfo.setNameIndex(byteBuffer.getShort());			// name_index(2byte)
		logger.debug("\tNameIndex : " + cpMap.get(fieldInfo.getNameIndex()).toString());
		fieldInfo.setDescriptorIndex(byteBuffer.getShort());	// descriptorIndex(2byte)
		logger.debug("\tDescriptorIndex : " + cpMap.get(fieldInfo.getDescriptorIndex()).toString());
		fieldInfo.setAttributeCount(byteBuffer.getShort());		// attributeCount(2byte)
		logger.debug("\tAttributeCount : " + fieldInfo.getAttributeCount());
		AttributeLogic attributeLogic = new AttributeLogic();
		if (fieldInfo.getAttributeCount() > 0) {
			Attribute[] attributes = new Attribute[fieldInfo.getAttributeCount()];
			int i = 0;
			while (i < fieldInfo.getAttributeCount()) {
				attributes[i] = attributeLogic.convertToAttribute(byteBuffer,cpMap);
				i++;
			}
			fieldInfo.setAttributes(attributes);
		}
		return fieldInfo;
	}
	
	/**
	 * Class Constant Pool Structure
	 * @param bytes
	 * @param startIndex
	 * @return
	 */
	private ClassConstantPool convertToClassInfo(ByteBuffer byteBuffer) {
		ClassConstantPool cp = new ClassConstantPool();
		int nameIndex = byteBuffer.getShort();	// name_index(2byte)
		cp.setNameIndex(nameIndex);
		return cp;
	}
	
	/**
	 * Long or Double Constant Pool Structure
	 * @param bytes
	 * @param startIndex
	 * @param cpType
	 * @return
	 */
	private LongDoubleConstantPool convertToLongDoubleInfo(ByteBuffer byteBuffer, ConstantPoolType cpType) {
		LongDoubleConstantPool cp = null;
		switch (cpType) {
		case LONG:
			cp = new LongConstantPool();
			break;
		case DOUBLE:
			cp = new DoubleConstantPool();
			break;
		default:
			return null;
		}
		byte[] highBytes = new byte[4];
		int i = 0;
		while (i < 4) {
			highBytes[i] = byteBuffer.get();
			i++;
		}
		byte[] lowBytes = new byte[4];
		i = 0;
		while (i < 4) {
			lowBytes[i] = byteBuffer.get();
			i++;
		}
		cp.setHighBytes(highBytes);	//  4byte
		cp.setLowBytes(lowBytes);
		return cp;
	}
	
	/**
	 * Integer or Float Constant Pool Structure
	 * @param bytes
	 * @param startIndex
	 * @param cpType
	 * @return
	 */
	private IntegerFloatConstantPool convertToIntegerFloatInfo(ByteBuffer byteBuffer, ConstantPoolType cpType) {
		IntegerFloatConstantPool cp = null;
		switch (cpType) {
		case INTEGER:
			cp = new IntegerConstantPool();
			break;
		case FLOAT:
			cp = new FloatConstantPool();
			break;
		default:
			return null;
		}
		
		byte[] bytes = new byte[4];
		int i = 0;
		while (i < 4) {
			bytes[i] = byteBuffer.get();
			i++;
		}
		
		cp.setBytes(bytes);
		return cp;
	}
	
	/**
	 * Fieldref or Interfaceref or Methodref Constant Pool Structure
	 * @param bytes
	 * @param startIndex
	 * @param cpType
	 * @return
	 */
	private ReferenceConstantPool convertToReferenceInfo(ByteBuffer byteBuffer , ConstantPoolType cpType) {
		ReferenceConstantPool cp = null;
		switch (cpType) {
		case FIELDREF:
			cp = new FieldrefConstantPool();
			break;
		case INTERFACEMETHODREF:
			cp = new InterfaceMethodrefConstantPool();
			break;
		case METHODREF:
			cp = new MethodrefConstantPool();
			break;
		default:
			return null;
		}
		
		cp.setClassIndex(byteBuffer.getShort());
		cp.setNameAndTypeIndex(byteBuffer.getShort());
		return cp;
	}
	
	/**
	 * NameAndType Constant Pool Structure
	 * @param bytes
	 * @param startIndex
	 * @return
	 */
	private NameAndTypeConstantPool convertToNameAndTypeInfo(ByteBuffer byteBuffer) {
		NameAndTypeConstantPool cp = new NameAndTypeConstantPool();
		cp.setNameIndex(byteBuffer.getShort());
		cp.setDescriptorIndex(byteBuffer.getShort());
		return cp;
	}
	
	/**
	 * String Constant Pool Structure
	 * @param bytes
	 * @param startIndex
	 * @return
	 */
	private StringConstantPool convertToStringInfo(ByteBuffer byteBuffer) {
		StringConstantPool cp = new StringConstantPool();
		cp.setStringIndex(byteBuffer.getShort());
		return cp;
	}

	/**
	 * UTF8 Constant Pool Structure
	 * @param bytes
	 * @param startIndex
	 * @return
	 */
	private Utf8ConstantPool convertToUtf8Info(ByteBuffer byteBuffer) {
		Utf8ConstantPool cp = new Utf8ConstantPool();
		cp.setLength(byteBuffer.getShort());
		byte[] bytes = new byte[cp.getLength()];
		int i = 0;
		while (i < cp.getLength()) {
			bytes[i] = byteBuffer.get();
			i++;
		}
		cp.setBytes(bytes);
		return cp;
	}
	

}
