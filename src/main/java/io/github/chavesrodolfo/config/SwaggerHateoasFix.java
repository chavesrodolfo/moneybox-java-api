package io.github.chavesrodolfo.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.plugin.core.SimplePluginRegistry;


@Configuration
public class SwaggerHateoasFix {
    
    /**
     * Fix to use SpringFox 2.9.2 with Spring HATEOAS in SpringBoot 2+
     * Remove class after issue https://github.com/springfox/springfox/issues/3052 be solved.
	 */
	@Bean
	public LinkDiscoverers discoverers(){
		return new LinkDiscoverers(SimplePluginRegistry.create(Arrays.asList(new CollectionJsonLinkDiscoverer())));
    }
    
}