package com.batch.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import com.batch.model.Food;

@Configuration
@EnableBatchProcessing
public class BatchXmlConfig {

	@Autowired
	private JobBuilderFactory jb;

	@Autowired
	private StepBuilderFactory sb;

	@Autowired
	private DataSource dataSource;

	// 1.reader
	@Bean
	public StaxEventItemReader<Food> readXmlData() {
		StaxEventItemReader<Food> reader = new StaxEventItemReader<>();
		reader.setResource(new ClassPathResource("food.xml"));
		reader.setFragmentRootElementName("food");
		Map<String, String> map = new HashMap<>();
		map.put("food", "com.batch.model.Food");
		XStreamMarshaller marshaller = new XStreamMarshaller();
		marshaller.setAliases(map);
		reader.setUnmarshaller(marshaller);
		System.out.println("In reader");
		return reader;
	}

	// 2.writer
	@Bean
	public ItemWriter<Food> writer() {
		JdbcBatchItemWriter<Food> writer = new JdbcBatchItemWriter<>();
		writer.setSql(
				"INSERT INTO FOOD  (id, name, price, description, calories ) VALUES (:id, :name, :price, :description, :calories)");
		writer.setDataSource(dataSource);
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		System.out.println("In writer");
		return writer;
	}

	// 3.processor

	// 4.Step
	@Bean
	public Step stepProcess() {
		return sb.get("stepProcess").<Food, Food>chunk(5).reader(readXmlData()).writer(writer()).build();
	}

	// 5.Job
	@Bean
	public Job jobProcess() {
		return jb.get("jobProcess").incrementer(new RunIdIncrementer()).listener(listener()).start(stepProcess())
				.build();
	}

	@Bean
	public JobExecutionListener listener() {
		return new JobExecutionListener() {

			@Override
			public void beforeJob(JobExecution jobExecution) {
				System.out.println("before Job");
			}

			@Override
			public void afterJob(JobExecution jobExecution) {
				System.out.println("after Job");
			}
		};
	}

}
