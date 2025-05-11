// Updated React SearchForm component based on 12-factor principles, SOLID, and HATEOAS integration
import React, { useState } from 'react';
import { getCachedData, storeInCache } from '../services/cacheService';
import { pollSearchResult } from '../services/apiService';
import { initiateSearch } from '../services/initiateSearchService';

const SearchForm = () => {
  const [type, setType] = useState('planets');
  const [name, setName] = useState('');
  const [result, setResult] = useState(null);
  const [error, setError] = useState(null);
  const [offlineMode, setOfflineMode] = useState(false);
  const [loading, setLoading] = useState(false);

  const types = ['planets', 'starships', 'vehicles', 'people', 'films', 'species'];

  const toggleOfflineMode = () => setOfflineMode(!offlineMode);

  const handleSubmit = async (e) => {
    e.preventDefault();
    const cachedData = getCachedData(type, name);

    if (offlineMode) {
      if (cachedData) {
        setResult(cachedData);
        setError(null);
      } else {
        setError("Sorry, we couldn't find anything matching your search in offline mode.");
        setResult(null);
      }
      return;
    }

    try {
      setLoading(true);
      const { requestId } = await initiateSearch(type, name);

      // Poll for result using requestId
      const pollResult = await pollSearchResult(requestId);
      if (pollResult && pollResult.count!=0) {
        setResult(pollResult);
        storeInCache(type, name, pollResult);
        setError(null);
      } else {
        setResult(null);
        setError("No results found.");
      }
    } catch (err) {
      console.error("Search error:", err);
      setError("An error occurred while fetching data.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="search-form">
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="type">Type</label>
          <select
            id="type"
            value={type}
            onChange={(e) => setType(e.target.value)}
            className="form-control"
          >
            {types.map((typeOption, index) => (
              <option key={index} value={typeOption}>
                {typeOption}
              </option>
            ))}
          </select>
        </div>

        <div className="form-group">
          <label htmlFor="name">Name</label>
          <input
            type="text"
            id="name"
            value={name}
            onChange={(e) => setName(e.target.value)}
            className="form-control"
            placeholder="Enter name"
          />
        </div>

        <button type="submit" className="btn btn-primary" disabled={loading}>
          {loading ? "Searching..." : "Search"}
        </button>
      </form>

      <div className="offline-toggle mt-4">
        <button onClick={toggleOfflineMode} className="btn btn-secondary">
          {offlineMode ? "Switch to Online Mode" : "Switch to Offline Mode"}
        </button>
        <p className="mt-2">
          Mode: <strong>{offlineMode ? "Offline (cache)" : "Online (live API)"}</strong>
        </p>
      </div>

      {result && (
        <div className="result mt-4 p-4 border rounded shadow-sm">
          <h3 className="mb-3 text-primary">Result</h3>
          <div><strong>Type:</strong> {result.type}</div>
          <div><strong>Name:</strong> {result.name}</div>
          <div><strong>Count:</strong> {result.count}</div>
          <div><strong>Films:</strong>
            <ul className="list-disc ml-5">
              {result.films && result.films.length > 0 ? (
                result.films.map((film, index) => (
                  <li key={index}>{film}</li>
                ))
              ) : (
                <li>No Films Available</li>
              )}
            </ul>
          </div>
        </div>
      )}

      {error && <p className="error mt-4 text-danger">{error}</p>}
    </div>
  );
};

export default SearchForm;
