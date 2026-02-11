import React, { useState } from 'react';
import { Activity, Play, TrendingUp, TrendingDown, Zap } from 'lucide-react';
import PerformanceService from '../../../services/performanceService';
import Loading from '../../../components/Loading/Loading';
import './PerformanceReport.css';

const PerformanceReport = () => {
  const [results, setResults] = useState(null);
  const [loading, setLoading] = useState(false);
  const [iterations, setIterations] = useState(10);

  const runBenchmark = async () => {
    setLoading(true);
    try {
      const data = await PerformanceService.runFullBenchmark(iterations);
      setResults(data);
    } catch (error) {
      console.error('Benchmark failed:', error);
    } finally {
      setLoading(false);
    }
  };

  const formatTime = (ms) => `${ms.toFixed(2)}ms`;

  return (
    <div className="performance-report">
      <div className="page-header">
        <h1>
          <Activity size={28} />
          Performance Analysis
        </h1>
        <p>Compare REST API vs GraphQL performance</p>
      </div>

      <div className="benchmark-controls">
        <div className="control-group">
          <label>Iterations:</label>
          <input
            type="number"
            value={iterations}
            onChange={(e) => setIterations(parseInt(e.target.value))}
            min="5"
            max="50"
            disabled={loading}
          />
        </div>
        <button
          className="btn btn-primary"
          onClick={runBenchmark}
          disabled={loading}
        >
          <Play size={18} />
          {loading ? 'Running...' : 'Run Benchmark'}
        </button>
      </div>

      {loading && <Loading text="Running performance tests..." />}

      {results && !loading && (
        <div className="results-container">
          {/* Products Comparison */}
          <div className="comparison-card">
            <h2>Products Endpoint</h2>
            <div className="comparison-grid">
              <div className="api-result rest">
                <h3>REST API</h3>
                <div className="metric">
                  <span className="label">Average:</span>
                  <span className="value">{formatTime(results.products.rest.average)}</span>
                </div>
                <div className="metric">
                  <span className="label">Min:</span>
                  <span className="value">{formatTime(results.products.rest.min)}</span>
                </div>
                <div className="metric">
                  <span className="label">Max:</span>
                  <span className="value">{formatTime(results.products.rest.max)}</span>
                </div>
              </div>

              <div className="vs-divider">
                <Zap size={32} />
              </div>

              <div className="api-result graphql">
                <h3>GraphQL</h3>
                <div className="metric">
                  <span className="label">Average:</span>
                  <span className="value">{formatTime(results.products.graphql.average)}</span>
                </div>
                <div className="metric">
                  <span className="label">Min:</span>
                  <span className="value">{formatTime(results.products.graphql.min)}</span>
                </div>
                <div className="metric">
                  <span className="label">Max:</span>
                  <span className="value">{formatTime(results.products.graphql.max)}</span>
                </div>
              </div>
            </div>

            <div className={`winner-badge ${results.products.winner.toLowerCase()}`}>
              {results.products.winner === 'REST' ? <TrendingDown size={20} /> : <TrendingUp size={20} />}
              <span>Winner: {results.products.winner}</span>
              <span className="difference">({results.products.difference}ms faster)</span>
            </div>
          </div>

          {/* Categories Comparison */}
          <div className="comparison-card">
            <h2>Categories Endpoint</h2>
            <div className="comparison-grid">
              <div className="api-result rest">
                <h3>REST API</h3>
                <div className="metric">
                  <span className="label">Average:</span>
                  <span className="value">{formatTime(results.categories.rest.average)}</span>
                </div>
                <div className="metric">
                  <span className="label">Min:</span>
                  <span className="value">{formatTime(results.categories.rest.min)}</span>
                </div>
                <div className="metric">
                  <span className="label">Max:</span>
                  <span className="value">{formatTime(results.categories.rest.max)}</span>
                </div>
              </div>

              <div className="vs-divider">
                <Zap size={32} />
              </div>

              <div className="api-result graphql">
                <h3>GraphQL</h3>
                <div className="metric">
                  <span className="label">Average:</span>
                  <span className="value">{formatTime(results.categories.graphql.average)}</span>
                </div>
                <div className="metric">
                  <span className="label">Min:</span>
                  <span className="value">{formatTime(results.categories.graphql.min)}</span>
                </div>
                <div className="metric">
                  <span className="label">Max:</span>
                  <span className="value">{formatTime(results.categories.graphql.max)}</span>
                </div>
              </div>
            </div>

            <div className={`winner-badge ${results.categories.winner.toLowerCase()}`}>
              {results.categories.winner === 'REST' ? <TrendingDown size={20} /> : <TrendingUp size={20} />}
              <span>Winner: {results.categories.winner}</span>
              <span className="difference">({results.categories.difference}ms faster)</span>
            </div>
          </div>

          {/* Summary */}
          <div className="summary-card">
            <h2>Summary</h2>
            <p>Tests run with {iterations} iterations each</p>
            <ul>
              <li>Products: <strong>{results.products.winner}</strong> performed better</li>
              <li>Categories: <strong>{results.categories.winner}</strong> performed better</li>
            </ul>
          </div>
        </div>
      )}
    </div>
  );
};

export default PerformanceReport;
