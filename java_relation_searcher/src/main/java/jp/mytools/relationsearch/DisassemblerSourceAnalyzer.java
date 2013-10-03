package jp.mytools.relationsearch;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map.Entry;

import jp.mytools.relationsearch.attributes.beans.Attribute;
import jp.mytools.relationsearch.attributes.enums.AttributeType;
import jp.mytools.relationsearch.classfile.beans.ClassFileInfo;
import jp.mytools.relationsearch.classfile.logic.ClassFileLogic;
import jp.mytools.relationsearch.constantpool.beans.ConstantPool;
import jp.mytools.relationsearch.constantpool.beans.Utf8ConstantPool;
import jp.mytools.relationsearch.fields.beans.FieldInfo;
import jp.mytools.relationsearch.methods.beans.MethodInfo;

public class DisassemblerSourceAnalyzer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String filePath = "/Users/kawata_yusuke/Development/target.txt";
		
		try (FileReader fr = new FileReader(filePath);BufferedReader br = new BufferedReader(fr);) {

			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				DisassemblerSourceAnalyzer executor = new DisassemblerSourceAnalyzer();
				executor.exec(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		
		//String targetPath = "/Users/kawata_yusuke/Development/eclipse_workspace/ameba_blog_tomcat-2985/target/classes/jp/ameblo/pub/logic/BlogEntryTextLogic.class";
		// String targetPath =
		// "/Users/kawata_yusuke/Development/eclipse_workspace/samples/target/classes/local/samples/myprivate/compiler/MyTestInterfaceRunner.class";
		// String targetPath = "/Users/kawata_yusuke/dummyBinary.txt";
	}
	
	private void exec(String targetPath) throws Exception {
		FileSystem fs = FileSystems.getDefault();
		Path path = fs.getPath(targetPath);
		byte[] bytes = Files.readAllBytes(path);
		ClassFileLogic logic = new ClassFileLogic();
		ClassFileInfo dto = logic.convert(bytes);
		
	}

}
