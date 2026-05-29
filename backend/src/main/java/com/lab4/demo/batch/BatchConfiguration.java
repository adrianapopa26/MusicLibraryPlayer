package com.lab4.demo.batch;

import javax.sql.DataSource;

import com.lab4.demo.track.TrackRepository;
import com.lab4.demo.track.model.Track;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfiguration {

    private final TrackRepository trackRepository;

    public BatchConfiguration(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    @Bean
    public FlatFileItemReader<Track> reader() {
        return new FlatFileItemReaderBuilder<Track>()
                .name("TrackBatchItemReader")
                .resource(new ClassPathResource("Tracks.csv"))
                .delimited()
                .names("id", "artist", "album", "duration", "explicit_lyrics", "link", "preview", "title")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Track>() {{
                    setTargetType(Track.class);
                }})
                .build();
    }

    @Bean
    public TrackBatchItemProcessor processor() {
        return new TrackBatchItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Track> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Track>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO track (id, artist, album, duration, explicit_lyrics, link, preview, title) VALUES (:id, :preview, :album, :duration, :explicit_lyrics, :link, :artist, :title)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importUserJob(JobRepository jobRepository, JobCompletionNotificationListener listener, Step step1) {
        return new JobBuilder("importUserJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                      JdbcBatchItemWriter<Track> writer) {
        trackRepository.deleteAll();
        return new StepBuilder("step1", jobRepository)
                .<Track, Track>chunk(10, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }
}
