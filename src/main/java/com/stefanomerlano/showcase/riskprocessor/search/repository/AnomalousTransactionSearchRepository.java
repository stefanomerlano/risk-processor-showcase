package com.stefanomerlano.showcase.riskprocessor.search.repository;

import com.stefanomerlano.showcase.riskprocessor.search.document.AnomalousTransactionDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for Elasticsearch documents.
 */
@Repository
public interface AnomalousTransactionSearchRepository extends ElasticsearchRepository<AnomalousTransactionDoc, String> {
}