package jp.co.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan; // ★追加
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EntityScan("jp.co.example.quote_proposal_1.entity") 
public class InsuranceQuoteProposalApplication {

    public static void main(String[] args) {
        SpringApplication.run(InsuranceQuoteProposalApplication.class, args);
    }

}