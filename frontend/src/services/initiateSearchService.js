
/**
 * Initiates a Kafka-backed search by sending a request to the backend.
 * @param {string} type - Entity type (e.g. "planets", "people")
 * @param {string} name - Entity name to search for
 * @returns {Promise<{ requestId: string }>}
 */
export const initiateSearch = async (type, name) => {
    try {
      const response = await axios.get('/api/search', {
        params: { type, name },
        withCredentials: true,
      });
  
      if (!response.data?.requestId) {
        throw new Error('Invalid response: requestId not found');
      }
  
      return { requestId: response.data.requestId };
    } catch (error) {
      console.error('Error initiating search:', error);
      throw error;
    }
  };