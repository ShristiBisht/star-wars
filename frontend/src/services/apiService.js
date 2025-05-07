import axios from 'axios';

export const fetchEntityData = async (type, name) => {
  const response = await axios.get(`/api/search`, {
    params: { type, name },
    withCredentials: true,
  });
  return response.data;
};
