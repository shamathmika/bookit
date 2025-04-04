package com._2.BookIt.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
public class MongoConfig {
	
	/**
	 * Removes the _class field for all registers within mongodb. Without this, attempting to save to mongodb
	 * was throwing an error since Spring Boot automatically adds _class field and we have a validation
	 * in mongodb to not expect new fields.
	 */
	@Bean
	public MongoTemplate mongoTemplate (MongoDatabaseFactory mongoDbFactory,
	                                    MongoMappingContext context) {
		
		MappingMongoConverter converter =
				new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory), context);
		converter.setTypeMapper(new DefaultMongoTypeMapper(null));
		
		return new MongoTemplate(mongoDbFactory, converter);
	}
}
