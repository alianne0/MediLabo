//import { createContext, useContext, useState, useEffect } from "react";
//
//const AuthContext = createContext(null);
//
//export const AuthProvider = ({ children }) => {
//  const [token, setToken] = useState(null);
//  const [initialized, setInitialized] = useState(false);
//
//  useEffect(() => {
//    const t = localStorage.getItem("auth_token");
//    setToken(t);
//    setInitialized(true);
//  }, []);
//
//  return (
//    <AuthContext.Provider value={{ token, setToken, initialized }}>
//      {children}
//    </AuthContext.Provider>
//  );
//};
//
//export const useAuth = () => useContext(AuthContext);