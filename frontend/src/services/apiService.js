import axios from 'axios';

export const fetchEntityData = async (type, name) => {
  const response = await axios.get(`http://localhost:8080/search`, {
    params: { type, name },
    withCredentials: true,
  });
  return response.data;
};
