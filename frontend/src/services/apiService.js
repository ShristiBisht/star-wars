import axios from 'axios';

export const fetchEntityData = async (type, name,offlineMode) => {
  const response = await axios.get(`/api/search`, {
    params: { type, name,offlineMode: offlineMode ? 'true' : 'false' },
    withCredentials: true,
  });
  return response.data;
};
