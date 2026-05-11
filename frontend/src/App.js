// src/App.js
import React from "react";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";

import PatientList from "./components/PatientList";
import PatientDetail from "./components/PatientDetail";
import PatientForm from "./components/PatientForm";
import Login from "./components/Login";

const TOKEN_KEY = "auth_token";

const isAuthenticated = () => {
  const token = localStorage.getItem(TOKEN_KEY);
  if (!token) return false;
  try {
    const [, payload] = token.split(".");
    const { exp } = JSON.parse(atob(payload));
    if (exp && exp * 1000 < Date.now()) {
      localStorage.removeItem(TOKEN_KEY);
      return false;
    }
    return true;
  } catch {
    localStorage.removeItem(TOKEN_KEY);
    return false;
  }
};

const PrivateRoute = ({ children }) => {
  return isAuthenticated() ? children : <Navigate to="/login" />;
};

function App() {
  return (
    <Router>
      <div className="container mt-3">
        <Routes>
          <Route
            path="/"
            element={
              isAuthenticated() ? (
                <Navigate to="/patients" />
              ) : (
                <Navigate to="/login" />
              )
            }
          />

          <Route path="/login" element={<Login />} />

          <Route
            path="/patients"
            element={
              <PrivateRoute>
                <PatientList />
              </PrivateRoute>
            }
          />

          <Route
            path="/patients/:id"
            element={
              <PrivateRoute>
                <PatientDetail />
              </PrivateRoute>
            }
          />

          <Route
            path="/patients/new"
            element={
              <PrivateRoute>
                <PatientForm />
              </PrivateRoute>
            }
          />

          <Route
            path="/patients/edit/:id"
            element={
              <PrivateRoute>
                <PatientForm />
              </PrivateRoute>
            }
          />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
