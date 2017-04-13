package io.opentracing.contrib.jdbc;


import io.opentracing.Span;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static io.opentracing.contrib.jdbc.JdbcTracingUtils.buildSpan;

public class TracingStatement implements Statement {
    private final Statement statement;
    private final ArrayList<String> batchCommands = new ArrayList<>();
    private final String dbType;
    private final String dbUser;

    public TracingStatement(Statement statement, String dbType, String dbUser) {
        this.statement = statement;
        this.dbType = dbType;
        this.dbUser = dbUser;
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        Span span = buildSpan("Query", sql, dbType, dbUser);
        try {
            return statement.executeQuery(sql);
        } finally {
            span.finish();
        }
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        Span span = buildSpan("Update", sql, dbType, dbUser);
        try {
            return statement.executeUpdate(sql);
        } finally {
            span.finish();
        }
    }

    @Override
    public void close() throws SQLException {
        statement.close();
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        return statement.getMaxFieldSize();
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {
        statement.setMaxFieldSize(max);
    }

    @Override
    public int getMaxRows() throws SQLException {
        return statement.getMaxRows();
    }

    @Override
    public void setMaxRows(int max) throws SQLException {
        statement.setMaxRows(max);
    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        statement.setEscapeProcessing(enable);
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return statement.getQueryTimeout();
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
        statement.setQueryTimeout(seconds);
    }

    @Override
    public void cancel() throws SQLException {
        statement.cancel();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return statement.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        statement.clearWarnings();
    }

    @Override
    public void setCursorName(String name) throws SQLException {
        statement.setCursorName(name);
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        Span span = buildSpan("Execute", sql, dbType, dbUser);
        try {
            return statement.execute(sql);
        } finally {
            span.finish();
        }
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return statement.getResultSet();
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return statement.getUpdateCount();
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return statement.getMoreResults();
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        statement.setFetchDirection(direction);
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return statement.getFetchDirection();
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        statement.setFetchSize(rows);
    }

    @Override
    public int getFetchSize() throws SQLException {
        return statement.getFetchSize();
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return statement.getResultSetConcurrency();
    }

    @Override
    public int getResultSetType() throws SQLException {
        return statement.getResultSetType();
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        statement.addBatch(sql);
        batchCommands.add(sql);
    }

    @Override
    public void clearBatch() throws SQLException {
        statement.clearBatch();
        batchCommands.clear();
    }

    @Override
    public int[] executeBatch() throws SQLException {
        Span span = buildSpanForBatch(batchCommands, dbType, dbUser);
        try {
            return statement.executeBatch();
        } finally {
            span.finish();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return statement.getConnection();
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        return statement.getMoreResults(current);
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return statement.getGeneratedKeys();
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        Span span = buildSpan("Update", sql, dbType, dbUser);
        try {
            return statement.executeUpdate(sql, autoGeneratedKeys);
        } finally {
            span.finish();
        }
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        Span span = buildSpan("Update", sql, dbType, dbUser);
        try {
            return statement.executeUpdate(sql, columnIndexes);
        } finally {
            span.finish();
        }
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        Span span = buildSpan("Update", sql, dbType, dbUser);
        try {
            return statement.executeUpdate(sql, columnNames);
        } finally {
            span.finish();
        }
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        Span span = buildSpan("Execute", sql, dbType, dbUser);
        try {
            return statement.execute(sql, autoGeneratedKeys);
        } finally {
            span.finish();
        }
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        Span span = buildSpan("Execute", sql, dbType, dbUser);
        try {
            return statement.execute(sql, columnIndexes);
        } finally {
            span.finish();
        }
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        Span span = buildSpan("Execute", sql, dbType, dbUser);
        try {
            return statement.execute(sql, columnNames);
        } finally {
            span.finish();
        }
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return statement.getResultSetHoldability();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return statement.isClosed();
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        statement.setPoolable(poolable);
    }

    @Override
    public boolean isPoolable() throws SQLException {
        return statement.isPoolable();
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        statement.closeOnCompletion();
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return statement.isCloseOnCompletion();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return statement.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return statement.isWrapperFor(iface);
    }

    private Span buildSpanForBatch(List<String> batchCommands, String dbType, String dbUser) {
        StringBuilder sql = new StringBuilder();
        for (String batchCommand : batchCommands) {
            sql.append(batchCommand);
        }

        return buildSpan("Batch", sql.toString(), dbType, dbUser);
    }
}
