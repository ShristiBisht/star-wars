import React, { useState } from 'react';
import { getCachedData, storeInCache } from '../services/cacheService';
import { fetchEntityData } from '../services/apiService';

const types = ['planets', 'starships', 'vehicles', 'people', 'films', 'species'];

const SearchForm = () => {
  const [formData, setFormData] = useState({ type: 'planets', name: '' });
  const [result, setResult] = useState(null);
  const [error, setError] = useState(null);
  const [offlineMode, setOfflineMode] = useState(false);

  const handleChange = (e) => {
    setFormData(prev => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const { type, name } = formData;

    if (!name.trim()) {
      setError('Please enter a valid name.');
      setResult(null);
      return;
    }

    const cached = getCachedData(type, name);
    if (offlineMode) {
      if (cached) {
        setResult(cached);
        setError(null);
      } else {
        setError('No cached data available for offline mode.');
        setResult(null);
      }
      return;
    }

    try {
      if(!offlineMode){
        // If not in offline mode, fetch data from the API
        const data = await fetchEntityData(type, name,offlineMode);
        setResult(data);
        setError(null);
        storeInCache(type, name, data);
      }
    } catch (err) {
      console.error('API Error:', err);
      setError('Unable to fetch data. Please try again later.');
      setResult(null);
    }
  };

  return (
    <div className="search-form">
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="type">Type</label>
          <select
            id="type"
            name="type"
            value={formData.type}
            onChange={handleChange}
            className="form-control"
          >
            {types.map((t) => (
              <option key={t} value={t}>{t}</option>
            ))}
          </select>
        </div>

        <div className="form-group">
          <label htmlFor="name">Name</label>
          <input
            id="name"
            name="name"
            value={formData.name}
            onChange={handleChange}
            placeholder="Enter name"
            className="form-control"
          />
        </div>

        <button type="submit" className="btn btn-primary">Search</button>
      </form>

      <div className="offline-toggle mt-4">
        <button onClick={() => setOfflineMode(prev => !prev)} className="btn btn-secondary">
          {offlineMode ? "Switch to Online Mode" : "Switch to Offline Mode"}
        </button>
      </div>

      {result && (
        <div className="result mt-4 p-4 border rounded shadow-sm">
          <h3 className="mb-3 text-primary">Result</h3>
          <div><strong>Type:</strong> {result.type}</div>
          <div><strong>Name:</strong> {result.name}</div>
          <div><strong>Count:</strong> {result.count}</div>
          <div><strong>Films:</strong>
            <ul className="list-disc ml-5">
              {(result.films?.length > 0) ? (
                result.films.map((film, i) => <li key={i}>{film}</li>)
              ) : (
                <li>No Films Available</li>
              )}
            </ul>
          </div>
        </div>
      )}

      {error && <div className="alert alert-danger mt-4">{error}</div>}
    </div>
  );
};

export default SearchForm;
