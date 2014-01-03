/*
 * Copyright 2012-2014 Brian Matthews
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
import org.apache.tika.Tika;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.jcr.Credentials;
import javax.jcr.Repository;
import javax.jcr.SimpleCredentials;

/**
 * Configure the application context.
 *
 * @author <a href="mailto:brian@btmatthews.com">Brian Matthews</a>
 * @since 1.0.0
 */
@Configuration
public class AppConfig {

    /**
     * Lookup the content repository in JNDI.
     */
    @Resource(name = "jcr/local", mappedName = "jcr/local")
    private Repository repository;

    /**
     * Create the {@link com.btmatthews.atlas.jcr.CredentialsProvider} bean that is used to provide authentication credentials when accessing
     * the content repository.
     *
     * @return The {@link com.btmatthews.atlas.jcr.CredentialsProvider} bean.
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
     * Create the {@link com.btmatthews.atlas.jcr.RepositoryProvider} bean that is used to obtain the {@link Repository} API object.
     *
     * @return The {@link com.btmatthews.atlas.jcr.RepositoryProvider} bean.
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
    public Tika tika() {
        return new Tika();
    }
}
