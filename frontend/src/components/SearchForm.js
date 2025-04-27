import React, { useState } from 'react';
import axios from 'axios';

const SearchForm = () => {
  const [type, setType] = useState('Planets');
  const [name, setName] = useState('');
  const [result, setResult] = useState(null);
  const [error, setError] = useState(null);

  const types = ['Planets', 'Spaceships', 'Vehicles', 'People', 'Films', 'Species'];

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.get(`http://localhost:8080/search?type=${type}&name=${name}`);
      setResult(response.data);
      setError(null);
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

      {result && (
        <div className="result mt-4">
          <h3>Results:</h3>
          <pre>{JSON.stringify(result, null, 2)}</pre>
        </div>
      )}

      {error && <p className="error mt-4">{error}</p>}
    </div>
  );
};

export default SearchForm;
