import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import SearchForm from './SearchForm'; // Adjust the import according to your folder structure
import { getCachedData, storeInCache } from '../services/cacheService';
import { pollSearchResult } from '../services/apiService';
import { initiateSearch } from '../services/initiateSearchService';

// Mocking the services used in the component
jest.mock('../services/cacheService', () => ({
  getCachedData: jest.fn(),
  storeInCache: jest.fn(),
}));

jest.mock('../services/apiService', () => ({
  pollSearchResult: jest.fn(),
}));

jest.mock('../services/initiateSearchService', () => ({
  initiateSearch: jest.fn(),
}));

describe('SearchForm Component', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  test('renders SearchForm correctly', () => {
    render(<SearchForm />);

    expect(screen.getByLabelText(/type/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/name/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /search/i })).toBeInTheDocument();
  });

  test('searches and displays result in online mode', async () => {
    initiateSearch.mockResolvedValue({ requestId: '1234' });
    pollSearchResult.mockResolvedValue({
      type: 'planets',
      name: 'Tatooine',
      count: 1,
      films: ['A New Hope'],
    });
  
    render(<SearchForm />);
  
    fireEvent.change(screen.getByLabelText(/type/i), { target: { value: 'planets' } });
    fireEvent.change(screen.getByLabelText(/name/i), { target: { value: 'Tatooine' } });
    fireEvent.click(screen.getByRole('button', { name: /search/i }));
  
    await waitFor(() =>
      expect(screen.getByText((_, el) => el.textContent === 'Type: planets')).toBeInTheDocument()
    );
  
    expect(screen.getByText((_, el) => el.textContent === 'Name: Tatooine')).toBeInTheDocument();
    expect(screen.getByText((_, el) => el.textContent === 'Count: 1')).toBeInTheDocument();
    expect(screen.getByText('A New Hope')).toBeInTheDocument();
  });
  

  test('shows error message if no results are found', async () => {
    // Arrange
    const mockResponse = { requestId: '123' };
    const mockPollResult = { count: 0 };

    initiateSearch.mockResolvedValue(mockResponse);
    pollSearchResult.mockResolvedValue(mockPollResult);

    render(<SearchForm />);

    fireEvent.change(screen.getByLabelText(/type/i), { target: { value: 'vehicles' } });
    fireEvent.change(screen.getByLabelText(/name/i), { target: { value: 'XYZ123' } });
    fireEvent.click(screen.getByRole('button', { name: /search/i }));

    // Wait for the result
    await waitFor(() => screen.getByText('No results found.'));

    expect(screen.getByText('No results found.')).toBeInTheDocument();
  });

  test('searches and displays cached result in offline mode', async () => {
    const cachedResult = {
      type: 'starships',
      name: 'Millennium Falcon',
      count: 1,
      films: ['Film1'],
    };
    getCachedData.mockReturnValue(cachedResult);
  
    render(<SearchForm />);
  
    fireEvent.click(screen.getByRole('button', { name: /switch to offline mode/i }));
    fireEvent.change(screen.getByLabelText(/type/i), { target: { value: 'starships' } });
    fireEvent.change(screen.getByLabelText(/name/i), { target: { value: 'Millennium Falcon' } });
    fireEvent.click(screen.getByRole('button', { name: /search/i }));
  
    await waitFor(() =>
      expect(screen.getByText((_, el) => el.textContent === 'Type: starships')).toBeInTheDocument()
    );
  
    expect(screen.getByText((_, el) => el.textContent === 'Name: Millennium Falcon')).toBeInTheDocument();
    expect(screen.getByText((_, el) => el.textContent === 'Count: 1')).toBeInTheDocument();
    expect(screen.getByText('Film1')).toBeInTheDocument();
  });
  

  test('toggles between online and offline mode', () => {
    render(<SearchForm />);
  
    // Assert initial state (Online Mode)
    expect(screen.getByText((_, element) =>
      element.textContent === 'Mode: Online (live API)'
    )).toBeInTheDocument();
  
    // Switch to offline mode
    fireEvent.click(screen.getByRole('button', { name: /switch to offline mode/i }));
    expect(screen.getByText((_, element) =>
      element.textContent === 'Mode: Offline (cache)'
    )).toBeInTheDocument();
  
    // Switch back to online mode
    fireEvent.click(screen.getByRole('button', { name: /switch to online mode/i }));
    expect(screen.getByText((_, element) =>
      element.textContent === 'Mode: Online (live API)'
    )).toBeInTheDocument();
  });
});
