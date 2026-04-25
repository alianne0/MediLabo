// src/App.js
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';

import Home from './components/Home';
import PatientList from './components/PatientList';
import PatientDetail from './components/PatientDetail';
import PatientForm from './components/PatientForm';

function App() {
  return (
    <Router>
      <div className="container mt-3">
        <Routes>
          <Route path="/" element={<Home />} />

          {/* Patients */}
          <Route path="/patients" element={<PatientList />} />
          <Route path="/patients/:id" element={<PatientDetail />} />
          <Route path="/patients/new" element={<PatientForm />} />
          <Route path="/patients/edit/:id" element={<PatientForm />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;