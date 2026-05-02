//import { Navigate } from "react-router-dom";
//import { useAuth } from "./AuthProvider";
//
//const PrivateRoute = ({ children }) => {
//  const { token, initialized } = useAuth();
//
//  if (!initialized) return null; // wait for storage load
//
//  return token ? children : <Navigate to="/login" replace />;
//};
//
//export default PrivateRoute;