package jp.mytools.relations;

import jp.mytools.relations.config.ConfigMaster;
import jp.mytools.relations.resolver.RelationResolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RelationResolveExecutor {

	private static Logger logger = LoggerFactory.getLogger(RelationResolveExecutor.class );

	public static void main(String[] args) {
		try {
			RelationResolver resolver = (RelationResolver) Class.forName(ConfigMaster.getResolverClass()).newInstance();
			resolver.resolve();
		} catch (Throwable t) {
			logger.error("", t);
		}
	}


}
