import React, { useState } from 'react';
import axios from 'axios';

const SearchForm = () => {
  const [type, setType] = useState('planets');
  const [name, setName] = useState('');
  const [result, setResult] = useState(null);
  const [error, setError] = useState(null);
  const [offlineMode, setOfflineMode] = useState(false);  // Add offlineMode state

  const types = ['planets', 'starships', 'vehicles', 'people', 'films', 'species'];

  const toggleOfflineMode = () => {
    setOfflineMode(!offlineMode);  // Toggle offline mode
  };

  // Function to check and fetch cached data from localStorage
  const getCachedData = (type, name) => {
    const cachedData = localStorage.getItem(`${type}-${name}`);
    if (cachedData) {
      return JSON.parse(cachedData);
    }
    return null;
  };

  // Function to store data in localStorage
  const storeInLocalStorage = (type, name, data) => {
    localStorage.setItem(`${type}-${name}`, JSON.stringify(data));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const cachedData = getCachedData(type, name);
    if (offlineMode && cachedData) {
      console.log("Caching in local storage")
      setResult(cachedData);
      setError(null);
      return;
    }
    else if(!cachedData){
      console.error("No data in cache");
      setError("Sorry, we couldn't find anything matching your search in offline mode.");
      setResult(null);
    }

    try {
      if(!offlineMode){
        // If not in offline mode, fetch data from the API
        const response = await axios.get(`http://localhost:8080/search`, {
          params: { type, name,offlineMode },
          withCredentials: true,  // Send cookies with the request
        });
        setResult(response.data);
        setError(null);
        storeInLocalStorage(type, name, response.data);
      }
      
    } catch (error) {
      console.error("Error fetching data:", error);
      setError("Sorry, we couldn't find anything matching your search.");
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

        <button type="submit" className="btn btn-primary">Search</button>
      </form>

      {/* Toggle for Offline Mode */}
      <div className="offline-toggle mt-4">
        <button onClick={toggleOfflineMode} className="btn btn-secondary">
          {offlineMode ? "Switch to Online Mode" : "Switch to Offline Mode"}
        </button>
      </div>

      {/* Display result */}
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

      {/* Display error message */}
      {error && <p className="error mt-4">{error}</p>}
    </div>
  );
};

export default SearchForm;