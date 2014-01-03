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

import com.btmatthews.atlas.jcr.config.PooledRepositoryConfiguration;
import com.btmatthews.leabharlann.view.FileContentMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * Configure the servlet context.
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
     * Register the resources from the WebJars dependencies.
     *
     * @param registry The resource handler registry.
     */
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * Register the {@link FileContentMessageConverter} and {@link MappingJacksonHttpMessageConverter} message converters.
     *
     * @param converters The list to which the message converters are added.
     */
    @Override
    public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
        converters.add(fileContentMessageConverter);
        converters.add(new MappingJackson2HttpMessageConverter());
    }
}
