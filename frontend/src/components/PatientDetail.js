import { useEffect, useState, useCallback } from "react";
import { useParams, Link } from "react-router-dom";
import axios from "axios";

const API_URL = "http://localhost:8080/api/patients";

function PatientDetail() {
  const { id } = useParams();

  const [patient, setPatient] = useState({
    firstName: "",
    lastName: "",
    dateOfBirth: "",
    gender: "",
    address: "",
    phone: ""
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  /* Load patient (same pattern as PatientForm) */
  const loadPatient = useCallback(async () => {
    if (!id) return;

    setLoading(true);
    setError(null);

    try {
      const response = await axios.get(`${API_URL}/${id}`);
      setPatient(response.data);
    } catch (err) {
      console.error("Error loading patient:", err);
      setError("Failed to load patient");
    } finally {
      setLoading(false);
    }
  }, [id]);

  useEffect(() => {
    loadPatient();
  }, [loadPatient]);

  if (loading) return <p>Loading...</p>;
  if (error) return <p className="text-danger">{error}</p>;

  return (
    <div>
      <h2>Patient Details</h2>

      <div className="mb-2">
        <strong>Name:</strong> {patient.firstName} {patient.lastName}
      </div>

      <div className="mb-2">
        <strong>Date of Birth:</strong> {patient.dateOfBirth}
      </div>

      <div className="mb-2">
        <strong>Gender:</strong> {patient.gender}
      </div>

      <div className="mb-2">
        <strong>Address:</strong> {patient.address}
      </div>

      <div className="mb-3">
        <strong>Phone:</strong> {patient.phone}
      </div>

      <Link to={`/patients/edit/${id}`} className="btn btn-warning me-2">
        Edit
      </Link>

      <Link to="/patients" className="btn btn-secondary">
        Back
      </Link>
    </div>
  );
}

export default PatientDetail;