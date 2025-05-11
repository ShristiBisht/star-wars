import axios from 'axios';


/**
 * Polls the backend until a response is available for the given requestId.
 * @param {string} requestId - UUID returned by initiateSearch
 * @returns {Promise<Object|null>} - The search result or null if timed out
 */
export const pollSearchResult = async (requestId) => {
  const maxRetries = 20;
  const delay = 1500; // 1.5 seconds
  let attempts = 0;

  while (attempts < maxRetries) {
    try {
      const response = await axios.get(`/api/result/${requestId}`, {
        withCredentials: true,
      });

      if (response.status === 200 && response.data) {
        return response.data;
      }
    } catch (err) {
      if (err.response?.status !== 404) {
        console.error("Polling error:", err);
        throw err;
      }
    }

    await new Promise((resolve) => setTimeout(resolve, delay));
    attempts++;
  }

  console.warn("Polling timed out.");
  return null;
};
