package vn.ghtk.demo.catalog.adapter.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {
    @Primary
    @Bean("dataSource")
    @ConfigurationProperties(prefix = "spring.demo.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }
}
