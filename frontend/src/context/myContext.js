// MyContext.js
import React from 'react';
import { useState } from 'react';

const MyContext = React.createContext(null);

export const MyContextProvider = ({ children }) => {
  const [token, setToken ] = useState(null);

  return <MyContext.Provider value={{token, setToken}}>{children}</MyContext.Provider>;
};

export default MyContext;
