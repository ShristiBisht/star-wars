import React, { useState } from 'react';
import axios from 'axios';


const SearchForm = () => {
  const [type, setType] = useState('planets');
  const [name, setName] = useState('');
  const [result, setResult] = useState(null);
  const [error, setError] = useState(null);

  const types = ['planets', 'starships', 'vehicles', 'people', 'films', 'species'];

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.get(`http://localhost:8080/search`, {
            params: { type, name },
            withCredentials: true // Send cookies with the request
        });
      setResult(response.data);
      setError(null);
      console.log(response.data); // Handle response
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
          <pre>{result && (
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
</pre>
        </div>
      )}

      {error && <p className="error mt-4">{error}</p>}
    </div>
  );
};

export default SearchForm;
