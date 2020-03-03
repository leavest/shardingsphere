/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.encrypt.rewrite.token.generator.impl;

import com.google.common.base.Preconditions;
import lombok.Setter;
import org.apache.shardingsphere.encrypt.rewrite.aware.QueryWithCipherColumnAware;
import org.apache.shardingsphere.encrypt.rewrite.token.generator.BaseEncryptSQLTokenGenerator;
import org.apache.shardingsphere.encrypt.strategy.EncryptTable;
import org.apache.shardingsphere.sql.parser.relation.metadata.RelationMetas;
import org.apache.shardingsphere.sql.parser.relation.statement.SQLStatementContext;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.predicate.AndPredicate;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.predicate.PredicateSegment;
import org.apache.shardingsphere.sql.parser.sql.statement.generic.WhereSegmentAvailable;
import org.apache.shardingsphere.underlying.rewrite.sql.token.generator.CollectionSQLTokenGenerator;
import org.apache.shardingsphere.underlying.rewrite.sql.token.generator.aware.RelationMetasAware;
import org.apache.shardingsphere.underlying.rewrite.sql.token.pojo.generic.SubstitutableColumnNameToken;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Optional;

/**
 * Predicate column token generator for encrypt.
 */
@Setter
public final class EncryptPredicateColumnTokenGenerator extends BaseEncryptSQLTokenGenerator implements CollectionSQLTokenGenerator, RelationMetasAware, QueryWithCipherColumnAware {
    
    private RelationMetas relationMetas;
    
    private boolean queryWithCipherColumn;
    
    @Override
    protected boolean isGenerateSQLTokenForEncrypt(final SQLStatementContext sqlStatementContext) {
        return sqlStatementContext.getSqlStatement() instanceof WhereSegmentAvailable && ((WhereSegmentAvailable) sqlStatementContext.getSqlStatement()).getWhere().isPresent();
    }
    
    @Override
    public Collection<SubstitutableColumnNameToken> generateSQLTokens(final SQLStatementContext sqlStatementContext) {
        Preconditions.checkState(((WhereSegmentAvailable) sqlStatementContext.getSqlStatement()).getWhere().isPresent());
        Collection<SubstitutableColumnNameToken> result = new LinkedHashSet<>();
        for (AndPredicate each : ((WhereSegmentAvailable) sqlStatementContext.getSqlStatement()).getWhere().get().getAndPredicates()) {
            result.addAll(generateSQLTokens(sqlStatementContext, each));
        }
        return result;
    }
    
    private Collection<SubstitutableColumnNameToken> generateSQLTokens(final SQLStatementContext sqlStatementContext, final AndPredicate andPredicate) {
        Collection<SubstitutableColumnNameToken> result = new LinkedList<>();
        for (PredicateSegment each : andPredicate.getPredicates()) {
            Optional<EncryptTable> encryptTable = findEncryptTable(sqlStatementContext, each);
            if (!encryptTable.isPresent() || !encryptTable.get().findEncryptor(each.getColumn().getIdentifier().getValue()).isPresent()) {
                continue;
            }
            int startIndex = each.getColumn().getOwner().isPresent() ? each.getColumn().getOwner().get().getStopIndex() + 2 : each.getColumn().getStartIndex();
            int stopIndex = each.getColumn().getStopIndex();
            if (!queryWithCipherColumn) { 
                Optional<String> plainColumn = encryptTable.get().findPlainColumn(each.getColumn().getIdentifier().getValue());
                if (plainColumn.isPresent()) {
                    result.add(new SubstitutableColumnNameToken(startIndex, stopIndex, plainColumn.get()));
                    continue;
                }
            }
            Optional<String> assistedQueryColumn = encryptTable.get().findAssistedQueryColumn(each.getColumn().getIdentifier().getValue());
            SubstitutableColumnNameToken encryptColumnNameToken = assistedQueryColumn.map(columnName -> new SubstitutableColumnNameToken(startIndex, stopIndex, columnName))
                    .orElseGet(() -> new SubstitutableColumnNameToken(startIndex, stopIndex, encryptTable.get().getCipherColumn(each.getColumn().getIdentifier().getValue())));
            result.add(encryptColumnNameToken);
        }
        return result;
    }
    
    private Optional<EncryptTable> findEncryptTable(final SQLStatementContext sqlStatementContext, final PredicateSegment segment) {
        Optional<String> tableName = sqlStatementContext.getTablesContext().findTableName(segment, relationMetas);
        return tableName.isPresent() ? getEncryptRule().findEncryptTable(tableName.get()) : Optional.empty();
    }
}
