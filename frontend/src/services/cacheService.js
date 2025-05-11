export const getCachedData = (type, name) => {
    try {
      const cached = localStorage.getItem(`${type}-${name}`);
      return cached ? JSON.parse(cached) : null;
    } catch (e) {
      console.error("Error accessing cache", e);
      return null;
    }
  };
  
  export const storeInCache = (type, name, data) => {
    try {
      console.log("Data stored in cache",data.count)
      localStorage.setItem(`${type}-${name}`, JSON.stringify(data));
    } catch (e) {
      console.error("Error storing to cache", e);
    }
  };
  