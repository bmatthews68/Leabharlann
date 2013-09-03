/*
 * Copyright 2012-2013 Brian Matthews
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.btmatthews.leabharlann.config;

import com.btmatthews.atlas.jcr.CredentialsProvider;
import com.btmatthews.atlas.jcr.RepositoryProvider;
import com.btmatthews.atlas.jcr.config.PooledRepositoryConfiguration;
import com.btmatthews.leabharlann.service.EncodingDetector;
import com.btmatthews.leabharlann.service.TypeDetector;
import com.btmatthews.leabharlann.service.impl.ICU4JEncodingDetector;
import com.btmatthews.leabharlann.service.impl.TikaTypeDetector;
import com.btmatthews.leabharlann.view.FileContentMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;
import javax.jcr.Credentials;
import javax.jcr.Repository;
import javax.jcr.SimpleCredentials;
import java.util.List;

/**
 * Configure the application context.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 */
@Configuration
@ComponentScan({"com.btmatthews.leabharlann.controller", "com.btmatthews.leabharlann.service", "com.btmatthews.leabharlann.view"})
@Import(PooledRepositoryConfiguration.class)
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

    /**
     * Auto-wire the message converter that translates file content descriptors to HTTP servlet responses.
     */
    @Autowired
    private FileContentMessageConverter fileContentMessageConverter;
    /**
     * Lookup the content repository in JNDI.
     */
    @Resource(name = "jcr/local", mappedName = "jcr/local")
    private Repository repository;

    /**
     * Create the {@link CredentialsProvider} bean that is used to provide authentication credentials when accessing
     * the content repository.
     *
     * @return The {@link CredentialsProvider} bean.
     */
    @Bean
    public CredentialsProvider credentialsProvider() {
        return new CredentialsProvider() {
            @Override
            public Credentials getGlobalCredentials() {
                return new SimpleCredentials("admin", "password".toCharArray());
            }

            @Override
            public Credentials getUserCredentials() {
                return null;
            }
        };
    }

    /**
     * Create the {@link RepositoryProvider} bean that is used to obtain the {@link Repository} API object.
     *
     * @return The {@link RepositoryProvider} bean.
     */
    @Bean
    public RepositoryProvider repositoryProvider() {
        return new RepositoryProvider() {
            @Override
            public Repository getRepository() {
                return repository;
            }
        };
    }

    @Bean
    public TypeDetector typeDetector() {
        return new TikaTypeDetector();
    }

    @Bean
    public EncodingDetector encodingDetector() {
        return new ICU4JEncodingDetector();
    }

    /**
     * Register the {@link FileContentMessageConverter} and {@link MappingJacksonHttpMessageConverter} message converters.
     *
     * @param converters The list to which the message converters are added.
     */
    @Override
    public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
        converters.add(fileContentMessageConverter);
        converters.add(new MappingJacksonHttpMessageConverter());
    }
}
