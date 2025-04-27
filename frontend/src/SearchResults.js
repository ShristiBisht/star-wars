import React from "react";

const SearchResults = ({ results }) => {
  if (!results) return null;

  return (
    <div className="search-results">
      <h3>Search Results:</h3>
      <p><strong>Name:</strong> {results.name}</p>
      <p><strong>Type:</strong> {results.type}</p>
      <p><strong>Films:</strong> {results.films?.join(", ")}</p>
      <p><strong>Count:</strong> {results.count}</p>
    </div>
  );
};

export default SearchResults;
