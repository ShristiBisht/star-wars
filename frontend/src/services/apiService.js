import axios from 'axios';

export const fetchEntityData = async (type, name) => {
  try {
    const response = await axios.get('/api/search', {
      params: { type, name },
      withCredentials: true,
    });
    console.log("response is",response)
    // Validate that the response is a proper object (optional, but useful)
    if (typeof response.data !== 'object' || response.data === null) {
      throw new Error('Invalid response: expected a JSON object.');
    }
     return { data: response.data };
  } catch (error) {
    // If it's an Axios error, log more details
    if (error.response) {
      console.error('Backend responded with error:', {
        status: error.response.status,
        data: error.response.data,
      });
    } else if (error.request) {
      console.error('Request made but no response received:', error.request);
    } else {
      console.error('Error setting up the request:', error.message);
    }

    // Optionally: rethrow to handle it in the calling component
    throw error;
  }
};