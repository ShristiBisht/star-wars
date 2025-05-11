import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import SearchForm from './SearchForm'; // Path to your SearchForm component
import { fetchEntityData } from '../services/apiService';

// Mock the fetchEntityData API
jest.mock('../services/apiService', () => ({
  fetchEntityData: jest.fn(),
}));

// Mock localStorage
const mockLocalStorage = (() => {
  let store = {};
  return {
    getItem: jest.fn((key) => store[key] || null),
    setItem: jest.fn((key, value) => { store[key] = value; }),
    clear: jest.fn(() => { store = {}; }),
  };
})();
Object.defineProperty(window, 'localStorage', { value: mockLocalStorage });

describe('SearchForm', () => {
  afterEach(() => {
    jest.clearAllMocks();
  });

  test('displays error when offline and no cached data is available', async () => {
    fetchEntityData.mockRejectedValueOnce(new Error('Failed to fetch'));
    
    render(<SearchForm />);
    
    // Switch to Offline Mode
    fireEvent.click(screen.getByText(/Switch to Offline Mode/i));

    fireEvent.change(screen.getByLabelText(/Name/i), { target: { value: 'Tatooine' } });
    fireEvent.click(screen.getByText(/Search/i));  // Submit using the button

    // Wait for the error message
    await waitFor(() => {
      expect(screen.getByText(/Sorry, we couldn't find anything matching your search in offline mode./i)).toBeInTheDocument();
    });
  });

  test('displays cached data when offline', async () => {
    // Arrange: mock localStorage with cached data
    const cachedData = {
      type: 'planet',
      name: 'Tatooine',
      count: 1,
      films: ['Film 1'],
    };
    window.localStorage.setItem('planets-Tatooine', JSON.stringify(cachedData));
  
    render(<SearchForm />);
  
    // Act: switch to offline mode
    fireEvent.click(screen.getByText(/Switch to Offline Mode/i));
  
    // Fill in the name field
    fireEvent.change(screen.getByLabelText(/Name/i), {
      target: { value: 'Tatooine' },
    });
  
    // Click the search button
    fireEvent.click(screen.getByText(/Search/i));
  
  });

  test('displays data when online', async () => {
    const mockData = {
      data: {
        type: 'planet',
        name: 'Tatooine',
        count: 1,
        films: ['Film 1'],
      },
    };
    
    fetchEntityData.mockResolvedValueOnce(mockData);

    const { container } = render(<SearchForm />);
  
    // Fill in form fields
    fireEvent.change(screen.getByLabelText(/Name/i), {
      target: { value: 'Tatooine' },
    });
  
    // Submit form using querySelector (since role="form" doesn't exist)
    const form = container.querySelector('form');
    fireEvent.submit(form);
    // Wait for and assert the result
  await waitFor(() => {
    expect(screen.getByRole('heading', { name: /result/i })).toBeInTheDocument();
    expect(screen.getByText(/Tatooine/i)).toBeInTheDocument();
    expect(screen.getByText(/Film 1/i)).toBeInTheDocument();
  });
  // Check localStorage
  expect(mockLocalStorage.setItem).toHaveBeenCalledWith(
    'planets-Tatooine',
    JSON.stringify(mockData.data)
  );
});
});
