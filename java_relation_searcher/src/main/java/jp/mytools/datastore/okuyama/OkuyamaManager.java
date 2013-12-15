package jp.mytools.datastore.okuyama;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okuyama.imdst.client.OkuyamaClient;
import okuyama.imdst.client.OkuyamaClientException;
import okuyama.imdst.client.OkuyamaClientFactory;

public class OkuyamaManager {

	private static Logger log = LoggerFactory.getLogger(OkuyamaManager.class);

	private static String[] masterNodes = { "localhost:8888" };
	private static int maxClients = 10;
	
	private enum OkuyamaKeyName {
		INCREMENT_KEY_MANAGER("INCR_KEY");

		private OkuyamaKeyName(String keyName) {
			this.keyName = keyName;
		}
		
		private String keyName;
		
		public String getKeyName() {
			return this.keyName;
		}
	}
	
	private static OkuyamaClient getClient() throws OkuyamaClientException {
		OkuyamaClient okuyamaClient = null;

		OkuyamaClientFactory factory = OkuyamaClientFactory.getFactory(
				masterNodes, maxClients);

		okuyamaClient = factory.getClient();

		return okuyamaClient;
	}
	
	/**
	 * TODO どう考えてもsynchronizedあかん
	 * @return
	 * @throws OkuyamaClientException
	 */
	public static synchronized String getKey() throws OkuyamaClientException {
		
		Long key = null;
		OkuyamaClient client = null;
		try {
			client = getClient();
			Object[] result = client.incrValue(OkuyamaKeyName.INCREMENT_KEY_MANAGER.getKeyName(), 1L);
			
			if (result[0].equals(true)) {
				key = (Long)result[1];
			}
			
			if (key == null) {
				throw new OkuyamaClientException("Fail to get key.");
			}
		} finally {
			if (client != null) {
				client.close();
			}
		}
		
		return key.toString();
	}
	
	public static Object getByKey(String key) throws OkuyamaClientException{
		OkuyamaClient client = null;
		try {
			client = getClient();
			Object[] result = client.getObjectValue(key);
			if (result[0].equals("true")) {
				return result[1];
			}
		} finally {
			if (client != null) {
				client.close();
			}
		}
		return null;
	}
	
	public static List<Object> getByTag(String[] tagList) throws OkuyamaClientException{
		
		OkuyamaClient client = null;
		List<Object> result = new ArrayList<Object>();
		try {
			client = getClient();
			// traffic,memoryに負荷が掛かりすぎるようならgetTagKeysResultに変更する
			String[] keys = client.getMultiTagKeys(tagList,true);
			if (keys == null) {
				return result;
			}
			
			for (String key : keys) {
				Object[] tempResult = client.getObjectValue(key);
				
				if (tempResult[0].equals("true")) {
					
					if (tempResult[1] instanceof Map) {
						Map<String, String> resMap = (Map) tempResult[1];
						resMap.put("key", key);
					}
					result.add(tempResult[1]);
				}
			}
		} finally {
			if (client != null) {
				client.close();
			}
		}
		
		return result;
	}

	public static String[] getKeysByTag(String[] tagList)  throws OkuyamaClientException{
		
		OkuyamaClient client = null;
		String[] keys = null;
		try  {
			client = getClient();
			keys = client.getMultiTagKeys(tagList,true);
		} finally {
			if (client != null) {
				client.close();
			}
		}
		
		return keys;
	}
	
	public static void add(String key , Object dto , String[] tagList) throws OkuyamaClientException{
		
		OkuyamaClient client = null;
		try {
			client = getClient();			
			client.setObjectValue(key, tagList, dto);
			log.info("[ADD] key = " + key);
		} finally {
			if (client != null) {
				client.close();
			}
		}
	}
		
	public static void mod(String key,String newKey, Object dto ,String[] tagList) throws OkuyamaClientException{
		OkuyamaClient client = null;
		try {
			client = getClient();
			client.setNewObjectValue(newKey, tagList, dto);
			client.removeValue(key);
			log.info("[MOD] key = " + key + " , tags = " + tagList);
		} finally {
			if (client != null) {
				client.close();
			}
		}
		
	}
	
	public static void del(String key) throws OkuyamaClientException {
		OkuyamaClient client = null;
		try {
			client = getClient();
			client.removeValue(key);
			log.info("[DEL] key = " + key);
		} finally {
			if (client != null) {
				client.close();
			}
		}
	}
	
}
