package jp.mytools.relationsearch.classfile.logic;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang.ArrayUtils;

import jp.mytools.relationsearch.attributes.beans.Attribute;
import jp.mytools.relationsearch.attributes.beans.AttributeInfo;
import jp.mytools.relationsearch.attributes.logic.AttributeLogic;
import jp.mytools.relationsearch.classfile.beans.ClassFileInfo;
import jp.mytools.relationsearch.classfile.exceptions.ClassFileFormatException;
import jp.mytools.relationsearch.constantpool.beans.ClassConstantPool;
import jp.mytools.relationsearch.constantpool.beans.ConstantPool;
import jp.mytools.relationsearch.constantpool.beans.DoubleConstantPool;
import jp.mytools.relationsearch.constantpool.beans.FieldrefConstantPool;
import jp.mytools.relationsearch.constantpool.beans.FloatConstantPool;
import jp.mytools.relationsearch.constantpool.beans.IntegerConstantPool;
import jp.mytools.relationsearch.constantpool.beans.IntegerFloatConstantPool;
import jp.mytools.relationsearch.constantpool.beans.InterfaceMethodrefConstantPool;
import jp.mytools.relationsearch.constantpool.beans.LongConstantPool;
import jp.mytools.relationsearch.constantpool.beans.LongDoubleConstantPool;
import jp.mytools.relationsearch.constantpool.beans.MethodrefConstantPool;
import jp.mytools.relationsearch.constantpool.beans.NameAndTypeConstantPool;
import jp.mytools.relationsearch.constantpool.beans.ReferenceConstantPool;
import jp.mytools.relationsearch.constantpool.beans.StringConstantPool;
import jp.mytools.relationsearch.constantpool.beans.Utf8ConstantPool;
import jp.mytools.relationsearch.constantpool.enums.ConstantPoolType;
import jp.mytools.relationsearch.constantpool.exceptions.ConstantPoolException;
import jp.mytools.relationsearch.fields.beans.FieldInfo;
import jp.mytools.relationsearch.methods.beans.MethodInfo;

public class ClassFileLogic {

	public ClassFileInfo convert(byte[] bytes) throws ConstantPoolException, ClassFileFormatException {
		
		if (ArrayUtils.isEmpty(bytes)) return null;
		
		ClassFileInfo result = new ClassFileInfo();
		ByteBuffer byteBuff = ByteBuffer.wrap(bytes);
		// magic (4byte)
		result.setMagic(byteBuff.getInt());
		System.out.println("magic : " + result.getMagic());
		// compile minor version (2byte)
		result.setMinorVersion(byteBuff.getShort());
		System.out.println("minorVersion : " + result.getMinorVersion());
		// compile major version  (2byte)
		result.setMajorVersion(byteBuff.getShort());
		System.out.println("majorVersion : " + result.getMajorVersion());
		// constant pool's count  (2byte)
		result.setConstantPoolCount(byteBuff.getShort() - 1);
		System.out.println("constantPoolCount : " + result.getConstantPoolCount());

		int processCnt = 0;
		Map<Integer,ConstantPool> cpMap = new TreeMap<>();
		while (processCnt < result.getConstantPoolCount()) {
			// tag
			int cpTag = byteBuff.get();
			// constant pool type
			ConstantPoolType cpType = ConstantPoolType.getByTag(cpTag);
			//System.out.println((processCnt + 1) + " : constantPool(" + cpTag + ") : " + cpType);
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
			System.out.println(cpEntry.getKey() + ":" + cpEntry.getValue().toString());
		}
		// access_flag(2byte)
		result.setAccessFlag(byteBuff.getShort());
		System.out.println("accessFlag : " + result.getAccessFlag());
		// this_class(2byte)
		result.setThisClass(byteBuff.getShort());
		System.out.println("thisClass : " + result.getThisClass());
		// super_class(2byte)
		result.setSuperClass(byteBuff.getShort());
		System.out.println("superClass : " + result.getSuperClass());
		// interfaces_count(2byte)
		result.setInterfacesCount(byteBuff.getShort());
		System.out.println("interfacesCount : " + result.getInterfacesCount());
		if (result.getInterfacesCount() > 0) {
			int[] interfaces = new int[result.getInterfacesCount()];
			int i = 0;
			while (i < result.getInterfacesCount()) {
				interfaces[i] = byteBuff.getShort();
				System.out.println("\tßinterfaces[" + i + "] : " + interfaces[i]);
				i++;
			}
			// interfaces(2byte * interfaces_count)
			result.setInterfaces(interfaces);
		}
		// fields_count(2byte)
		result.setFieldsCount(byteBuff.getShort());
		System.out.println("fieldsCount : " + result.getFieldsCount());
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
		System.out.println("attributesCount : " + result.getAttributesCount());
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
		
		System.out.println("position/limit : " + byteBuff.position() + "/" + byteBuff.limit());
		
		return result;
	}
	
	private MethodInfo convertToMethodInfo(ByteBuffer byteBuffer , Map<Integer,ConstantPool> cpMap) throws ClassFileFormatException {
		System.out.println("[MethodInfo]------------------------");
		MethodInfo methodInfo = new MethodInfo();
		methodInfo.setAccessFlags(byteBuffer.getShort());	// access_flags(2byte)
		methodInfo.setNameIndex(byteBuffer.getShort());
		System.out.println("\tNameIndex : " + cpMap.get(methodInfo.getNameIndex()).toString());
		methodInfo.setDescriptorIndex(byteBuffer.getShort());
		System.out.println("\tDescriptorIndex : " + cpMap.get(methodInfo.getDescriptorIndex()).toString());
		methodInfo.setAttributesCount(byteBuffer.getShort());
		System.out.println("\tAttributeCount : " + methodInfo.getAttributesCount());
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
		System.out.println("[FieldInfo]------------------------");
		FieldInfo fieldInfo = new FieldInfo();
		fieldInfo.setAccessFlags(byteBuffer.getShort());		// access_flags(2byte)
		fieldInfo.setNameIndex(byteBuffer.getShort());			// name_index(2byte)
		System.out.println("\tNameIndex : " + cpMap.get(fieldInfo.getNameIndex()).toString());
		fieldInfo.setDescriptorIndex(byteBuffer.getShort());	// descriptorIndex(2byte)
		System.out.println("\tDescriptorIndex : " + cpMap.get(fieldInfo.getDescriptorIndex()).toString());
		fieldInfo.setAttributeCount(byteBuffer.getShort());		// attributeCount(2byte)
		System.out.println("\tAttributeCount : " + fieldInfo.getAttributeCount());
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
